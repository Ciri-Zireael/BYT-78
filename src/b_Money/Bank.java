package b_Money;

import java.util.Hashtable;

public class Bank {
	private Hashtable<String, Account> accountlist = new Hashtable<String, Account>();
	private String name;
	private Currency currency;
	
	/**
	 * New Bank
	 * @param name Name of this bank
	 * @param currency Base currency of this bank (If this is a Swedish bank, this might be a currency class representing SEK)
	 */
	Bank(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}
	
	/**
	 * Get the name of this bank
	 * @return Name of this bank
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the Currency of this bank
	 * @return The Currency of this bank
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Open an account at this bank.
	 * @param accountid The ID of the account
	 * @throws AccountExistsException If the account already exists
	 */
	public void openAccount(String accountid) throws AccountExistsException {
		if (accountlist.containsKey(accountid)) {
			throw new AccountExistsException();
		}
		else {
			/*
			* This (code that was here) does not add a new account to the list:
			accountlist.get(accountid);
			* The code added below does.
			*/
			accountlist.put(accountid, new Account(accountid, this.currency));
		}
	}

	/*
	* I added a method (along with the documentation) for checking if the account with given ID exists within the Bank
	* */
	/**
	 * Check if an account with provided id exists in this Bank
	 * @param accountId The ID of the account
	 * @return Boolean indicating whether the account with given ID exists
	 * */
	public boolean hasAccount(String accountId) {
		return this.accountlist.containsKey(accountId);
	}

	/*
	 * I added a method (along with the documentation) for getting the account with given ID from the Bank
	 * */
	/**
	 * Get an account with provided id from this Bank (if it exists)
	 * @param accountId The ID of the account
	 * @throws AccountDoesNotExistException
	 * @return The account matching the criteria
	 * */
	public Account getAccount(String accountId) throws AccountDoesNotExistException{
		if (!this.hasAccount(accountId))
			throw new AccountDoesNotExistException();
		return accountlist.get(accountId);
	}
	
	/**
	 * Deposit money to an account
	 * @param accountid Account to deposit to
	 * @param money Money to deposit.
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	/*
	 * I added a check that would prevent someone from trying to deposit a negative amount of Money.
	 * */
	public void deposit(String accountid, Money money) throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		if (money.getAmount() < 0)
			throw new NegativeAmountOfMoneyException();
		/*
		* I negated the condition for checking if the account is on the list since we're throwing an exception
		* for when the account does not exist
		* */
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			account.deposit(money);
		}
	}
	
	/**
	 * Withdraw money from an account
	 * @param accountid Account to withdraw from
	 * @param money Money to withdraw
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	/*
	* I added a check that would prevent someone from trying to withdraw a negative amount of Money.
	* */
	public void withdraw(String accountid, Money money) throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		if (money.getAmount() < 0)
			throw new NegativeAmountOfMoneyException();
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			/*
			* This method is for withdrawing and not depositing, so I changed the method below from deposit wo withdraw
			* */
			account.withdraw(money);
		}
	}
	
	/**
	 * Get the balance of an account
	 * @param accountid Account to get balance from
	 * @return Balance of the account
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	/*
	* Here I also changed the return type to Double so that it stays consistent with the return type of getAmount in Money
	* */
	public Double getBalance(String accountid) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			return accountlist.get(accountid).getBalance().getAmount();
		}
	}

	/**
	 * Transfer money between two accounts
	 * @param fromaccount ID of account to deduct from in this Bank
	 * @param tobank Bank where receiving account resides
	 * @param toaccount ID of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	/*
	* I realized that with the current implementation, nothing would be stopping someone from transferring the negative
	* amount of money which probably should be stopped, so I took it upon myself to add some mechanisms
	* in order to prevent that.
	* */
	public void transfer(String fromaccount, Bank tobank, String toaccount, Money amount) throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		if (amount.getAmount() < 0)
			throw new NegativeAmountOfMoneyException();
		/*
		 * I just rewrote the line below to use the hasAccount method I added for better readability
		 * */
		if (!this.hasAccount(fromaccount) || !tobank.hasAccount(toaccount)) {
			throw new AccountDoesNotExistException();
		}
		else {
			accountlist.get(fromaccount).withdraw(amount);
			tobank.accountlist.get(toaccount).deposit(amount);
		}		
	}

	/**
	 * Transfer money between two accounts on the same bank
	 * @param fromaccount ID of account to deduct from
	 * @param toaccount ID of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	/*
	* Here I added the new exception to the method signature after the changes I've introduced in the method above.
	* */
	public void transfer(String fromaccount, String toaccount, Money amount) throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		transfer(fromaccount, this, fromaccount, amount);
	}

	/**
	 * Add a timed payment
	 * @param accountid ID of account to deduct from in this Bank
	 * @param payid ID of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount ID of receiving account
	 */
	public void addTimedPayment(String accountid, String payid, Integer interval, Integer next, Money amount, Bank tobank, String toaccount) throws TimedPaymentExistsException {
		Account account = accountlist.get(accountid);
		account.addTimedPayment(payid, interval, next, amount, tobank, toaccount);
	}
	
	/**
	 * Remove a timed payment
	 * @param accountid ID of account to remove timed payment from
	 * @param id ID of timed payment
	 */
	public void removeTimedPayment(String accountid, String id) throws TimedPaymentDoesNotExistException {
		Account account = accountlist.get(accountid);
		account.removeTimedPayment(id);
	}
	
	/**
	 * A time unit passes in the system
	 */
	public void tick() throws AccountDoesNotExistException, NegativeAmountOfMoneyException {
		for (Account account : accountlist.values()) {
			account.tick();
		}
	}	
}
