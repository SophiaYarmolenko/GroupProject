package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.CoursesAndUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesAndUsersRepository extends JpaRepository<CoursesAndUsers, Long> {
    CoursesAndUsers findCoursesAndUsersByCourseIdAndAndUserId(Long courseId,Long userId);
}
