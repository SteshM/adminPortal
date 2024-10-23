package com.example.repository;

import com.example.model.Shop;
import com.example.model.ShopImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopImagesRepo extends JpaRepository<ShopImages,Long> {
    List<ShopImages> findByShop(Shop shop);

}
