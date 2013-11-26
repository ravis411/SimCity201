package bank;
import bank.bankClientRole;
import bank.gui.TellerGui;
import interfaces.Employee;

import java.util.*;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import util.Interval;
import Person.Role.Role;


public class bankTellerRole extends Role implements Employee{
	private bankClientRole myClient;
	private int LineNum; //from 1 to n, with 5 being the loan line, should be assigned in creation
	public enum requestState {pending, withdrawal, deposit, open, none, notBeingHelped};
	public enum location {entrance, station, breakRoom};
	public location locationState = location.entrance;
	private requestState state = requestState.none;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private String name;
	private numberAnnouncer announcer;
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
		state = requestState.notBeingHelped;
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
	public boolean pickAndExecuteAction() {
		Accounts = Database.INSTANCE.sendDatabase();
		if (locationState == location.station){
			if (state == requestState.deposit){
				processDeposit(myClient);
				return true;
			}
			if (state == requestState.withdrawal){
				processWithdrawal(myClient);
				return true;
			}
			if (state == requestState.open){
				openAccount(myClient);
				return true;
			}
			if (state == requestState.notBeingHelped){
				receiveClient(myClient);
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
		announcer.msgAddBankTeller(this);
		announcer.msgTransactionComplete(LineNum,this, null);
	}
	private void receiveClient(bankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Recieving new client");
			AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Hello " + b + ", how may I help you.");
		b.msgMayIHelpYou();
		state = requestState.pending;
	}
	private void processDeposit(bankClientRole b){
			AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Ok, hold on.");
		for (Account a : Accounts){
			if (a.client == b){
				a.amount = a.amount + transactionAmount;
					AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"$" + transactionAmount + " has been deposited into the account.");
				b.msgTransactionCompleted(transactionAmount - (2*transactionAmount));
				state = requestState.none;
				announcer.msgTransactionComplete(LineNum,this,b);
				myClient = null;
			}
		}
	}
	private void processWithdrawal(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (transactionAmount > a.amount){
						AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"Error: Attempted to withdraw more money than is available in account.");
					b.msgTransactionCompleted(0);
				}else if (transactionAmount <= a.amount){
					a.amount = a.amount - transactionAmount;
						AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"$" + transactionAmount + " has been withdrawn from the account.");
					b.msgTransactionCompleted(transactionAmount);
				}
				state = requestState.none;
				announcer.msgTransactionComplete(LineNum,this,b);
				myClient = null;
			}
		}
	}
	private void openAccount(bankClientRole b){
		Account a = new Account(b,0);
			AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, name,"New bank account has been opened for " + b);
		Database.INSTANCE.addToDatabase(a);
		b.msgAccountOpened(a);
		state = requestState.notBeingHelped;
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
		return "Bank Teller " + getName();
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
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