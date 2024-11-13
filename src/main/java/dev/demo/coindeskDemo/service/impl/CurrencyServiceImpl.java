package dev.demo.coindeskDemo.service.impl;

import dev.demo.coindeskDemo.entity.Currency;
import dev.demo.coindeskDemo.repository.CurrencyRepository;
import dev.demo.coindeskDemo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public Optional<Currency> findById(String code) {
        return currencyRepository.findById(code);
    }

    @Override
    public void deleteById(String code) {
        currencyRepository.deleteById(code);
    }


}
