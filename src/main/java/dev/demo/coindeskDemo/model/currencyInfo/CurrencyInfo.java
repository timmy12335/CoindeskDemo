package dev.demo.coindeskDemo.model.currencyInfo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyInfo {

    private String code;

    private String name;

    private BigDecimal rate;
}
