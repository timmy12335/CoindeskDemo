package dev.demo.coindeskDemo.model.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CoinDeskApiResBean {

    @JsonProperty("time")
    private UpdateTime updateTime;

    @JsonProperty("disclaimer")
    private String disclaimer;

    @JsonProperty("chartName")
    private String chartName;

    @JsonProperty("bpi")
    private Map<String, BpiCurrency> bpi;
}
