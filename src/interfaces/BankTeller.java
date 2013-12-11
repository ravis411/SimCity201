package interfaces;


public interface BankTeller {
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
	 * message received by a bankClientRole asking to deposit money into an account.
	 * @param a - amount of money
	 */
	public void msgDeposit(double a);
	/**
	 * message received by a bankClientRole asking to withdraw money from an account.
	 * @param a - amount of money
	 */

	public void msgWithdraw(double a);
}
