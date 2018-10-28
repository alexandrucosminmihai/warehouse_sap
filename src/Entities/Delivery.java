package Entities;

import java.util.Date;

public class Delivery {
    public Date    time;
    public String  articleCode;
    public String  type;
    public Integer quantitySingleUnits;

    public Delivery(
            Date time, String articleCode, String type,
            Integer quantitySingleUnits) {
        this.time = time;
        this.articleCode = articleCode;
        this.type = type;
        this.quantitySingleUnits = quantitySingleUnits;
    }
}


