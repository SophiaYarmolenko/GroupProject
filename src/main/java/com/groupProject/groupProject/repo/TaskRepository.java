package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository  extends CrudRepository<Task,Long> {
}
