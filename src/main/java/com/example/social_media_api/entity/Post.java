package com.example.social_media_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int likesCount;
    @Column (name="content" , nullable = false)

    private String content;
    @Column (name="title" , nullable = false)
    private String title;
    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> likes = new HashSet<>();
    @JsonIgnore
    @ManyToOne
    private User user;
    @UpdateTimestamp
    private LocalDateTime createdAt;
}
