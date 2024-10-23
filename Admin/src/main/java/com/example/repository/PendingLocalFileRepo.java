package com.example.repository;

import com.example.model.PendingLocalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingLocalFileRepo extends JpaRepository<PendingLocalFile,Long> {
}
