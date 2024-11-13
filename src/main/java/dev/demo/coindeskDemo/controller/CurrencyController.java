package dev.demo.coindeskDemo.controller;

import dev.demo.coindeskDemo.entity.Currency;
import dev.demo.coindeskDemo.exception.CurrencyException;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfoResBean;
import dev.demo.coindeskDemo.service.CurrencyInfoService;
import dev.demo.coindeskDemo.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyInfoService currencyInfoService;

    @GetMapping
    @Operation(summary = "查詢全部幣別")
    public List<Currency> getAllCurrencies(){
        return currencyService.getAll();
    }

    @PostMapping
    @Operation(summary = "新增幣別")
    public Currency saveCurrency(@RequestBody Currency currency){
        if(currencyService.findById(currency.getCode()).isPresent()){
            throw new CurrencyException("已存在對應幣別");
        }
        return currencyService.saveCurrency(currency);
    }

    @PutMapping
    @Operation(summary = "更新幣別")
    public Currency updateCurrency(@RequestBody Currency currency){
        if(currencyService.findById(currency.getCode()).isEmpty()){
            throw new CurrencyException("沒有對應幣別");
        }
        return currencyService.saveCurrency(currency);
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "移除幣別")
    public String deleteCurrency(@PathVariable String code){
        currencyService.deleteById(code);
        return "已刪除";
    }

    @GetMapping("/info")
    @Operation(summary = "取得幣別更新資訊")
    public CurrencyInfoResBean getCurrencyInfo(){
        return currencyInfoService.getCurrencyInfo();
    }
}
