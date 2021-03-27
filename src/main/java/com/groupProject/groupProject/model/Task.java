package com.groupProject.groupProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "task")
    private String task;
    @Column(name = "time")
    private String time;
    @ManyToOne
    private  Course course;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();
    public void removeDocument(Document document) {
        documents.remove( document );
        document.setTask( null );
    }
    public void addDocument(Document document) {
        documents.add( document );
        document.setTask( this );
    }
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> tasks = new ArrayList<>();


    public void removeComment(Comment comment) {
        tasks.remove( comment );
        comment.setTask( null );
    }
    public void addComment(Comment comment) {
        tasks.add( comment );
        comment.setTask( this );
    }

}
