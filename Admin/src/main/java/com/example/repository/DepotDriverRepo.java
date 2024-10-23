package com.example.repository;

import com.example.model.DeportDriver;
import com.example.model.Depot;
import com.example.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepotDriverRepo  extends JpaRepository<DeportDriver,Long> {
    DeportDriver findByDriver(MyUser user);
    DeportDriver findByDriverAndDepot(MyUser driver, Depot depot);
}
