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
	public enum inLineState{noTicket, waiting, goingToLine, atDesk, beingHelped, leaving};
	inLineState state2 = inLineState.noTicket;
	private bankTellerRole teller = null;
	private loanTellerRole loanTeller = null;
	private numberAnnouncer announcer;
	private double requestAmount = 0;
	private boolean hasLoan = false;
	private double amountDue = 0;
	private int ticketNum;
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
		ticketNum = takeANumberDispenser.INSTANCE.pullTicket();
		if (trans.equalsIgnoreCase("deposit")){
			this.state1 = bankState.deposit;
		}
		if (trans.equalsIgnoreCase("withdraw")){
			this.state1 = bankState.withdraw;
		}
		if (trans.equalsIgnoreCase("loan")){
			this.state1 = bankState.loan;
		}
		Do("My ticket number is " + ticketNum);
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
	public void msgCallingTicket(int t, int l){
		if (ticketNum == t){
			state2 = inLineState.goingToLine;
			lineNum = l;
			stateChanged();
		} else stateChanged();
	}
	public void msgAtLine(){ //from gui
		atLine.release();
		state2 = inLineState.atDesk;
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
		if (state1 != bankState.nothing){
			if (state2 == inLineState.noTicket){
				goToWaitingArea();
				return true;
			}
			if (state2 == inLineState.goingToLine){
				goToLine(lineNum);
				return true;
			}
			if (state2 == inLineState.beingHelped){
				if (myAccount == null){
					openAccount();
					return true;
				}else {
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
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		announcer.msgAddClient(this);
	}
	private void goToLine(int l){
		DoGoToLine(l);
		try {
			atLine.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		teller.msgInLine(this);
	}
	
	private void openAccount(){
		teller.msgOpenAccount();
	}
	private void IWantToDeposit(){
		teller.msgDeposit(requestAmount);
	}
	private void IWantToWithdraw(){
		teller.msgWithdraw(requestAmount);
	}
	private void IWantALoan(){
		loanTeller.msgLoan(requestAmount);
	}
	private void Leaving(){
		clientGui.DoLeave();
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
		return "Bank Client  " + getName();
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
