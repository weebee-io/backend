package com.weebeeio.demo.domain.recommend.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_stock")
@Getter
@NoArgsConstructor
public class ProductStock {
    
    @Id
    @Column(name = "stock_no")
    private Integer stockNo;
    
    @Column(name = "stock_name")
    private String stockName;
    
    @Column(name = "stock_rank")
    private Integer stockRank;
    
    @Column(name = "stock_detail")
    private String stockDetail;

    @Column(name = "stock_url")
    private String stockUrl;
} 