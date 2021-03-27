package com.groupProject.groupProject.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
}
