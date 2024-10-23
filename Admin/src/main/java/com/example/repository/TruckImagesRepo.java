package com.example.repository;

import com.example.model.Truck;
import com.example.model.TruckImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TruckImagesRepo extends JpaRepository<TruckImages,Long> {
    public List<TruckImages> findByTruck(Truck truck);

}
