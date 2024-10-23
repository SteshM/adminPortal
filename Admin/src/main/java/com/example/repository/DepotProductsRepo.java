package com.example.repository;

import com.example.model.Depot;
import com.example.model.DepotProduct;
import com.example.model.Product;
import com.example.model.QuantityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotProductsRepo extends JpaRepository<DepotProduct,Long> {
    public DepotProduct findByProductAndQuantityAtAndDepot(Product product , QuantityAttribute quantityAttribute, Depot depot);
    public DepotProduct findByProductAndQuantityAt(Product product , QuantityAttribute quantityAttribute);
    public DepotProduct findByProduct(Product product);
    public List<DepotProduct> findByDepot(Depot depot);
}
