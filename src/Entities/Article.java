package Entities;

import java.math.BigDecimal;

public class Article {
    public String     code;
    public Integer    palletQuantity;
    public BigDecimal singleUnitWeight;
    public BigDecimal palletWeight;
    public BigDecimal singleUnitVolume;
    public BigDecimal palleteVolume;

    public Integer minQuantity;
    public Integer maxQuantity;
    public Integer maxCapacity;
    public Integer currentQuantity;

    public Article() {}

    public Article(
            String code, Integer palletQuantity, BigDecimal singleUnitWeight,
            BigDecimal palletWeight, BigDecimal singleUnitVolume,
            BigDecimal palleteVolume) {
        this.code = code;
        this.palletQuantity = palletQuantity;
        this.singleUnitWeight = singleUnitWeight;
        this.palletWeight = palletWeight;
        this.singleUnitVolume = singleUnitVolume;
        this.palleteVolume = palleteVolume;
    }
}
