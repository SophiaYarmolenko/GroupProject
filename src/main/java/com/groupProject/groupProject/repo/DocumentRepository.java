package com.groupProject.groupProject.repo;

import com.groupProject.groupProject.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}
