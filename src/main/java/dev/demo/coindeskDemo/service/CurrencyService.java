package dev.demo.coindeskDemo.service;

import dev.demo.coindeskDemo.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    public List<Currency> getAll();

    public Currency saveCurrency(Currency currency);

    public Optional<Currency> findById(String code);

    public void deleteById(String code);
}
