package bank;
import bank.bankClientRole;
import bank.gui.TellerGui;

import java.util.*;

import agent.Agent;
import astar.AStarTraversal;


public class bankTellerRole extends Agent{
	private bankClientRole myClient;
	private int LineNum; //from 1 to n, with 5 being the loan line, should be assigned in creation
	public enum requestState {withdrawal, deposit, open, none, notBeingHelped};
	private requestState state = requestState.none;
	double transactionAmount;
	private List<Account> Accounts = Database.INSTANCE.sendDatabase();
	private String name;
	private numberAnnouncer announcer;
	private int ticketNum = 1;


	public bankTellerRole(String s, int n){
		super();
		name = s;
		LineNum = n;
		Accounts = Database.INSTANCE.sendDatabase();
	}

	//	Messages
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
		return false;
	}



	//	Actions
	private void receiveClient(bankClientRole b){
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
				myClient = null;
			}
		}
	}
	private void processWithdrawal(bankClientRole b){
		for (Account a : Accounts){
			if (a.client == b){
				if (transactionAmount > a.amount){
					b.msgTransactionCompleted(0);
				}else if (transactionAmount <= a.amount){
					a.amount = a.amount - transactionAmount;
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
		Database.INSTANCE.addToDatabase(a);
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