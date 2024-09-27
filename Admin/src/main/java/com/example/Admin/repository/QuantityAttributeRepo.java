package com.example.Admin.repository;

import com.example.Admin.model.Product;
import com.example.Admin.model.QuantityAttribute;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuantityAttributeRepo extends JpaRepository<QuantityAttribute,Long> {
    public QuantityAttribute findByProductAndQuantityAttId(Product product, Long qid);
    public QuantityAttribute findByProduct(Product product, Limit of);
    public List<QuantityAttribute> findByProduct(Product product);

}
