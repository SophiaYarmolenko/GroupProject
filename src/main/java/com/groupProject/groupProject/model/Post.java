package com.groupProject.groupProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "announce")
    private String announce;

    @Column(name = "post")
    private String post;

    @ManyToOne
    private  Course courses;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    public void removeDocument(Document document) {
        documents.remove( document );
        document.setPost( null );
    }

    public void addDocument(Document document) {
        documents.add( document );
        document.setPost( this );
    }

}
