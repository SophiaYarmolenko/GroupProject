package com.groupProject.groupProject.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "courses")
    private List<User> users = new ArrayList<>();

    public Course(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "courses", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Role> roles = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setCourses(this);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
        document.setCourse(null);
    }

    public void addDocument(Document document) {
        documents.add(document);
        document.setCourse(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setCourses(null);
    }
}
