package dev.demo.coindeskDemo.model.currencyInfo;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyInfoResBean {

    private String updateTime;

    private List<CurrencyInfo> currencyInfo;

}
