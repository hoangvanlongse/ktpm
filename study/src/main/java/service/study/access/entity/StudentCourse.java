package service.study.access.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "enroll_time")
    private LocalDateTime enrollTime;
    
    @Column(name = "progress")
    private Double progress; // percentage 0-100
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "number_current_lesson")
    private Integer numberCurrentLesson;
    
    @PrePersist
    protected void onCreate() {
        if (enrollTime == null) {
            enrollTime = LocalDateTime.now();
        }
    }
}