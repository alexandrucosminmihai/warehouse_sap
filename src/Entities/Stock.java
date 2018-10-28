package Entities;

public class Stock {
    String  articleCode;
    Integer stockAreaA;
    Integer stockAreaB;
    Integer stockAreaC;

    public Stock(
            String articleCode, Integer stockAreaA, Integer stockAreaB,
            Integer stockAreaC) {
        this.articleCode = articleCode;
        this.stockAreaA = stockAreaA;
        this.stockAreaB = stockAreaB;
        this.stockAreaC = stockAreaC;
    }
}
