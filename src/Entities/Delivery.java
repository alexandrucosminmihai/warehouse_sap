package Entities;

import java.util.Date;

public class Delivery {
    private enum DeliveryType {IN, OUT};

    Date time;
    double articleCode;
    DeliveryType type;
    double quantity;
}


