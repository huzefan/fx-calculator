package anz.currency.fxcalculator;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyUtils {
    public static Map<String,CurrencyPair>loadRatesFromString(String rates) {
        List<CurrencyPair> pairs = Arrays.asList(rates.split(","))
                .stream()
                .map(CurrencyUtils::getCurrencyRate)
                .collect(Collectors.toList());

        pairs.addAll(pairs.stream()
                .map(CurrencyUtils::getSwapRate)
                .collect(Collectors.toList()));

        Map<String,CurrencyPair> map = new HashMap<>();
        pairs.forEach(pair -> map.put(pair.getKey(), pair));

        return map;
    }

    private static CurrencyRate getCurrencyRate(String rates) {
        return new CurrencyRate(rates.substring(0, 3),
                rates.substring(3, 6),
                Double.valueOf(rates.substring(6)));
    }

    private static CurrencyRate getSwapRate(CurrencyPair pair) {
        return new CurrencyRate(pair.getTerms().getCurrencyCode(),
                pair.getBase().getCurrencyCode(),
                1.0/pair.getRate());
    }
}
