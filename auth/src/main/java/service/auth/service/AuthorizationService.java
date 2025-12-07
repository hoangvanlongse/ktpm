package service.auth.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthorizationService implements IAuthorizationService {

    private final Map<String, HttpMethod> publicPaths;
    private final Map<String, Set<HttpMethod>> studentOrTeacherPaths;
    private final Map<String, Set<HttpMethod>> studentOnlyPaths;
    private final Map<String, Set<HttpMethod>> teacherOnlyPaths;

    public AuthorizationService(
            @Qualifier("publicPaths") Map<String, HttpMethod> publicPaths,
            @Qualifier("studentOrTeacherPaths") Map<String, Set<HttpMethod>> studentOrTeacherPaths,
            @Qualifier("studentOnlyPaths") Map<String, Set<HttpMethod>> studentOnlyPaths,
            @Qualifier("teacherOnlyPaths") Map<String, Set<HttpMethod>> teacherOnlyPaths) {
        this.publicPaths = publicPaths;
        this.studentOrTeacherPaths = studentOrTeacherPaths;
        this.studentOnlyPaths = studentOnlyPaths;
        this.teacherOnlyPaths = teacherOnlyPaths;
    }

    @Override
    public boolean canAccess(HttpServletRequest req) {
        String path = req.getRequestURI();
        HttpMethod reqMethod = HttpMethod.valueOf(req.getMethod());

        for (Map.Entry<String, HttpMethod> entry : publicPaths.entrySet()) {
            if (path.startsWith(entry.getKey()) && reqMethod.equals(entry.getValue()))
                return true;
        }

        return false;
    }

    @Override
    public boolean canAccess(String userRoles, String path, HttpMethod reqMethod) {

        if (!"STUDENT".equals(userRoles) && !"TEACHER".equals(userRoles))
            return false;

        // Check paths accessible by both Student and Teacher
        for (Map.Entry<String, Set<HttpMethod>> entry : studentOrTeacherPaths.entrySet()) {
            if (path.startsWith(entry.getKey()) && entry.getValue().contains(reqMethod))
                return true;
        }

        // Check paths accessible only by Student
        if ("STUDENT".equals(userRoles)) {
            for (Map.Entry<String, Set<HttpMethod>> entry : studentOnlyPaths.entrySet()) {
                if (path.startsWith(entry.getKey()) && entry.getValue().contains(reqMethod))
                    return true;
            }
        }

        // Check paths accessible only by Teacher
        if ("TEACHER".equals(userRoles)) {
            for (Map.Entry<String, Set<HttpMethod>> entry : teacherOnlyPaths.entrySet()) {
                if (path.startsWith(entry.getKey()) && entry.getValue().contains(reqMethod))
                    return true;
            }
        }

        return false;
    }
}

// POST /api/study/students, anyone
// POST /api/study/instructors, anyone
// GET /api/study/courses, anyone
// GET /api/study/schedules/courses, anyone

// GET /api/study/chapters/course/1", student, teacher
// GET /api/study/lessons", student, teacher
// GET /api/study/lessons/chapter/1", student, teacher
// GET /api/submit/student-exam/student/{{studentId}}", student, teacher
// GET /api/submit/student-exam/exam/{{examId}}", student, teacher
// GET /api/submit/student-exam/{{id}}", student, teacher
// GET /api/submit/student-exam/student001/exam001", student, teacher
// PUT /api/study/students/1", teacher, student
// GET /api/submit/question/exam/{{examId}}", teacher, student
// GET /api/submit/exam/instructor/{{instructorId}}", teacher, student
// GET /api/study/students", teacher, student

// POST /api/study/courses/1", teacher
// PUT /api/study/courses/1", teacher
// DEL /api/study/courses", teacher
// GET /api/study/chapters/1", teacher
// POST /api/study/chapters/1", teacher
// PUT /api/study/chapters", teacher
// DEL /api/study/chapters/1", teacher
// POST /api/study/lessons/1", teacher
// PUT /api/study/lessons/1", teacher
// DEL /api/study/lessons/1", teacher
// PUT /api/study/instructors/1", teacher
// POST /api/submit/exam", teacher
// PUT /api/submit/exam/{{examId}}", teacher
// DEL /api/submit/exam/{{examId}}", teacher
// POST /api/submit/question", teacher
// PUT /api/submit/question/{{questionId}}", teacher
// DEL /api/submit/question/{{questionId}}", teacher

// GET /api/study/students/1/courses/1/progress", student
// POST /api/study/students/1/lessons/1/join", student
// GET /api/study/schedules/1/courses", student
// POST /api/study/schedules/1/enroll", student
// GET /api/submit/exam/{{examId}}", student
// GET /api/submit/exam/search?keyword=java&type=QUIZ", student
// POST /api/submit/student-exam/submit", student

// /api/instructor/students/1, ?
