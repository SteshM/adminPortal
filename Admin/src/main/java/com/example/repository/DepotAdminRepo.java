package com.example.repository;

import com.example.model.Depot;
import com.example.model.DepotAdmin;
import com.example.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotAdminRepo extends JpaRepository<DepotAdmin,Long> {
    public List<DepotAdmin> findByMyUser(MyUser admin);
    public DepotAdmin findByMyUserAndDepot(MyUser admin, Depot depot);
    public List<DepotAdmin> findByDepot(Depot depot);

}
