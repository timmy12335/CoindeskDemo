package dev.demo.coindeskDemo.CurrencyServiceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.demo.coindeskDemo.common.CoinDeskApiService;
import dev.demo.coindeskDemo.controller.CurrencyController;
import dev.demo.coindeskDemo.entity.Currency;
import dev.demo.coindeskDemo.model.coindesk.BpiCurrency;
import dev.demo.coindeskDemo.model.coindesk.CoinDeskApiResBean;
import dev.demo.coindeskDemo.model.coindesk.UpdateTime;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfo;
import dev.demo.coindeskDemo.model.currencyInfo.CurrencyInfoResBean;
import dev.demo.coindeskDemo.repository.CurrencyRepository;
import dev.demo.coindeskDemo.service.CurrencyInfoService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@CommonsLog
public class CurrencyServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private CoinDeskApiService coinDeskApiService;

    @MockBean
    private CurrencyInfoService currencyInfoService;

    @InjectMocks
    private CurrencyController currencyController;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. 測試查詢幣別對應表資料 API，並顯示其內容
    @Test
    public void testGetAllCurrencies() throws Exception {
        List<Currency> currencies = Arrays.asList(
                new Currency("USD", "美元"),
                new Currency("EUR", "歐元")
        );

        when(currencyRepository.findAll()).thenReturn(currencies);

        MvcResult result = mockMvc.perform(get("/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].name").value("美元"))
                .andExpect(jsonPath("$[1].code").value("EUR"))
                .andExpect(jsonPath("$[1].name").value("歐元"))
                .andReturn();

        log.info(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    // 2. 測試新增幣別對應表資料 API
    @Test
    public void testAddCurrency() throws Exception {
        Currency currency = new Currency("GBP", "英鎊");
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        mockMvc.perform(post("/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"GBP\", \"name\": \"英鎊\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("GBP"))
                .andExpect(jsonPath("$.name").value("英鎊"));
    }

    // 3. 測試更新幣別對應表資料 API，並顯示其內容
    @Test
    public void testUpdateCurrency() throws Exception {
        Currency existingCurrency = new Currency("USD", "美元");
        when(currencyRepository.findById("USD")).thenReturn(Optional.of(existingCurrency));


        Currency updatedCurrency = new Currency("USD", "美金");
        when(currencyRepository.save(any(Currency.class))).thenReturn(updatedCurrency);

        MvcResult result = mockMvc.perform(put("/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"USD\", \"name\": \"美金\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("美金"))
                .andReturn();

        log.info(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    // 4. 測試刪除幣別對應表資料 API
    @Test
    public void testDeleteCurrency() throws Exception {
        doNothing().when(currencyRepository).deleteById("USD");

        mockMvc.perform(delete("/currency/USD"))
                .andExpect(status().isOk());

        verify(currencyRepository, times(1)).deleteById("USD");
    }

    // 5. 測試呼叫 Coindesk API，並顯示其內容
    @Test
    public void testGetCoinDeskData() throws Exception {
        CoinDeskApiResBean mockResponse = new CoinDeskApiResBean();
        // 設置時間信息
        UpdateTime time = new UpdateTime();
        time.setUpdated("Nov 12, 2024 14:29:42 UTC");
        time.setUpdatedISO("2024-11-12T14:29:42+00:00");
        time.setUpdateduk("Nov 12, 2024 at 14:29 GMT");

        // 設置幣別信息
        BpiCurrency usd = new BpiCurrency();
        usd.setCode("USD");
        usd.setSymbol("&#36;");
        usd.setRate("85,620.589");
        usd.setDescription("United States Dollar");
        usd.setRateFloat(BigDecimal.valueOf(85620.5893));

        BpiCurrency gbp = new BpiCurrency();
        gbp.setCode("GBP");
        gbp.setSymbol("&pound;");
        gbp.setRate("66,836.031");
        gbp.setDescription("British Pound Sterling");
        gbp.setRateFloat(BigDecimal.valueOf(66836.0314));

        BpiCurrency eur = new BpiCurrency();
        eur.setCode("EUR");
        eur.setSymbol("&euro;");
        eur.setRate("80,600.483");
        eur.setDescription("Euro");
        eur.setRateFloat(BigDecimal.valueOf(80600.4829));

        // 將幣別信息放入 Map
        Map<String, BpiCurrency> bpi = new HashMap<>();
        bpi.put("USD", usd);
        bpi.put("GBP", gbp);
        bpi.put("EUR", eur);

        // 組裝 mockResponse
        mockResponse.setUpdateTime(time);
        mockResponse.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org");
        mockResponse.setChartName("Bitcoin");
        mockResponse.setBpi(bpi);

        when(coinDeskApiService.getCurrentPrice()).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(get("/coinDesk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)))
                .andReturn();

        log.info(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    // 6. 測試呼叫資料轉換的 API，並顯示其內容
    @Test
    public void testGetTransformedData() throws Exception {
        CurrencyInfoResBean currencyInfoResBean = new CurrencyInfoResBean();
        currencyInfoResBean.setUpdateTime("2024/11/12 08:48:50");
        CurrencyInfo currencyInfo = new CurrencyInfo();
        currencyInfo.setCode("USD");
        currencyInfo.setName("美元");
        currencyInfo.setRate(BigDecimal.valueOf(50000.0));
        currencyInfoResBean.setCurrencyInfo(List.of(currencyInfo));

        when(currencyInfoService.getCurrencyInfo()).thenReturn(currencyInfoResBean);

        MvcResult result = mockMvc.perform(get("/currency/info")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.updateTime").value("2024/11/12 08:48:50"))
                        .andExpect(jsonPath("$.currencyInfo[0].code").value("USD"))
                        .andExpect(jsonPath("$.currencyInfo[0].name").value("美元"))
                        .andExpect(jsonPath("$.currencyInfo[0].rate").value("50000.0"))
                        .andReturn();

        log.info(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
