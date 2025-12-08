package service.auth.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * Configuration class to centralize all authorization rules.
 * This adheres to SRP and makes rule changes easier without modifying
 * the core logic in AuthorizationService.
 */
@Configuration
public class AuthorizationRuleConfig {

    @Bean(name = "publicPaths")
    public Map<String, HttpMethod> publicPaths() {
        Map<String, HttpMethod> paths = new HashMap<>();

        // Anyone can access these endpoints
        paths.put("/api/study/courses", HttpMethod.GET);
        paths.put("/api/study/schedules/courses", HttpMethod.GET);
        paths.put("/api/study/students", HttpMethod.POST);
        paths.put("/api/study/instructors", HttpMethod.POST);

        return paths;
    }

    @Bean(name = "studentOrTeacherPaths")
    public Map<String, Set<HttpMethod>> studentOrTeacherPaths() {
        Map<String, Set<HttpMethod>> paths = new HashMap<>();

        // Paths accessible by both STUDENT and TEACHER
        paths.put("/api/submit/student-exam", Set.of(HttpMethod.GET));
        paths.put("/api/submit/question/exam", Set.of(HttpMethod.GET));
        paths.put("/api/study/chapters/course", Set.of(HttpMethod.GET));
        paths.put("/api/study/lessons", Set.of(HttpMethod.GET));
        paths.put("/api/submit/exam", Set.of(HttpMethod.GET));
        paths.put("/api/submit/exam/instructor", Set.of(HttpMethod.GET));
        paths.put("/api/study/students", Set.of(HttpMethod.GET, HttpMethod.PUT));

        return paths;
    }

    @Bean(name = "studentOnlyPaths")
    public Map<String, Set<HttpMethod>> studentOnlyPaths() {
        Map<String, Set<HttpMethod>> paths = new HashMap<>();

        // Paths accessible only by STUDENT
        paths.put("/api/study/students/", Set.of(HttpMethod.GET)); // Specific student profile lookup
        paths.put("/api/study/students", Set.of(HttpMethod.POST)); // Although covered by public, for future
        paths.put("/api/study/schedules", Set.of(HttpMethod.GET, HttpMethod.POST));
        paths.put("/api/submit/student-exam", Set.of(HttpMethod.POST));

        return paths;
    }

    @Bean(name = "teacherOnlyPaths")
    public Map<String, Set<HttpMethod>> teacherOnlyPaths() {
        Map<String, Set<HttpMethod>> paths = new HashMap<>();

        // Paths accessible only by TEACHER
        paths.put("/api/study/chapters", Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE));
        paths.put("/api/study/courses", Set.of(HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE));
        paths.put("/api/study/lessons", Set.of(HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE));
        paths.put("/api/submit/exam", Set.of(HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE));
        paths.put("/api/submit/question", Set.of(HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE));
        paths.put("/api/study/instructors", Set.of(HttpMethod.GET, HttpMethod.PUT)); // Update instructor profile

        return paths;
    }
}