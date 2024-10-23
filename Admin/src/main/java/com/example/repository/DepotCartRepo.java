package com.example.repository;

import com.example.model.DepotCart;
import com.example.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotCartRepo extends JpaRepository<DepotCart,Long> {
    public List<DepotCart> findByAdmin(MyUser admin);
    public DepotCart findByCartIdAndAdmin(Long cartId, MyUser admin);
}
