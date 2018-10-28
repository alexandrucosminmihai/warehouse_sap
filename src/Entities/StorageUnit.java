package Entities;

import java.math.BigDecimal;

public class StorageUnit {
    public String     code;
    public String     area;
    public Integer    capacity;
    public Integer    maxWeight;
    public BigDecimal maxVolume;

    public StorageUnit(
            String code, String area, Integer capacity, Integer maxWeight,
            BigDecimal maxVolume) {
        this.code = code;
        this.area = area;
        this.capacity = capacity;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;
    }
}
