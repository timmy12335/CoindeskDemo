package dev.demo.coindeskDemo.CurrencyServiceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.demo.coindeskDemo.common.CoinDeskApiService;
import dev.demo.coindeskDemo.controller.CurrencyController;
import dev.demo.coindeskDemo.entity.Currency;
import dev.demo.coindeskDemo.model.coindesk.BpiCurrency;
import dev.demo.coindeskDemo.model.coindesk.CoinDeskApiResBean;
import dev.demo.coindeskDemo.model.coindesk.UpdateTime;
import dev.demo.coindeskDemo.model.currency.CurrencyReqBean;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfo;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfoResBean;
import dev.demo.coindeskDemo.service.CurrencyInfoService;
import dev.demo.coindeskDemo.service.CurrencyService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@CommonsLog
public class CurrencyServiceTest {

    @Mock
    private CoinDeskApiService coinDeskApiService;

    @Mock
    private CurrencyInfoService currencyInfoService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    ObjectMapper objectMapper = new ObjectMapper();

    // 1. 測試查詢幣別對應表資料 API，並顯示其內容
    @Test
    public void testGetAllCurrencies() throws JsonProcessingException {
        List<Currency> currencies = Arrays.asList(
                new Currency("USD", "美元"),
                new Currency("EUR", "歐元")
        );

        when(currencyService.getAll()).thenReturn(currencies);

        List<Currency> response = currencyController.getAllCurrencies();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("USD", response.get(0).getCode());
        assertEquals("美元", response.get(0).getName());

        log.info(objectMapper.writeValueAsString(response));
    }

    // 2. 測試新增幣別對應表資料 API
    @Test
    public void testAddCurrency() {
        Currency currency = new Currency("GBP", "英鎊");
        when(currencyService.saveCurrency(any(CurrencyReqBean.class))).thenReturn(currency);

        Currency response = currencyController.saveCurrency(new CurrencyReqBean("GBP", "英鎊"));

        assertNotNull(response);
        assertEquals("GBP", response.getCode());
        assertEquals("英鎊", response.getName());
    }

    // 3. 測試更新幣別對應表資料 API，並顯示其內容
    @Test
    public void testUpdateCurrency() throws JsonProcessingException {
        Currency existingCurrency = new Currency("USD", "美元");
        when(currencyService.findById("USD")).thenReturn(Optional.of(existingCurrency));

        Currency updatedCurrency = new Currency("USD", "美金");
        when(currencyService.saveCurrency(any(CurrencyReqBean.class))).thenReturn(updatedCurrency);

        Currency response = currencyController.updateCurrency(new CurrencyReqBean("USD", "美金"));

        assertNotNull(response);
        assertEquals("USD", response.getCode());
        assertEquals("美金", response.getName());

        log.info(objectMapper.writeValueAsString(response));
    }

    // 4. 測試刪除幣別對應表資料 API
    @Test
    public void testDeleteCurrency() {
        doNothing().when(currencyService).deleteById("USD");

        currencyController.deleteCurrency("USD");

        verify(currencyService, times(1)).deleteById("USD");
    }

    // 5. 測試呼叫 Coindesk API，並顯示其內容
    @Test
    public void testGetCoinDeskData() throws JsonProcessingException {
        CoinDeskApiResBean mockResponse = new CoinDeskApiResBean();
        UpdateTime time = new UpdateTime();
        time.setUpdated("Nov 12, 2024 14:29:42 UTC");
        time.setUpdatedISO("2024-11-12T14:29:42+00:00");
        time.setUpdateduk("Nov 12, 2024 at 14:29 GMT");

        BpiCurrency usd = new BpiCurrency();
        usd.setCode("USD");
        usd.setSymbol("&#36;");
        usd.setRate("85,620.589");
        usd.setDescription("United States Dollar");
        usd.setRateFloat(BigDecimal.valueOf(85620.5893));

        Map<String, BpiCurrency> bpi = new HashMap<>();
        bpi.put("USD", usd);

        mockResponse.setUpdateTime(time);
        mockResponse.setDisclaimer("Data from CoinDesk API");
        mockResponse.setChartName("Bitcoin");
        mockResponse.setBpi(bpi);

        when(coinDeskApiService.getCurrentPrice()).thenReturn(mockResponse);

        CoinDeskApiResBean response = coinDeskApiService.getCurrentPrice();

        assertNotNull(response);
        assertEquals("Bitcoin", response.getChartName());
        assertEquals("USD", response.getBpi().get("USD").getCode());

        log.info(objectMapper.writeValueAsString(response));
    }

    // 6. 測試呼叫資料轉換的 API，並顯示其內容
    @Test
    public void testGetTransformedData() throws JsonProcessingException {
        CurrencyInfoResBean currencyInfoResBean = new CurrencyInfoResBean();
        currencyInfoResBean.setUpdateTime("2024/11/12 08:48:50");
        CurrencyInfo currencyInfo = new CurrencyInfo();
        currencyInfo.setCode("USD");
        currencyInfo.setName("美元");
        currencyInfo.setRate(BigDecimal.valueOf(50000.0));

        currencyInfoResBean.setCurrencyInfo(Collections.singletonList(currencyInfo));

        when(currencyInfoService.getCurrencyInfo()).thenReturn(currencyInfoResBean);

        CurrencyInfoResBean response = currencyController.getCurrencyInfo();

        assertNotNull(response);
        assertEquals("2024/11/12 08:48:50", response.getUpdateTime());
        assertEquals("USD", response.getCurrencyInfo().get(0).getCode());
        assertEquals("美元", response.getCurrencyInfo().get(0).getName());
        assertEquals(BigDecimal.valueOf(50000.0), response.getCurrencyInfo().get(0).getRate());

        log.info(objectMapper.writeValueAsString(response));
    }
}
