package com.github.tomaszgryczka.mwotests2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String id;
    private String name;
    private double price;
    private boolean isAvailable;
}
