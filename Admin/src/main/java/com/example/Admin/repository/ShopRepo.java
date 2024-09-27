package com.example.Admin.repository;

import com.example.Admin.model.MyUser;
import com.example.Admin.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepo extends JpaRepository<Shop,Long> {
    public List<Shop> findByMyUser(MyUser retailer);
    public Shop findByShopIdAndMyUser(Long shopId, MyUser retailer);
    public List<Shop> findByMyUserAndSoftDelete(MyUser retailer, boolean b);
}
