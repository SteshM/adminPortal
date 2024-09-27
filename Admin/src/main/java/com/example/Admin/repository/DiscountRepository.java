package com.example.Admin.repository;

import com.example.Admin.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount,Long> {
}
