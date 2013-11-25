package bank;
import bank.bankClientRole;
import bank.bankTellerRole.requestState;
import bank.gui.LoanGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Agent;
import astar.AStarTraversal;

/**
 * 
 * loanTeller is very similar to bankTeller, but it is on a different ticket line than the bankTellers so the clients know to go to 
 * this specific line
 *
 */
public class loanTellerRole extends Agent{
	private bankClientRole myClient;
	private int LineNum = 5; 
	public enum requestState {open, loan, pending, none, notBeingHelped};
	private requestState state = requestState.none;
	public enum location {entrance, station, breakRoom};
	public location locationState = location.entrance;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private numberAnnouncer announcer;
	private String name;
	private int ticketNum = 1;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private LoanGui loanGui = null;
	
	public loanTellerRole(String s){
		super();
		name = s;
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
		Do("Greetings customer");
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
	protected boolean pickAndExecuteAnAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (locationState == location.station){
			if (state == requestState.notBeingHelped){
				receiveClient(myClient);
				return true;
			}if (state == requestState.open){
				openAccount(myClient);
				return true;
			} else if (state == requestState.loan){
				processLoan(myClient);
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
		Do("Recieving new client");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	private void processLoan(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (b.age > 18 && b.age < 85){
					if (transactionAmount > a.amount){
						if (!b.HasLoan()){
							Do("Loan approved for $" + transactionAmount);
							b.msgLoanApproved(transactionAmount);
						}
					}
				} else {
					Do("Loan denied.");
					b.msgTransactionCompleted(0);
				}
				state = requestState.none;
				ticketNum++;
				announcer.msgLoanComplete();
				myClient = null;
			}
		}
	}
	private void openAccount(bankClientRole b){
		Account a = new Account(b, b.money);
		Do("New bank account has been opened for " + b);
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

}