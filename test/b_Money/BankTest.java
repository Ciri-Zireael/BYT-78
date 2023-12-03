package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	Money SEK100, SEK0, SEKn100;

	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);

		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);

		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");

		SEK100 = new Money(10000, SEK);
		SEK0 = new Money(0, SEK);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetName() {
		/* Check if you get the correct names of the Banks */
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		/* Check if you get the correct currencies of the Banks */
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		/* Check if the accounts that has been opened in the setUp exist in the corresponding banks */
		assertTrue(SweBank.hasAccount("Ulrika"));
		assertTrue(SweBank.hasAccount("Bob"));
		assertTrue(Nordea.hasAccount("Bob"));
		assertTrue(DanskeBank.hasAccount("Gertrud"));
	}

	/*
	 * Had to add a new exception to the method signature.
	 * */
	@Test
	public void testDeposit() throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		/* Check if the amount of money is correct before and after deposit */
		assertEquals(0, Nordea.getBalance("Bob"), 0.001);
		Nordea.deposit("Bob", SEK100);
		assertEquals(100, Nordea.getBalance("Bob"), 0.001);
	}

	/*
	* Had to add a new exception to the method signature.
	* */
	@Test
	public void testWithdraw() throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		/* Check if the amount of money is correct before and after withdrawal */
		assertEquals(0, Nordea.getBalance("Bob"), 0.001);
		Nordea.withdraw("Bob", SEK100);
		assertEquals(-100, Nordea.getBalance("Bob"), 0.001);
	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		/* Check if you get the correct balance */
		assertEquals(0, Nordea.getBalance("Bob"), 0.001);
	}

	/*
	* I added a new exception (NegativeAmountOfMoneyException) to the method signature.
	* */
	@Test
	public void testTransfer() throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		/* Check if the amount of money is correct before and after transfer  on both accounts */
		Nordea.deposit("Bob", SEK100);		// setting up money that will later be transferred
		assertEquals(100, Nordea.getBalance("Bob"), 0.001);
		assertEquals(0, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.transfer("Bob", SweBank, "Ulrika",SEK100);
		assertEquals(0, Nordea.getBalance("Bob"), 0.001);
		assertEquals(100, SweBank.getBalance("Ulrika"), 0.001);
	}

	/*
	 * I added new exceptions to the method signature.
	 * */
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, NegativeAmountOfMoneyException, TimedPaymentExistsException, TimedPaymentDoesNotExistException {
		/* Check if the timed payment works as intended */

		/* Set up */
		Money SEK10 = new Money(1000, SEK);
		Nordea.deposit("Bob", SEK100);
		Nordea.addTimedPayment("Bob", "test tp", 1, 2, SEK10, SweBank, "Ulrika");

		/* Initial checks */
		assertEquals(100, Nordea.getBalance("Bob"), 0.001);
		assertEquals(0, SweBank.getBalance("Ulrika"), 0.001);

		/* Checking if the procedure works */
		Nordea.tick();
		assertEquals(100, Nordea.getBalance("Bob"), 0.001);
		assertEquals(0, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(100, Nordea.getBalance("Bob"), 0.001);
		assertEquals(0, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(90, Nordea.getBalance("Bob"), 0.001);
		assertEquals(10, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(90, Nordea.getBalance("Bob"), 0.001);
		assertEquals(10, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(80, Nordea.getBalance("Bob"), 0.001);
		assertEquals(20, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(80, Nordea.getBalance("Bob"), 0.001);
		assertEquals(20, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(70, Nordea.getBalance("Bob"), 0.001);
		assertEquals(30, SweBank.getBalance("Ulrika"), 0.001);

		/* Canceling timed payment */
		Nordea.removeTimedPayment("Bob", "test tp");

		/* Checking to make sure the payment got cancelled */
		Nordea.tick();
		assertEquals(70, Nordea.getBalance("Bob"), 0.001);
		assertEquals(30, SweBank.getBalance("Ulrika"), 0.001);
		Nordea.tick();
		assertEquals(70, Nordea.getBalance("Bob"), 0.001);
		assertEquals(30, SweBank.getBalance("Ulrika"), 0.001);
	}
}
