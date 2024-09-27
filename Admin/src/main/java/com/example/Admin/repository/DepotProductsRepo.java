package com.example.Admin.repository;

import com.example.Admin.model.Depot;
import com.example.Admin.model.DepotProduct;
import com.example.Admin.model.Product;
import com.example.Admin.model.QuantityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotProductsRepo extends JpaRepository<DepotProductsRepo,Long> {
    public DepotProduct findByProductAndQuantityAtAndDepot(Product product , QuantityAttribute quantityAttribute, Depot depot);
    public DepotProduct findByProductAndQuantityAt(Product product , QuantityAttribute quantityAttribute);
    public DepotProduct findByProduct(Product product);
    public List<DepotProduct> findByDepot(Depot depot);
}
