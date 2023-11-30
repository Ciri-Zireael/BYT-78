package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;

	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.30);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}

	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.001);
		assertEquals(0.30, DKK.getRate(), 0.001);
		assertEquals(1.5, EUR.getRate(), 0.001);
	}

	@Test
	public void testSetRate() {
		DKK.setRate(0.2);
		assertEquals(0.2, DKK.getRate(), 0.001);
	}

	/*
	 * I renamed the test method from testGlobalValue to test UniversalValue for consistency with the rest of the code
	 * */
	@Test
	public void testUniversalValue() {
		assertEquals(150, SEK.universalValue(1000), 0.001);
		assertEquals(300, DKK.universalValue(1000), 0.001);
		assertEquals(1500, EUR.universalValue(1000), 0.001);
	}

	@Test
	public void testValueInThisCurrency() {
		assertEquals(1000, SEK.valueInThisCurrency(100, EUR), 0.001);
		assertEquals(50, DKK.valueInThisCurrency(100, SEK), 0.001);
		assertEquals(10, EUR.valueInThisCurrency(100, SEK), 0.001);
	}

}
