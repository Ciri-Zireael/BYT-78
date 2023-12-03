package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	/*
	* I added some exceptions to the method signature.
	* */
	@Test
	public void testAddRemoveTimedPayment() throws TimedPaymentDoesNotExistException, TimedPaymentExistsException {
		/* Check if there is no TimedPayment called "test tp" on the testAccount */
		assertFalse(testAccount.timedPaymentExists("test tp"));
		/* Add TimedPayment called "test tp" to the testAccount */
		testAccount.addTimedPayment("test tp", 2, 1, new Money(1000, SEK), SweBank, "Alice");
		/* Check if there is a TimedPayment called "test tp" on the testAccount */
		assertTrue(testAccount.timedPaymentExists("test tp"));
		/* Remove TimedPayment called "test tp" of the testAccount */
		testAccount.removeTimedPayment("test tp");
		/* Check if there is no TimedPayment called "test tp" of the testAccount */
		assertFalse(testAccount.timedPaymentExists("test tp"));
	}

	/*
	* Had to add extra exceptions to method signature
	* */
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, NegativeAmountOfMoneyException, TimedPaymentExistsException {
		/* Add a TimePayment called "test tp" to the testAccount */
		testAccount.addTimedPayment("test tp", 2, 1, new Money(1000, SEK), SweBank, "Alice");
		/* Check if the initial balance on both accounts is correct */
		assertEquals(100000, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10000, SweBank.getBalance("Alice"), 0.001);
		/* Perform a tick */
		testAccount.tick();
		/* Check if the balance on both accounts is correct */
		assertEquals(100000, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10000, SweBank.getBalance("Alice"), 0.001);
		/* Perform a tick */
		testAccount.tick();
		/* Check if the balance on both accounts is correct */
		assertEquals(99990, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10010, SweBank.getBalance("Alice"), 0.001);
		/* Perform a tick */
		testAccount.tick();
		/* Check if the balance on both accounts is correct */
		assertEquals(99990, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10010, SweBank.getBalance("Alice"), 0.001);
		/* Perform a tick */
		testAccount.tick();
		/* Check if the balance on both accounts is correct */
		assertEquals(99990, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10010, SweBank.getBalance("Alice"), 0.001);
		/* Perform a tick */
		testAccount.tick();
		/* Check if the balance on both accounts is correct */
		assertEquals(99980, testAccount.getBalance().getAmount(), 0.001);
		assertEquals(10020, SweBank.getBalance("Alice"), 0.001);
	}

	@Test
	public void testAddWithdraw() {
		/* Check if the initial balance is correct */
		assertEquals(100000, testAccount.getBalance().getAmount(), 0.001);
		/* Perform a tick */
		testAccount.withdraw(new Money(100000, SEK));
		/* Check if the balance on both accounts is correct */
		assertEquals(99000, testAccount.getBalance().getAmount(), 0.001);
	}
	
	@Test
	public void testGetBalance() {
		/* Check if you get the correct balance */
		assertEquals(new Money(10000000, SEK), testAccount.getBalance());
		try {
			assertEquals(new Money(1000000, SEK), SweBank.getAccount("Alice").getBalance());
		} catch (AccountDoesNotExistException e) {
			throw new RuntimeException(e);
		}
	}
}
