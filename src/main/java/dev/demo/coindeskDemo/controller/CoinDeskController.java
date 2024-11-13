package dev.demo.coindeskDemo.controller;

import dev.demo.coindeskDemo.common.CoinDeskApiService;
import dev.demo.coindeskDemo.model.coindesk.CoinDeskApiResBean;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/coinDesk")
public class CoinDeskController {

    @Autowired
    private CoinDeskApiService coinDeskApiService;

    @GetMapping
    @Operation(summary = "取得coinDesk Data")
    public CoinDeskApiResBean getCoinDeskData(){
        return coinDeskApiService.getCurrentPrice();
    }
}
