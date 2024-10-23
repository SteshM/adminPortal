package com.example.repository;

import com.example.model.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepotRepository extends JpaRepository<Depot,Long> {
    Depot findByDepotId(Long depotId);

    //   @Query("SELECT DISTINCT dp.depotId FROM DepotProduct dp WHERE dp.productId IN :productIds AND dp.quantity >= :quantities GROUP BY dp.depotId HAVING COUNT(DISTINCT dp.productId) = :productCount")
//    List<Long> findDepotIdsWithProducts(@Param("productIds") List<Long> productIds, @Param("quantities") List<Integer> quantities, @Param("productCount") long productCount);
    @Query(value = "SELECT DISTINCT dp.depot_id FROM depot_product dp WHERE dp.product_id IN :productIds AND dp.quantity >= :quantity GROUP BY dp.depot_id HAVING COUNT(DISTINCT dp.product_id) = :productCount", nativeQuery = true)
    List<Long> findDepotIdsWithProducts(@Param("productIds") List<Long> productIds, @Param("quantity") int quantity, @Param("productCount") long productCount);

}
