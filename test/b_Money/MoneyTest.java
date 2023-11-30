package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(100, SEK100.getAmount(), 0.001);
		assertEquals(10, EUR10.getAmount(), 0.001);
		assertEquals(200, SEK200.getAmount(),0.001);
		assertEquals(20, EUR20.getAmount(), 0.001);
		assertEquals(0, SEK0.getAmount(), 0.001);
		assertEquals(0, EUR0.getAmount(), 0.001);
		assertEquals(-100, SEKn100.getAmount(), 0.001);
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
		assertEquals(SEK, SEK200.getCurrency());
		assertEquals(EUR, EUR20.getCurrency());
		assertEquals(SEK, SEK0.getCurrency());
		assertEquals(EUR, EUR0.getCurrency());
		assertEquals(SEK, SEKn100.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("10.0 EUR", EUR10.toString());
		assertEquals("200.0 SEK", SEK200.toString());
		assertEquals("20.0 EUR", EUR20.toString());
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("0.0 EUR", EUR0.toString());
		assertEquals("-100.0 SEK", SEKn100.toString());
	}

	/*
	* I renamed the test method from testGlobalValue to test UniversalValue for consistency with the rest of the code
	* */
	@Test
	public void testUniversalValue() {
		assertEquals(1500, (int)SEK100.universalValue());
		assertEquals(1500, (int)EUR10.universalValue());
		assertEquals(3000, (int)SEK200.universalValue());
		assertEquals(3000, (int)EUR20.universalValue());
		assertEquals(0, (int)SEK0.universalValue());
		assertEquals(0, (int)EUR0.universalValue());
		assertEquals(-1500, (int)SEKn100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertEquals(true, SEK100.equals(EUR10));
		assertEquals(false, EUR10.equals(EUR20));
		assertEquals(true, SEK200.equals(EUR20));
		assertEquals(false, EUR20.equals(SEK0));
		assertEquals(true, SEK0.equals(EUR0));
		assertEquals(true, EUR0.equals(SEK0));
		assertEquals(false, SEKn100.equals(SEK100));
	}

	@Test
	public void testAdd() {
		assertEquals(SEK100, SEK100.add(SEK0));
		assertEquals(EUR20, EUR10.add(SEK100));
		assertEquals(SEK200, SEK200.add(SEK0));
		assertEquals(EUR20, EUR20.add(SEK0));
		assertEquals(SEK0, SEK0.add(EUR0));
		assertEquals(EUR0, EUR0.add(new Money(0,DKK)));
		assertEquals(SEKn100, SEKn100.add(SEK0));
	}

	@Test
	public void testSub() {
		assertEquals(SEK0, SEK100.sub(EUR10));
		assertEquals(EUR0, EUR10.sub(EUR10));
		assertEquals(SEK100, SEK200.sub(EUR10));
		assertEquals(EUR10, EUR20.sub(EUR10));
		assertEquals(SEK0, SEK0.sub(SEK0));
		assertEquals(EUR0, EUR0.sub(EUR0));
		assertEquals(SEKn100, SEKn100.sub(SEK0));
	}

	@Test
	public void testIsZero() {
		assertEquals(false, SEK100.isZero());
		assertEquals(false, EUR10.isZero());
		assertEquals(false, SEK200.isZero());
		assertEquals(false, EUR20.isZero());
		assertEquals(true, SEK0.isZero());
		assertEquals(true, EUR0.isZero());
		assertEquals(false, SEKn100.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(SEKn100, SEK100.negate());
		assertEquals(SEK0, SEK0.negate());
		assertEquals(EUR0, EUR0.negate());
		assertEquals(SEK100, SEKn100.negate());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, SEK100.compareTo(EUR10));
		assertTrue(EUR10.compareTo(EUR20) < 0);
		assertTrue(SEK200.compareTo(SEK0) > 0);
		assertEquals(0, EUR20.compareTo(SEK200));
		assertEquals(0, SEK0.compareTo(EUR0));
		assertTrue(EUR0.compareTo(SEKn100) > 0);
		assertTrue(SEKn100.compareTo(SEK0) < 0);
	}
}
