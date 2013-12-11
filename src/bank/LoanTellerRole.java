package bank;
import bank.BankTellerRole.location;
import bank.gui.LoanGui;
import Person.Role.*;
import Person.Role.Employee.WorkState;
import interfaces.BankClient;





import interfaces.LoanTeller;




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
public class LoanTellerRole extends Employee implements LoanTeller{
	public BankClient myClient;
	private int myClientAge;

	public enum requestState {open, loan, pending, none, notBeingHelped, repay};
	public requestState state = requestState.none;
	public enum location {entrance, station, breakRoom,closing};
	public location locationState = location.entrance;
	public double transactionAmount;
	
	
	double loanAmount;

	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private LoanNumberAnnouncer announcer;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private LoanGui loanGui = null;
	public boolean HasLoan = false;
 
	public LoanTellerRole(String workLocation){
		super(workLocation);
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
	
	public void deactivate(){
		super.deactivate();
		kill();
	}
	
	/**
	 * message received by a bankClientRole that there is someone at the desk. 
	 * @param b - bankClient being worked with
	 */
	public void msgInLine(BankClient b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Greetings customer");
		myClient = b;
		state = requestState.notBeingHelped;
		stateChanged();
	}
	
	/**
	 * message received by bankClient asking to open an account.
	 */
	public void msgOpenAccount(){
		state = requestState.open;
		stateChanged();
	}
	/**
	 * message received by a bankClient asking for a loan
	 * @param a - amount of money
	 * @param age - age of client
	 */
	public void msgLoan(double a, int age, boolean hl){
		transactionAmount = a;
		myClientAge = age;
		HasLoan = hl;
		state = requestState.loan;
		stateChanged();
	}
	public void msgRepay(double a, double m){
		transactionAmount = a;
		loanAmount = m;
		state = requestState.repay;
		stateChanged();
	}
	public void bankClosing(){
		transactionAmount = 0;
		locationState = location.closing;
		stateChanged();
		}	
	//	Scheduler
	public boolean pickAndExecuteAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if(workState == WorkState.ReadyToLeave){
			if(announcer.getNumClients() == 0){
				kill();
				return true;
			}
		}		if (locationState == location.station){
			if (state == requestState.notBeingHelped){
				receiveClient(myClient);
				return true;
			}if (state == requestState.loan){
				processLoan(myClient, HasLoan);
				return true;
			}if (state == requestState.repay){
				repayLoan(myClient);
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
	 * @param b - bankClient
	 */
	private void receiveClient(BankClient b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"Recieving new client");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	/**
	 * Processes a loan. If the age of the client is less than 18 or above 85, the client has enough money in his account, or he has an outstanding loan, the loan is denied. 
	 * Else it is approved. 
	 * @param b - bankClient
	 */
	private void processLoan(BankClient b, boolean HasLoan){
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
				else if (HasLoan){
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
	 * @param b - bankClient
	 */
	private void openAccount(BankClient b){
		Account a = new Account(b, 0);
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, this.myPerson.getName(),"New bank account has been opened for " + b);
		Database.INSTANCE.addToDatabase(a);
		b.msgAccountOpened(a);
		state = requestState.notBeingHelped;
	}

	private void repayLoan(BankClient b){
		if (loanAmount < transactionAmount){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, myPerson.getName(), "Insufficient fundds for loan repayment.");
			b.msgTransactionCompleted(0);
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, myPerson.getName(), "Loan has been successfully repaid.");
			b.msgLoanRepaid(-transactionAmount);
		}
	}
	private void Leaving(){
		announcer.msgGoodbye();
		doLeave();
	}


	//gui
	private void doGoToStation(){
		loanGui.DoGoToStation();
	}
	private void doLeave(){
		LoanTakeANumberDispenser.INSTANCE.resetTicket();
		loanGui.DoLeave();
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
		return Role.LOAN_TELLER_ROLE;
	}
	
	@Override
	public Double getSalary() {

		return null;
	}

//	@Override
//	public void workplaceIsOpen() {
//		// TODO Auto-generated method stub
//		this.activate();
//		
//	}


}