package interfaces;

public interface LoanTeller {
	/**
	 * Message stating that the teller is at his station and ready to take orders. Releases the atStation semaphore.
	 */
	public void msgAtStation();
	/**
	 * Message that releases the atIntermediate semaphore. Only for aesthetics - makes it so the teller doesn't move through objects
	 */
	public void msgAtIntermediate();
	/**
	 * message received by a bankClientRole that there is someone at the desk. 
	 * @param b - bankClientRole being worked with
	 */
	public void msgInLine(BankClient b);
	
	/**
	 * message received by bankClientRole asking to open an account.
	 */
	public void msgOpenAccount();
	/**
	 * message received by a bankClientRole asking for a loan
	 * @param a - amount of money
	 * @param age - age of client
	 * @param hl - hasLoan
	 */
	public void msgLoan(double a, int age, boolean hl);

	/**
	 * message received by a bankClientRole asking to repay a loan
	 * @param a - amount of money offered
	 * @param m - amount of money to be repaid
	 */
	public void msgRepay(double a, double m);
}
