package com.example.repository;

import com.example.model.AdminSuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminSuperAdminRepo extends JpaRepository<AdminSuperAdmin,Long> {
    @Query(value = "select * from get_admin_by_id(?1)", nativeQuery = true)
    public AdminSuperAdmin findAdminById(Long adminId);
    @Query(value = "select count(*) from get_retailer_cart_items(?1)", nativeQuery = true)
    public int getCartSize(Long retailerId);
}
