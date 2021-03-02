package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.ToDoItem;
import com.groupProject.groupProject.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    List<ToDoItem> findAllByUser(User user);
    Boolean existsByUser(User user);
}
