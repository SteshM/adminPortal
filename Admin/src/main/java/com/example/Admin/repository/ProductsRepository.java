package com.example.Admin.repository;

import com.example.Admin.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product,Long> {
    public Product findByProductId(Long productId);

}
