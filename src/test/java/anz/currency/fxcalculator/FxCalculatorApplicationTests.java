package anz.currency.fxcalculator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FxCalculatorApplicationTests {

	String currencyRates = "AUDUSD0.8371,CADUSD0.8711,USDCNY6.1715,EURUSD1.2315,GBPUSD1.5683,NZDUSD0.7750,USDJPY119.95,EURCZK27.6028,EURDKK7.4405,EURNOK8.6651";
	CurrencyRateMatrix matrix;

	@Before
	public void contextLoads() {
		matrix = new CurrencyRateMatrix(CurrencyUtils
				.loadRatesFromString(currencyRates));
	}

	@Test
	public void test() {
		String base = "USD";
		String terms = "USD";
		BigDecimal expected = getCurrencyAmount(terms, 1);
		BigDecimal actual = convert(base, terms, 1);
		assertEquals(base + "to " + terms, expected , actual);

		base = "AUD";
		terms = "USD";
		expected = getCurrencyAmount(terms, 0.8371);
		actual = convert(base, terms, 1);
		assertEquals(base + "to " + terms, expected , actual);

		base = "AUD";
		terms = "JPY";
		expected = getCurrencyAmount(terms, 101);
		actual = convert(base, terms, 1);
		assertEquals(base + "to " + terms, expected , actual);

		base = "NOK";
		terms = "CNY";
		expected = getCurrencyAmount(terms, 87.72);
		actual = convert(base, terms, 100);
		assertEquals(base + "to " + terms, expected , actual);


	}

	private BigDecimal convert(String base, String terms, double amount) {
		return matrix.getSwappedAmount(base, terms, amount);
	}

	private BigDecimal getCurrencyAmount(String currencyCode, double amount) {
		Currency currency = Currency.getInstance(currencyCode);
		BigDecimal dec = new BigDecimal(amount);
		return dec.setScale(currency.getDefaultFractionDigits(), BigDecimal.ROUND_CEILING);
	}

}
