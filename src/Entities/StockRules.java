package Entities;

public class StockRules {
    public String  articleCode;
    public String  storageAreaCode;
    public Integer minQuantity;
    public Integer maxQuantity;
    public Integer maxCapacity;

    public StockRules(
            String articleCode, String storageAreaCode, Integer minQuantity,
            Integer maxQuantity, Integer maxCapacity) {
        this.articleCode = articleCode;
        this.storageAreaCode = storageAreaCode;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.maxCapacity = maxCapacity;
    }
}
