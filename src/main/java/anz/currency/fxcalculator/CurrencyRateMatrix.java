package anz.currency.fxcalculator;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class CurrencyRateMatrix {

    private Map<String, CurrencyPair> currencyPairMap;

    public CurrencyRateMatrix(Map<String, CurrencyPair> currencyPairMap) {
       this.currencyPairMap = currencyPairMap;
    }

    public BigDecimal getSwappedAmount(String base, String terms, double quantity) {
        try {
            String currencyKey = base + terms;

            // unity, rate is always 1
            if (base.equalsIgnoreCase(terms)) {
                return getBigDecimalAmount(quantity, terms);
            }

            // check for direct match
            if (currencyPairMap.containsKey(currencyKey)) {
                CurrencyPair pair = currencyPairMap.get(currencyKey);
                return getBigDecimalAmount(pair.getAmount(quantity), terms);
            }

            // need to cross via either USD or EUR
            if (canConvertToUSD(base) && canConvertToUSD(terms)) {
                // X > USD > Y
                Double amt = convert(base, quantity, "USD");
                amt = convert("USD", amt, terms);
                return getBigDecimalAmount(amt, terms);
            } else if (canConvertToEUR(base) && canConvertToEUR(terms)) {
                // X > EUR > Y
                Double amt = convert(base, quantity, "EUR");
                amt = convert("EUR", amt, terms);
                return getBigDecimalAmount(amt, terms);
            } else if (canConvertToUSD(base)) {
                // X > USD > EUR > Y
                Double amt = convert(base, quantity, "USD");
                amt = convert("USD", amt, "EUR");
                amt = convert("EUR", amt, terms);
                return getBigDecimalAmount(amt, terms);
            } else if (canConvertToEUR(base)) {
                // X > EUR > USD > Y
                Double amt = convert(base, quantity, "EUR");
                amt = convert("EUR", amt, "USD");
                amt = convert("USD", amt, terms);
                return getBigDecimalAmount(amt, terms);
            }

            return null;
        } catch(Exception ex) {
            return null; // no matching currency found. possibly bad input
        }

    }

    private Double convert(String base, double amount, String terms) {
        CurrencyPair pair = currencyPairMap.get(base + terms);
        if(pair != null) {
            return pair.getAmount(amount);
        }
        return null;
    }

    private boolean canConvertToUSD(String currency) {
        String terms = "USD";
        return currencyPairMap.get(currency + terms)!= null;
    }

    private boolean canConvertToEUR(String currency) {
        String terms = "EUR";
        return currencyPairMap.get(currency + terms)!= null;
    }

    private BigDecimal getBigDecimalAmount(double amount, String currencyCode) {
        BigDecimal dec = new BigDecimal(amount);
        Currency currency = Currency.getInstance(currencyCode);
        return dec.setScale(currency.getDefaultFractionDigits(), BigDecimal.ROUND_CEILING);
    }
}
