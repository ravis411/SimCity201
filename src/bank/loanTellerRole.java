package bank;
import bank.bankClientRole;
import bank.gui.LoanGui;
import Person.Role.*;

//import Person.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;


/**
 * 
 * loanTeller is very similar to bankTeller, but it is on a different ticket line than the bankTellers so the clients know to go to 
 * this specific line
 *
 */
public class loanTellerRole extends Role{
	private bankClientRole myClient;
	public enum requestState {open, loan, pending, none, notBeingHelped};
	private requestState state = requestState.none;
	public enum location {entrance, station, breakRoom};
	public location locationState = location.entrance;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private numberAnnouncer announcer;
	private String name;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private LoanGui loanGui = null;
 
	public loanTellerRole(){
		super();
		Accounts = Database.INSTANCE.sendDatabase();
	}

	public void setAnnouncer(numberAnnouncer a){
		announcer = a;
	}

	//	Messages
	public void msgAtStation(){
		atStation.release();
		locationState = location.station;
		announcer.msgAddLoanTeller(this);
		stateChanged();
	}

	public void msgAtIntermediate(){
		atIntermediate.release();
		stateChanged();
	}

	public void msgInLine(bankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"Greetings customer");
		myClient = b;
		state = requestState.notBeingHelped;
		stateChanged();
	}
	public void msgOpenAccount(){
		state = requestState.open;
		stateChanged();
	}
	public void msgLoan(double a){
		transactionAmount = a;
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

	private void receiveClient(bankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"Recieving new client");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	private void processLoan(bankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"Hold on a moment.");
		for (Account a : Accounts){
			if (a.client == b){
				if (b.age > 18 && b.age < 85){
					if (transactionAmount > a.amount){
						if (!b.HasLoan()){
								AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"Loan approved for $" + transactionAmount);
							announcer.msgLoanComplete();
							b.msgLoanApproved(transactionAmount);
						}
					}
				} else {
						AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"Loan denied.");
					announcer.msgLoanComplete();
					b.msgTransactionCompleted(0);
				}
				state = requestState.none;
				myClient = null;
			}
		}
	}
	private void openAccount(bankClientRole b){
		Account a = new Account(b, b.money);
			AlertLog.getInstance().logMessage(AlertTag.BANK_LOAN_TELLER, name,"New bank account has been opened for " + b);
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
		return name;
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

}