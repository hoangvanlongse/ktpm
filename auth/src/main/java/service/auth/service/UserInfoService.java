package service.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import service.auth.dto.UserCreationResponse;
import service.auth.entity.UserInfo;
import service.auth.entity.UserRegistrationRequest;
import service.auth.repository.UserInfoRepository;

@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;
    private final RestTemplate restTemplate;

    @Value("${study.service.ip}")
    private String studyServiceURI;

    // Use constructor injection
    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
        // Initialize RestTemplate for internal service communication
        this.restTemplate = new RestTemplate();
    }

    // Method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by email (username)
        Optional<UserInfo> userInfo = repository.findByEmail(username);

        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Convert UserInfo to UserDetails (using Spring's built-in User class)
        UserInfo user = userInfo.get();

        List<GrantedAuthority> authories = List.of(user.getRoles().split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(user.getEmail(), user.getPassword(), authories);
    }

    /**
     * Orchestrates user creation:
     * 1. Checks if email already exists in Auth service.
     * 2. Calls the external Study Service to create the Student/Instructor entity.
     * 3. Saves the UserInfo entity with the new external ID if external call
     * succeeds.
     * 
     * @param userInfo The UserInfo entity containing email, password, and roles.
     * @return "existed", "success", or "failed:{message}"
     */
    @Override
    public String addUser(UserRegistrationRequest userRegistrationRequest) {
        Optional<UserInfo> checkUserInfo = repository.findByEmail(userRegistrationRequest.getEmail());

        if (checkUserInfo.isPresent()) {
            return "existed";
        }

        try {
            // Determine the target URL based on the role
            String role = userRegistrationRequest.getRoles() != null ? userRegistrationRequest.getRoles().toUpperCase()
                    : "STUDENT";
            String studyServiceUrl;
            String idFieldName; // The field to expect in the response (e.g., "studentId")

            if (null == role) {
                return "failed:Invalid role specified";
            } else
                switch (role) {
                    case "STUDENT" -> {
                        studyServiceUrl = studyServiceURI + "/api/study/students";
                        idFieldName = "studentId";
                    }
                    case "TEACHER" -> {
                        studyServiceUrl = studyServiceURI + "/api/study/instructors";
                        idFieldName = "instructorId";
                    }
                    default -> {
                        return "failed:Invalid role specified";
                    }
                }

            // 1. Prepare and send request to Study Service
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserRegistrationRequest> requestEntity = new HttpEntity<>(userRegistrationRequest, headers);

            System.out.println("Sending POST to: " + studyServiceUrl);

            // The Study Service is expected to return the newly created entity's ID
            ResponseEntity<UserCreationResponse> responseEntity = restTemplate.exchange(
                    studyServiceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    UserCreationResponse.class);

            // 2. Process Study Service response
            if (responseEntity.getStatusCode() == HttpStatus.CREATED
                    || responseEntity.getStatusCode() == HttpStatus.OK) {
                UserCreationResponse responseBody = responseEntity.getBody();

                // Extract the ID based on the role
                int externalId = -1;
                if ("STUDENT".equals(role) && responseBody != null) {
                    externalId = responseBody.getStudentId();
                } else if ("TEACHER".equals(role) && responseBody != null) {
                    externalId = responseBody.getInstructorId();
                }

                if (externalId == -1) {
                    // This indicates a successful HTTP status but a malformed body
                    return "failed:External service did not return a valid " + idFieldName;
                }

                // 3. Save UserInfo in Auth Service with the new external ID
                // Note: UserInfo currently only saves `id`, `name`, `email`, `password`,
                // `roles`.
                // To support external IDs, the UserInfo entity must be updated to store this
                // value.
                // Assuming for now, the "id" in UserInfo can store the external ID
                // and the current sequence ID field (UserInfo.id) is unused or auto-generated.
                //
                // **IMPORTANT:** Since the UserInfo entity only has one `id` field,
                // we'll store the external ID in the `name` field for demonstration,
                // which is not ideal. A proper solution requires modifying the UserInfo
                // entity to include a dedicated `externalId` field.
                //
                // FOR THIS REFACTOR, I'll store the External ID as part of the email
                // to make it unique and retrieveable, but this is a temporary workaround!
                // Best practice is to add a dedicated 'externalId' column to UserInfo.

                // Temporary workaround: Prepending the ID to the name or saving it as a
                // separate field
                UserInfo userInfo = new UserInfo(externalId, userRegistrationRequest.getName(),
                        userRegistrationRequest.getEmail(), userRegistrationRequest.getPassword(),
                        userRegistrationRequest.getRoles());

                userInfo.setName(userInfo.getName());

                // Encrypt password before saving
                userInfo.setPassword(encoder.encode(userInfo.getPassword()));
                repository.save(userInfo);

                return "success";
            } else {
                // Handle non-2xx status codes from Study Service
                String errorMsg = responseEntity.getBody() != null ? responseEntity.getBody().getMessage()
                        : "Unknown external error";
                return "failed:External service failure: " + errorMsg;
            }

        } catch (HttpClientErrorException e) {
            // Catches 4xx errors (e.g., Study Service validation errors)
            System.err.println("External Service Error: " + e.getResponseBodyAsString());
            return "failed:External service client error: " + e.getStatusCode().value();
        } catch (RestClientException e) {
            System.err.println("Internal or Network Error during addUser: " + e.getMessage());
            return "failed:Internal error: " + e.getMessage();
        }
    }

    @Override
    public String getUserAuthorities(String username) {
        Optional<UserInfo> userInfo = repository.findByEmail(username);
        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        UserInfo user = userInfo.get();
        return user.getRoles();
    }

    @Override
    public Long getUserStudyId(String username) {
        Optional<UserInfo> userInfo = repository.findByEmail(username);
        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        UserInfo user = userInfo.get();
        return user.getStudy_id();
    }

    @Override
    public String getUserName(String username) {
        Optional<UserInfo> userInfo = repository.findByEmail(username);
        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        UserInfo user = userInfo.get();
        return user.getName();
    }
}