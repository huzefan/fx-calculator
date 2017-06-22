package anz.currency.fxcalculator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@Component
public class FxCalculatorApplication {
	@Value("${currencyrates}")
	String currencyRates;

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(FxCalculatorApplication.class, args);
		CurrencyRateMatrix matrix = app.getBean(CurrencyRateMatrix.class);

		if(!validateInput(args)) {
			System.out.println("CURRENCY CONVERTER usage: <ccy1> <amount> in <ccy2>");
			return;
		}

		BigDecimal swappedAmount = matrix.getSwappedAmount(args[0], args[3], Double.valueOf(args[1]));
		if(swappedAmount != null) {
			System.out.println(String.format("\n%s %s in %s is %s", args[0], args[1], args[3], swappedAmount));
		} else {
			System.out.println("Cannot convert. Input Currency not found");
		}

	}

	private static boolean validateInput(String[] args) {
		// Usage: <ccy1> <amount> in <ccy2>
		if(args.length !=4) {
			System.out.println("\nPlease enter valid arguments");
			return false;
		}

		if(!NumberUtils.isNumber(args[1])) {
			System.out.println("\nInvalid currency amount");
			return false;
		}

		return true;
	}

	@Bean
	public CurrencyRateMatrix getCurrencyRateMatrix() {
		return new CurrencyRateMatrix(CurrencyUtils
				.loadRatesFromString(currencyRates));
	}

}
