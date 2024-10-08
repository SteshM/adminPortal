package com.example.Admin.repository;

import com.example.Admin.model.Truck;
import com.example.Admin.model.TruckImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TruckImagesRepo extends JpaRepository<TruckImages,Long> {
    public List<TruckImages> findByTruck(Truck truck);

}
