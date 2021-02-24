package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
}
