package com.example.Admin.repository;

import com.example.Admin.dto.DepotOrderResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepotOrderResponseRepo extends JpaRepository<DepotOrderResponseDto,Long> {
    @Query(value = "select * from get_depot_orders_assigned_for_admin(?1)", nativeQuery = true)
    public List<DepotOrderResponseDto> findAllAdminAssignedRestockOrders(Long adminId);
    @Query(value = "select * from get_depot_orders_for_admin(?1)", nativeQuery = true)
    public List<DepotOrderResponseDto> findAllAdminRestockOrders(Long adminId);
}
