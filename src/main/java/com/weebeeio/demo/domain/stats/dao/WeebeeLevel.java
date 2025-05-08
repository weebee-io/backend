package com.weebeeio.demo.domain.stats.dao;

public enum WeebeeLevel {
    LEVEL_1(0, 2, "weebee_lev_1"),
    LEVEL_2(3, 9, "weebee_lev_2"),
    LEVEL_3(10, 19, "weebee_lev_3"),
    LEVEL_4(20, 29, "weebee_lev_4"),
    LEVEL_5(30, Integer.MAX_VALUE, "weebee_lev_5");

    private final int minStatSum;
    private final int maxStatSum;
    private final String imageName;

    WeebeeLevel(int minStatSum, int maxStatSum, String imageName) {
        this.minStatSum = minStatSum;
        this.maxStatSum = maxStatSum;
        this.imageName = imageName;
    }

    public static WeebeeLevel fromStatSum(int statSum) {
        for (WeebeeLevel level : values()) {
            if (statSum >= level.minStatSum && statSum <= level.maxStatSum) {
                return level;
            }
        }
        return LEVEL_1; // 기본값
    }

    public String getImageName() {
        return imageName;
    }
} 