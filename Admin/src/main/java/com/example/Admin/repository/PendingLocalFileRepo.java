package com.example.Admin.repository;

import com.example.Admin.model.PendingLocalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingLocalFileRepo extends JpaRepository<PendingLocalFile,Long> {
}
