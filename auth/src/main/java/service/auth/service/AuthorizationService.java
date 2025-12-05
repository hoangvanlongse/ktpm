package service.auth.service;

import java.util.HashMap;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthorizationService implements IAuthorizationService {
    @Override
    public boolean canAccess(HttpServletRequest req) {
        // POST /api/study/students, anyone
        // POST /api/study/instructors, anyone
        // GET /api/study/courses, anyone
        // GET /api/study/schedules/courses, anyone
        HashMap<String, HttpMethod> publicPaths = new HashMap<>();

        publicPaths.put("/api/study/courses", HttpMethod.GET);
        publicPaths.put("/api/study/schedules/courses", HttpMethod.GET);
        publicPaths.put("/api/study/students", HttpMethod.POST);
        publicPaths.put("/api/study/instructors", HttpMethod.POST);

        String path = req.getRequestURI();
        HttpMethod reqMethod = HttpMethod.valueOf(req.getMethod());

        for (String publicPath : publicPaths.keySet()) {
            if (path.startsWith(publicPath) && reqMethod.equals(publicPaths.get(publicPath)))
                return true;
        }

        return false;
    }

    @Override
    public boolean canAccess(String userRoles, String path, HttpMethod reqMethod) {

        if (!"STUDENT".equals(userRoles) && !"TEACHER".equals(userRoles))
            return false;

        // GET /api/submit/student-exam, student, teacher
        // GET /api/submit/question/exam, teacher, student
        // GET /api/study/chapters/course", student, teacher
        // GET /api/study/lessons, student, teacher
        // PUT /api/study/students", teacher, student
        // GET /api/study/students", teacher, student
        HashMap<String, Set<HttpMethod>> studentOrTeacherPaths = new HashMap<>();
        studentOrTeacherPaths.put("/api/submit/student-exam", Set.of(HttpMethod.GET));
        studentOrTeacherPaths.put("/api/submit/question/exam", Set.of(HttpMethod.GET));
        studentOrTeacherPaths.put("/api/study/chapters/course", Set.of(HttpMethod.GET));
        studentOrTeacherPaths.put("/api/study/lessons", Set.of(HttpMethod.GET));
        studentOrTeacherPaths.put("/api/submit/exam/instructor", Set.of(HttpMethod.GET));
        studentOrTeacherPaths.put("/api/study/students", Set.of(HttpMethod.GET, HttpMethod.PUT));

        for (String curPath : studentOrTeacherPaths.keySet()) {
            if (path.startsWith(curPath) && studentOrTeacherPaths.get(curPath).contains(reqMethod))
                return true;
        }

        if ("STUDENT".equals(userRoles)) {
            // GET /api/study/students/, student
            // GET /api/study/schedules, student
            // GET /api/submit/exam, student
            // POST /api/study/students, student
            // POST /api/study/schedules, student
            // POST /api/submit/student-exam, student
            HashMap<String, Set<HttpMethod>> studentOnlyPaths = new HashMap<>();
            studentOnlyPaths.put("/api/study/students/", Set.of(HttpMethod.GET));
            studentOnlyPaths.put("/api/study/students", Set.of(HttpMethod.POST));
            studentOnlyPaths.put("/api/study/schedules", Set.of(HttpMethod.GET, HttpMethod.POST));
            studentOnlyPaths.put("/api/submit/exam", Set.of(HttpMethod.GET));
            studentOnlyPaths.put("/api/submit/student-exam", Set.of(HttpMethod.POST));

            for (String curPath : studentOnlyPaths.keySet()) {
                if (path.startsWith(curPath) && studentOnlyPaths.get(curPath).contains(reqMethod))
                    return true;
            }
        }

        if ("TEACHER".equals(userRoles)) {
            // GET /api/study/chapters, teacher
            // POST /api/study/courses, teacher
            // POST /api/study/chapters, teacher
            // POST /api/study/lessons, teacher
            // POST /api/submit/exam, teacher
            // POST /api/submit/question, teacher
            // PUT /api/study/courses, teacher
            // PUT /api/study/chapters, teacher
            // PUT /api/submit/question, teacher
            // PUT /api/study/lessons, teacher
            // PUT /api/study/instructors, teacher
            // PUT /api/submit/exam, teacher
            // DEL /api/study/lessons, teacher
            // DEL /api/study/chapters, teacher
            // DEL /api/submit/exam, teacher
            // DEL /api/study/courses, teacher
            // DEL /api/submit/question, teacher
            HashMap<String, Set<HttpMethod>> teacherOnlyPaths = new HashMap<>();
            teacherOnlyPaths.put("/api/study/chapters", Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE));
            teacherOnlyPaths.put("/api/study/courses", Set.of(HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE));
            teacherOnlyPaths.put("/api/study/lessons", Set.of(HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE));
            teacherOnlyPaths.put("/api/submit/exam", Set.of(HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE));
            teacherOnlyPaths.put("/api/submit/question", Set.of(HttpMethod.POST, HttpMethod.PUT,
                    HttpMethod.DELETE));
            teacherOnlyPaths.put("/api/study/instructors", Set.of(HttpMethod.PUT));

            for (String curPath : teacherOnlyPaths.keySet()) {
                // System.out.println(curPath + '\n' + teacherOnlyPaths.get(curPath));
                if (path.startsWith(curPath) && teacherOnlyPaths.get(curPath).contains(reqMethod))
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
