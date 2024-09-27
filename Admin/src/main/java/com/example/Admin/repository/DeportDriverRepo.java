package com.example.Admin.repository;

import com.example.Admin.model.DeportDriver;
import com.example.Admin.model.Depot;
import com.example.Admin.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeportDriverRepo extends JpaRepository<DeportDriver,Long> {

    DeportDriver findByDriver(MyUser user);
    DeportDriver findByDriverAndDepot(MyUser driver, Depot depot);
}
