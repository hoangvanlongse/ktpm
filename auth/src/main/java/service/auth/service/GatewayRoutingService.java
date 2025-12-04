package service.auth.service;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class GatewayRoutingService {

    // private final WebClient

    public ResponseEntity<?> forwardRequest(HttpServletRequest req, String address) {
        try {
            String url = address + req.getRequestURI();

            RestTemplate restTemplate = new RestTemplate();
            HttpMethod method = HttpMethod.valueOf(req.getMethod());
            HttpEntity<byte[]> requestEntity = convertHttpServletRequestToHttpEntity(req);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    method,
                    requestEntity,
                    byte[].class // ALWAYS forward raw bytes
            );

            // Copy status, headers, body back to client
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());

        } catch (IOException | RestClientException e) {
            return ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(e.getMessage().substring(0,
                    3))))
                    .body("Gateway error: " + e.getMessage());
        }
    }

    private HttpEntity<byte[]> convertHttpServletRequestToHttpEntity(HttpServletRequest request)
            throws IOException {

        // 1. Copy headers
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            // Skip headers Spring will add automatically
            if (headerName.equalsIgnoreCase("content-length"))
                continue;

            Enumeration<String> values = request.getHeaders(headerName);
            while (values.hasMoreElements()) {
                headers.add(headerName, values.nextElement());
            }
        }

        // 2. Read the body into byte[]
        byte[] body = request.getInputStream().readAllBytes();

        return new HttpEntity<>(body, headers);
    }
}