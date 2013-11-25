package bank;

import agent.Agent;
import bank.bankTellerRole;
import bank.gui.ClientGui;
import bank.gui.TellerGui;

import java.util.*;
import java.util.concurrent.*;

public class bankClientRole extends Agent {
	//	Data
	public enum bankState {nothing, deposit, withdraw, loan};
	bankState state1 = bankState.nothing;
	Account myAccount;
	public enum inLineState{noTicket, waiting, goingToLine, atDesk, beingHelped, transactionProcessing, leaving};
	inLineState state2 = inLineState.noTicket;
	private bankTellerRole teller = null;
	private loanTellerRole loanTeller = null;
	private numberAnnouncer announcer;
	private double requestAmount = 0;
	private boolean hasLoan = false;
	private double amountDue = 0;
	private int ticketNum = 0;
	private int loanTicketNum = 0;
	private int lineNum;
	private Semaphore atLine = new Semaphore(0,true);
	private ClientGui clientGui = null;
	private Semaphore atWaitingArea = new Semaphore(0,true);
	//included until the PersonAgent is introduced
	private String name;
	public double money = new Random().nextDouble() * 100; // hack for money
	public int age = new Random().nextInt(90)+15;

	//hack for accounts - to ensure that there are some existing accounts at the beginning of SimCity
	private int existsBankAccount = new Random().nextInt(10);

	/**
	 * hack to establish connection to bankTellerRole
	 */
	public void setTeller(bankTellerRole btr){
		this.teller = btr;
	}
	/**
	 * hack to establish connection to loanTellerRole
	 */	
	public void setLoanTeller(loanTellerRole ltr) {
		this.loanTeller = ltr;
	}

	public void setAnnouncer(numberAnnouncer a){
		this.announcer = a;
	}

	/**
	 * initializing bankClientRole
	 * there is a hack implemented to make sure there are some bank accounts to begin with so that 
	 * not everyone has to go open an account in the bank
	 */  

	public bankClientRole(String name, String trans, double ra) {
		super();
		this.name = name;
		this.requestAmount = ra;
		if (trans.equalsIgnoreCase("deposit")){
			this.state1 = bankState.deposit;
			ticketNum = takeANumberDispenser.INSTANCE.pullTicket();
			Do("My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("withdraw")){
			this.state1 = bankState.withdraw;
			ticketNum = takeANumberDispenser.INSTANCE.pullTicket();
			Do("My ticket number is " + ticketNum);
		}
		if (trans.equalsIgnoreCase("loan")){
			this.state1 = bankState.loan;
			loanTicketNum = loanTakeANumberDispenser.INSTANCE.pullTicket();
			Do("My ticket number is " + loanTicketNum);
		}
		//hack to ensure there are at least some bank accounts at simulation start
		if (existsBankAccount > 4){
			int newMoney = new Random().nextInt(100);
			myAccount = new Account(this,newMoney);
			Database.INSTANCE.addToDatabase(myAccount);
		}
	}


	//Messages
	public void msgAtWaitingArea(){
		atWaitingArea.release();
		state2 = inLineState.waiting;
		stateChanged();
	}
	public void msgCallingTicket(int t, int l, bankTellerRole btr){
		if (ticketNum == t){
			state2 = inLineState.goingToLine;
			lineNum = l;
			Do("That's my ticket, I need to go to line " + lineNum);
			setTeller(btr);
			stateChanged();
		}
	}
	public void msgCallingLoanTicket(int loanNumber, int i,
			loanTellerRole loanTeller2) {
		if (loanTicketNum == loanNumber){
			state2 = inLineState.goingToLine;
			lineNum = i;
			Do("That's my ticket, I need to go to line " + lineNum);
			setLoanTeller(loanTeller2);
			stateChanged();
		}

	}
	public void msgAtLine(){ //from gui
		atLine.release();
		stateChanged();
	}
	public void msgMayIHelpYou(){
		state2 = inLineState.beingHelped;
		stateChanged();
	}
	public void msgAccountOpened(Account a){
		myAccount = a;
		stateChanged();
	}
	public void msgTransactionCompleted(double n){
		money = money + n;
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}
	public void msgLoanApproved(double n){
		hasLoan = true;
		amountDue = n;
		money = money + n;
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
		stateChanged();
	}


	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		if (state2 == inLineState.goingToLine && state1 != bankState.loan){
			goToLine(lineNum);
			return true;
		}
		if (state2 == inLineState.goingToLine && state1 == bankState.loan){
			goToLoanLine();
			return true;
		}
		if (state1 != bankState.nothing){
			if (state2 == inLineState.noTicket){
				goToWaitingArea();
				return true;
			}
			if (state2 == inLineState.beingHelped){
				if (myAccount == null && state1 != bankState.loan){
					openAccount();
					return true;
				}else if (myAccount == null && state1 == bankState.loan){
					loanOpenAccount();
					return true;
				}else{
					if (state1 == bankState.deposit){
						IWantToDeposit();
						return true;
					}
					if (state1 == bankState.withdraw){
						IWantToWithdraw();
						return true;
					}
					if (state1 == bankState.loan){
						IWantALoan();
						return true;
					}
				}
			}
		}
	if (state2 == inLineState.leaving){
		Leaving();
		return true;
	}
	return false;
}
//Actions
private void goToWaitingArea(){
	DoGoToWaitingArea();
	announcer.msgAddClient(this);
	try {
		atWaitingArea.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}
private void goToLine(int l){
	DoGoToLine(l);
	try {
		atLine.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	Do("Arrived at line, the teller's name is " + teller);
	teller.msgInLine(this);
	state2 = inLineState.atDesk;
}
private void goToLoanLine(){
	DoGoToLine(5);
	try{
		atLine.acquire();
	} catch(InterruptedException e){
		e.printStackTrace();
	}
	Do("Arrived at line, the teller's name is " + loanTeller);
	loanTeller.msgInLine(this);
	state2 = inLineState.atDesk;
}
private void openAccount(){
	Do("I want to open an account.");
	teller.msgOpenAccount();
	state2 = inLineState.transactionProcessing;
}
private void loanOpenAccount(){
	Do("I want to open an account.");
	loanTeller.msgOpenAccount();
	state2 = inLineState.transactionProcessing;
}
private void IWantToDeposit(){
	Do("I want to deposit money.");
	teller.msgDeposit(requestAmount);
	state2 = inLineState.transactionProcessing;

}
private void IWantToWithdraw(){
	Do("I want to withdraw money.");
	teller.msgWithdraw(requestAmount);
	state2 = inLineState.transactionProcessing;

}
private void IWantALoan(){
	Do("I want a loan.");
	loanTeller.msgLoan(requestAmount);
	state2 = inLineState.transactionProcessing;

}
private void Leaving(){
	Do("Thanks, goodbye.");
	clientGui.DoLeave();
	state2 = inLineState.noTicket;
}


//gui
private void DoGoToLine(int l){
	Do("Going to line " + l);
	clientGui.doGoToLine(l);
}

private void DoGoToWaitingArea(){
	Do("Going to waiting area");
	clientGui.doGoToWaitingArea();

}




//other
public String getName() {
	return name;
}

public String toString() {
	return "Bank Client " + getName();
}
public boolean HasLoan(){
	return hasLoan;
}
public void setGui(ClientGui gui) {
	clientGui = gui;
}

public ClientGui getGui() {
	return clientGui;
}




}
