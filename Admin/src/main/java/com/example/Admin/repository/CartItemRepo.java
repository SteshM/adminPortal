package com.example.Admin.repository;

import com.example.Admin.model.CartItem;
import com.example.Admin.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item c WHERE c.cart_item_id = ?1 AND c.retailer_id = ?2", nativeQuery = true)
    public void deleteByIdAndRetailerId(Long cartItemId, Long retailerId);

    public List<CartItem> findByRetailer(MyUser retailer);
}
