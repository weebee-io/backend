package com.weebeeio.demo.domain.recommend.repository;

import com.weebeeio.demo.domain.recommend.dao.FinanceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<FinanceProduct, Integer> {
    
    @Query("SELECT f FROM FinanceProduct f WHERE f.productRank = :userRank")
    List<FinanceProduct> findProductsByUserRank(@Param("userRank") String userRank);
} 