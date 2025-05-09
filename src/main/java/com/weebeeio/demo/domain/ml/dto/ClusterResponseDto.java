package com.weebeeio.demo.domain.ml.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClusterResponseDto {
    private int cluster;
    private double proba;
    
}
