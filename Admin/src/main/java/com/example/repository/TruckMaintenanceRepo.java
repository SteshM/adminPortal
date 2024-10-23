package com.example.repository;

import com.example.model.TruckMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckMaintenanceRepo extends JpaRepository <TruckMaintenance,Long> {
}
