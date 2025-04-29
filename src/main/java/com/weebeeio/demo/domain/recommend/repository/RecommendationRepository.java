package com.weebeeio.demo.domain.recommend.repository;

import com.weebeeio.demo.domain.recommend.dao.ProductStock;
import com.weebeeio.demo.domain.recommend.dao.ProductDeposit;
import com.weebeeio.demo.domain.recommend.dao.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<UserStats, Integer> {
    
    @Query("SELECT s FROM ProductStock s JOIN UserStats st ON st.userId = :userId WHERE s.stockRank = st.stockUnderstanding")
    List<ProductStock> findStockRecommendations(@Param("userId") int userId);

    @Query("SELECT d FROM ProductDeposit d JOIN UserStats st ON st.userId = :userId WHERE d.depositRank = st.depositUnderstanding")
    List<ProductDeposit> findDepositRecommendations(@Param("userId") int userId);
} 