package dev.demo.coindeskDemo.service.impl;

import dev.demo.coindeskDemo.common.CoinDeskApiService;
import dev.demo.coindeskDemo.entity.Currency;
import dev.demo.coindeskDemo.model.coindesk.CoinDeskApiResBean;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfo;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfoResBean;
import dev.demo.coindeskDemo.repository.CurrencyRepository;
import dev.demo.coindeskDemo.service.CurrencyInfoService;
import dev.demo.coindeskDemo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyInfoServiceImpl implements CurrencyInfoService {

    @Autowired
    private CoinDeskApiService coinDeskApiService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public CurrencyInfoResBean getCurrencyInfo() {
        CoinDeskApiResBean coinDeskApiResBean = coinDeskApiService.getCurrentPrice();
        Map<String, String> coinMap = getCurrencyName();

        return transformCurrencyInfoResBean(coinDeskApiResBean, coinMap);
    }

    private CurrencyInfoResBean transformCurrencyInfoResBean(CoinDeskApiResBean coinDeskApiResBean, Map<String, String> coinMap){
        CurrencyInfoResBean currencyInfoResBean = new CurrencyInfoResBean();

        List<CurrencyInfo> currencyInfoList = coinDeskApiResBean.getBpi().values().stream()
                .map(currency -> {
                    CurrencyInfo currencyInfo = new CurrencyInfo();
                    currencyInfo.setCode(currency.getCode());
                    currencyInfo.setName(coinMap.get(currency.getCode()));
                    currencyInfo.setRate(currency.getRateFloat());
                    return currencyInfo;
                })
                .toList();

        currencyInfoResBean.setUpdateTime(DateUtils.formatTime(coinDeskApiResBean.getUpdateTime().getUpdatedISO()));
        currencyInfoResBean.setCurrencyInfo(currencyInfoList);

        return currencyInfoResBean;
    }
    private Map<String, String> getCurrencyName(){
        List<Currency> currency = currencyRepository.findAll();
        return currency.stream().collect(Collectors.toMap(Currency::getCode, Currency::getName));
    }

}
