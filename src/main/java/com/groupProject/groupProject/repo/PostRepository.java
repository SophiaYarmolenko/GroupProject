package com.groupProject.groupProject.repo;


import com.groupProject.groupProject.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post,Long> {
}