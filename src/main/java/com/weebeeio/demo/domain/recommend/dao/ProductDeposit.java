package com.weebeeio.demo.domain.recommend.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_deposit")
@Getter
@NoArgsConstructor
public class ProductDeposit {
    
    @Id
    @Column(name = "deposit_no")
    private Integer depositNo;
    
    @Column(name = "deposit_name")
    private String depositName;
    
    @Column(name = "deposit_rank")
    private Integer depositRank;
    
    @Column(name = "deposit_detail")
    private String depositDetail;
} 