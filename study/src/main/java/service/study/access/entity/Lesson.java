package service.study.access.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resource", columnDefinition = "jsonb")
    private JsonNode resource;
    
    @Column(name = "image")
    private String image;
    
    @Column(name = "position")
    private Integer position;
    
    @Column(name = "duration")
    private Integer duration; // in minutes
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}