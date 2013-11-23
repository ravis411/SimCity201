package bank;

import agent.Agent;
import bank.bankTellerRole;
import bank.gui.ClientGui;
import bank.gui.bankGui;

import java.util.*;


public class bankClientRole extends Agent {
	//	Data
	public enum bankState {nothing, deposit, withdraw, loan};
	bankState state1 = bankState.nothing;
	Account myAccount;
	public enum inLineState{noTicket, waiting, goingToLine, beingHelped, leaving};
	inLineState state2 = inLineState.noTicket;
	private bankTellerRole teller;
	private double requestAmount = 0;
	private boolean hasLoan = false;
	private double amountDue = 0;
	private int ticketNum;
	private int lineNum;

	//included until the PersonAgent is introduced
	private String name;
	public double money = new Random().nextDouble() * 100; // hack for money
	public int age = new Random().nextInt(90)+15;
	/**
	 * initializing bankClientRole
	 */

	public bankClientRole(String name, String trans, double ra) {
		super();
		this.name = name;
		this.requestAmount = ra;
		if (trans.equalsIgnoreCase("deposit")){
			this.state1 = bankState.deposit;
		}
		if (trans.equalsIgnoreCase("withdraw")){
			this.state1 = bankState.withdraw;
		}
		if (trans.equalsIgnoreCase("loan")){
			this.state1 = bankState.loan;
		}
	}

	
	//Messages
	public void msgCallingTicket(int t, int l){
		if (ticketNum == t){
			state2 = inLineState.goingToLine;
			lineNum = l;
			stateChanged();
			return;
		}
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
			if (state2 == inLineState.waiting){
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
				}
				if (state1 == bankState.deposit){
					IWantToDeposit();
					return true;
				}
				if (state1 == bankState.withdraw){
					IWantToWithdraw();
					return true;
				}
			}
		}
		
		return false;
	}
	//Actions
	private void goToWaitingArea(){
		
	}
	private void goToLine(int l){
		
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
		teller.msgLoan(requestAmount);
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

}
