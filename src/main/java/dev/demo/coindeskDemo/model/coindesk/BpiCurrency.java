package dev.demo.coindeskDemo.model.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BpiCurrency {

    private String code;

    private String symbol;

    private String rate;

    private String description;

    @JsonProperty("rate_float")
    private BigDecimal rateFloat;
}
