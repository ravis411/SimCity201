package bank;
import bank.BankClientRole;
import bank.gui.LoanGui;
import Person.Role.*;
import interfaces.Employee;


//import Person.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import util.Interval;


/**
 * 
 * loanTeller is very similar to bankTeller, but it is on a different ticket line than the bankTellers so the clients know to go to 
 * this specific line
 * @author Byron Choy
 *
 */
public class LoanTellerRole extends Role implements Employee{
	private BankClientRole myClient;
	private int myClientAge;
	public enum requestState {open, loan, pending, none, notBeingHelped};
	private requestState state = requestState.none;
	public enum location {entrance, station, breakRoom};
	public location locationState = location.entrance;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private LoanNumberAnnouncer announcer;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private LoanGui loanGui = null;
 
	public LoanTellerRole(){
		super();
		Accounts = Database.INSTANCE.sendDatabase();
	}

	public void setAnnouncer(LoanNumberAnnouncer a){
		announcer = a;
	}

	//	Messages
	/**
	 * Message stating that the teller is at his station and ready to take orders. Releases the atStation semaphore.
	 */
	public void msgAtStation(){
		atStation.release();
		locationState = location.station;
		stateChanged();
	}
	/**
	 * Message that releases the atIntermediate semaphore. Only for aesthetics - makes it so the teller doesn't move through objects
	 */
	public void msgAtIntermediate(){
		atIntermediate.release();
		stateChanged();
	}
	/**
	 * message received by a bankClientRole that there is someone at the desk. 
	 * @param b - bankClientRole being worked with
	 */
	public void msgInLine(BankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Greetings customer");
		myClient = b;
		state = requestState.notBeingHelped;
		stateChanged();
	}
	
	/**
	 * message received by bankClientRole asking to open an account.
	 */
	public void msgOpenAccount(){
		state = requestState.open;
		stateChanged();
	}
	/**
	 * message received by a bankClientRole asking for a loan
	 * @param a - amount of money
	 * @param age - age of client
	 */
	public void msgLoan(double a, int age){
		transactionAmount = a;
		myClientAge = age;
		state = requestState.loan;
		stateChanged();
	}


	//	Scheduler
	public boolean pickAndExecuteAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (locationState == location.station){
			if (state == requestState.notBeingHelped){
				receiveClient(myClient);
				return true;
			}if (state == requestState.loan){
				processLoan(myClient);
				return true;
			}
			if (state == requestState.open){
				openAccount(myClient);
				return true;
			}
		}
		if (locationState == location.entrance){
			goToStation();
			return true;
		}
		return false;
	}



	//	Actions
	/**
	 * After getting to the intermediate position, sends the teller to the right station. After he gets there, 
	 * sends a message to the number announcer enabling him to increment the announcer.
	 */
	private void goToStation(){
		try {
			atIntermediate.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		doGoToStation();
		try {
			atStation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		announcer.msgAddLoanTeller(this);
		announcer.msgLoanComplete();
	}

	/**
	 * Greeting the client
	 * @param b - bankClientRole
	 */
	private void receiveClient(BankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Recieving new client");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	/**
	 * Processes a loan. If the age of the client is less than 18 or above 85, the client has enough money in his account, or he has an outstanding loan, the loan is denied. 
	 * Else it is approved. 
	 * @param b - bankClientRole
	 */
	private void processLoan(BankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Hold on a moment.");
		for (Account a : Accounts){
			if (a.client == b){
				if (myClientAge < 18 || myClientAge > 85){
					AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(), "Loan denied, age inappropriate.");
					announcer.msgLoanComplete();
					b.msgTransactionCompleted(0);
				}
				else if (transactionAmount < a.amount){
					AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(), "Loan denied, amount exists in account.");
					announcer.msgLoanComplete();
					b.msgTransactionCompleted(0);
				}
				else if (b.HasLoan()){
					AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(), "Loan denied, existing loan has not been paid.");
					announcer.msgLoanComplete();
					b.msgTransactionCompleted(0);
				} else {
							AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Loan approved for $" + transactionAmount);
							announcer.msgLoanComplete();
							b.msgLoanApproved(transactionAmount);
				}
				state = requestState.none;
				myClient = null;
			}
		}
	}
	/**
	 * Opens new account
	 * @param b - bankClientRole
	 */
	private void openAccount(BankClientRole b){
		Account a = new Account(b, 0);
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"New bank account has been opened for " + b);
		Database.INSTANCE.addToDatabase(a);
		b.msgAccountOpened(a);
		state = requestState.notBeingHelped;
	}


	//gui
	private void doGoToStation(){
		loanGui.DoGoToStation();
	}


	//Accesors, etc.
	public String getName() {
		return this.myPerson.getName();
	}

	public void setGui(LoanGui gui){
		loanGui = gui;
	}

	public LoanGui getGui(){
		return loanGui;
	}
	public String toString() {
		return "Loan Teller " + getName();
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return null;
	}

	@Override
	public Interval getShift() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}

}