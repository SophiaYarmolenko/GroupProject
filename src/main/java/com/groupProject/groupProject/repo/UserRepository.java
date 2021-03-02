package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     User findByEmail(String email);
}
