package Entities;

import java.util.Date;

public class Delivery {
    public Date    time;
    public String  articleCode;
    public String  type;
    public Integer quantity;

    public Delivery(
            Date time, String articleCode, String type, Integer quantity) {
        this.time = time;
        this.articleCode = articleCode;
        this.type = type;
        this.quantity = quantity;
    }
}


