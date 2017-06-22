package anz.currency.fxcalculator;

import java.util.Currency;

public interface CurrencyPair {
    Currency getBase();
    Currency getTerms();
    double getRate();
    String getKey();
    double getAmount(double quantity);
}
