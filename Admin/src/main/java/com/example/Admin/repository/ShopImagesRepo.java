package com.example.Admin.repository;

import com.example.Admin.model.Shop;
import com.example.Admin.model.ShopImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopImagesRepo extends JpaRepository<ShopImages,Long> {
    List<ShopImages> findByShop(Shop shop);

}
