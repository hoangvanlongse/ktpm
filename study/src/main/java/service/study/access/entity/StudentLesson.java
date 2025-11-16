package service.study.access.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "student_lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StudentLesson.StudentLessonId.class)
public class StudentLesson {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    
    @Column(name = "status")
    private String status;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentLessonId implements Serializable {
        private Long student;
        private Long lesson;
    }
}