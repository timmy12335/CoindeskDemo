package dev.demo.coindeskDemo.common;

import dev.demo.coindeskDemo.model.coindesk.CoinDeskApiResBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CoinBaseApiService", url = "https://api.coindesk.com")
public interface CoinDeskApiService {

    @GetMapping("/v1/bpi/currentprice.json")
    CoinDeskApiResBean getCurrentPrice();
}
