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
	public enum requestState {withdrawal, deposit, open, loan, none, notBeingHelped};
	private requestState state = requestState.none;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private numberAnnouncer announcer;
	private String name;
	private int ticketNum = 1;
	private Semaphore atStation = new Semaphore(0,true);

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
		Do("At Station.");
		atStation.release();
		announcer.msgAddLoanTeller(this);
		stateChanged();
	}
	
	public void msgInLine(bankClientRole b){
		myClient = b;
		stateChanged();
	}
	public void msgOpenAccount(){
		state = requestState.open;
	}
	public void msgLoan(double a){
		transactionAmount = a;
		state = requestState.loan;
	}


	//	Scheduler
	protected boolean pickAndExecuteAnAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (state == requestState.notBeingHelped){
			receiveClient(myClient);
			if (state == requestState.open){
				openAccount(myClient);
			} else if (state == requestState.loan){
				processLoan(myClient);
			}
		}
		return false;
	}



	//	Actions
	private void receiveClient(bankClientRole b){
		Do("Recieving new client");
		b.msgMayIHelpYou();
	}
	private void processLoan(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (b.age > 18 && b.age < 85){
					if (transactionAmount > a.amount){
						if (!b.HasLoan()){
							Do("Loan approved.");
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
		Database.INSTANCE.addToDatabase(a);
		Do("New bank account has been opened for " + b);
		b.msgAccountOpened(a);
	}

	
	//gui
	
	//Accesors, etc.
	public String getName() {
		return name;
	}

	public String toString() {
		return "Bank Teller  " + getName();
	}

}