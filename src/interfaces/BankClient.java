package interfaces;


import bank.Account;


/**
 * a BankClient interface to unit test a BankClientRole
 * @author Byron Choy
 *
 */

public interface BankClient {
	/**
	 * Message sent by the GUI releasing the semaphore when the client reaches the waiting area
	 */
	public abstract void msgAtWaitingArea();

	/**
	 * 
	 * Sent by the number announcer. If the number matches the client's ticket, the client should go to the proper line
	 * @param t = ticket number
	 * @param l = line number
	 * @param btr = bank teller role
	 */
	public abstract void msgCallingTicket(int t, int l, BankTeller btr);

	/**
	 * Same as msgCallingTicket, except for loans
	 * @param loanNumber
	 * @param i = line number 
	 * @param loanTeller2 = loan teller
	 */
	public abstract void msgCallingLoanTicket(int loanNumber, int i, LoanTeller loanTeller2);

	/**
	 * sent from the gui when the client is at the line
	 */
	public abstract void msgAtLine();

	/**
	 * sent by the bank teller as a greeting to the client to let the client know he can communicate his needs
	 */
	public abstract void msgMayIHelpYou();
	/**
	 *sent by the bank teller. Gives the client his new account.
	 * @param a = account
	 */
	public abstract void msgAccountOpened(Account a);
	/**
	 * sent by the bank teller. Does deposits, withdrawals, and loan failures.
	 * @param n = amount that is sent, 0 is sent when things fail (withdrawal fail, loan fail)
	 */
	public abstract void msgTransactionCompleted(double n);

	/**
	 * sent by the bank teller when a loan is approved. 
	 * @param n = loan amount
	 */
	public abstract void msgLoanApproved(double n);
	
	/**
	 * sent by the loan teller when a loan has been successfully repaid
	 * @param n - repayment amount
	 */
	public abstract void msgLoanRepaid(double n);
	public abstract void msgFreeze();
	public abstract void msgUnfreeze();
}
