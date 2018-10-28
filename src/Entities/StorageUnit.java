package Entities;

import Entities.Article;

import java.math.BigDecimal;
import java.util.TreeMap;

public class StorageUnit {
    public String     code;
    public String     area;
    public Integer    capacity;
    public Integer    maxWeight;
    public BigDecimal maxVolume;

    public TreeMap<String, Article> articles;

    public StorageUnit(
            String code, String area, Integer capacity, Integer maxWeight,
            BigDecimal maxVolume) {
        this.code = code;
        this.area = area;
        this.capacity = capacity;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;

        articles = new TreeMap<>();
    }
}
