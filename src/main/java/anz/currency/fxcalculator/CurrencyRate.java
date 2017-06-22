package anz.currency.fxcalculator;

import java.util.Currency;

public class CurrencyRate implements CurrencyPair {
    private Currency base;
    private Currency terms;
    private double rate;
    private String key;

    public CurrencyRate(String baseCurrencyCode, String termsCurrencyCode, double rate) {
        this.base = Currency.getInstance(baseCurrencyCode);
        this.terms = Currency.getInstance(termsCurrencyCode);
        this.rate = rate;
        this.key = baseCurrencyCode + termsCurrencyCode;
    }

    public String getKey() {
        return this.key;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTerms() {
        return terms;
    }

    public double getRate() {
        return this.rate;
    }

    public double getAmount(double quantity) {
        return this.rate * quantity;
    }

}
