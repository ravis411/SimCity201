package bank;
import bank.bankClientRole;
import bank.gui.TellerGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Agent;
import astar.AStarTraversal;


public class bankTellerRole extends Agent{
	private bankClientRole myClient;
	private int LineNum; //from 1 to n, with 5 being the loan line, should be assigned in creation
	public enum requestState {withdrawal, deposit, open, none, notBeingHelped};
	public enum location {entrance, station, breakRoom};
	public location locationState = location.entrance;
	private requestState state = requestState.none;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private String name;
	private numberAnnouncer announcer;
	private int ticketNum = 1;
	private Semaphore atStation = new Semaphore(0,true);
	private Semaphore atIntermediate = new Semaphore(0,true);
	private TellerGui tellerGui = null;
	
	public bankTellerRole(String s, int n){
		super();
		name = s;
		LineNum = n;
		Accounts = Database.INSTANCE.sendDatabase();
	}
	
	public void setAnnouncer(numberAnnouncer a){
		this.announcer = a;
	}

	//	Messages

	public void msgAtStation(){
		atStation.release();
		locationState = location.station;
		stateChanged();
	}
	
	public void msgAtIntermediate(){
		atIntermediate.release();
		stateChanged();
	}
	
	public void msgInLine(bankClientRole b){
		myClient = b;
		stateChanged();
	}
	public void msgOpenAccount(){
		state = requestState.open;
		stateChanged();
	}
	public void msgDeposit(double a){
		transactionAmount = a;
		state = requestState.deposit;
		stateChanged();
	}
	public void msgWithdraw(double a){
		transactionAmount = a;
		state = requestState.withdrawal;
		stateChanged();
	}

	//	Scheduler
	protected boolean pickAndExecuteAnAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (locationState == location.station){
		if (state == requestState.notBeingHelped){
			receiveClient(myClient);
			if (state == requestState.deposit){
				processDeposit(myClient);
			}
			if (state == requestState.withdrawal){
				processWithdrawal(myClient);
			}
			if (state == requestState.open){
				openAccount(myClient);
			}
		}
		}
		if (locationState == location.entrance){
			goToStation();
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
		announcer.msgAddBankTeller(this);
		announcer.msgTransactionComplete(LineNum);
	}
	private void receiveClient(bankClientRole b){
		Do("Recieving new client");
		b.msgMayIHelpYou();
	}
	private void processDeposit(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				a.amount = a.amount + transactionAmount;
				b.msgTransactionCompleted(transactionAmount - (2*transactionAmount));
				state = requestState.none;
				ticketNum++;
				announcer.msgTransactionComplete(LineNum);
				Do("$" + transactionAmount + " has been deposited into the account.");
				myClient = null;
			}
		}
	}
	private void processWithdrawal(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (transactionAmount > a.amount){
					Do("Error: Attempted to withdraw more money than is available in account.");
					b.msgTransactionCompleted(0);
				}else if (transactionAmount <= a.amount){
					a.amount = a.amount - transactionAmount;
					Do("$" + transactionAmount + " has been withdrawn from the account.");
					b.msgTransactionCompleted(transactionAmount);
				}
				state = requestState.none;
				ticketNum++;
				announcer.msgTransactionComplete(LineNum);
				myClient = null;
			}
		}
	}
	private void openAccount(bankClientRole b){
		Account a = new Account(b, b.money);
		Do("New bank account has been opened for " + b);
		Database.INSTANCE.addToDatabase(a);
		b.msgAccountOpened(a);
	}

	//gui
	private void doGoToStation(){
		tellerGui.DoGoToStation();
	}
	
	
	
	//Accesors, etc.
	public String getName() {
		return name;
	}
	
	public int getLine() {
		return LineNum;
	}
	
	public void setGui(TellerGui gui) {
		tellerGui = gui;
	}

	public TellerGui getGui() {
		return tellerGui;
	}

	
	public String toString() {
		return "Bank Teller  " + getName();
	}

}