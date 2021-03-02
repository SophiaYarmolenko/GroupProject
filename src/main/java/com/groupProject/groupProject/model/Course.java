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
public class Course
{
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
}
