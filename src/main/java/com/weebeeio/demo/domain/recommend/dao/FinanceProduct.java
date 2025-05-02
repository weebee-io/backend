package com.weebeeio.demo.domain.recommend.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "finance_product")
@Getter
@NoArgsConstructor
public class FinanceProduct {
    
    @Id
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "product_name", length = 255)
    private String productName;
    
    @Column(name = "product_detail", columnDefinition = "TEXT")
    private String productDetail;
    
    @Column(name = "product_rank", length = 45)
    private String productRank;
    
    @Column(name = "product_url", length = 255)
    private String productUrl;
} 