package com.example.Admin.repository;

import com.example.Admin.model.AdminSuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSuperRepo extends JpaRepository<AdminSuperAdmin,Long> {
    public AdminSuperAdmin findByAdminId(Long adminId);

}
