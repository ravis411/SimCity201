package bank;

import agent.Agent;
import bank.bankTellerRole;
import bank.gui.ClientGui;

import java.util.*;


public class bankClientRole extends Agent {
	//	Data
	public enum bankState {nothing, deposit, withdraw, loan};
	bankState state1 = bankState.nothing;
	Account myAccount;
	private ClientGui clientGui;
	public enum inLineState{notInLine, waiting, leaving, beingHelped};
	inLineState state2 = inLineState.notInLine;
	private bankTellerRole teller;
	private double requestAmount = 0;
	private boolean hasLoan = false;
	private double amountDue = 0;
	private int lineNum;

	//included until the PersonAgent is introduced
	private String name;
	public double money = new Random().nextDouble() * 100; // hack for money
	public int age = new Random().nextInt(90);
	/**
	 * initializing bankClientRole
	 */
	public bankClientRole(){
		
	}
	
	
	//Messages

	public void msgMayIHelpYou(){
		state2 = inLineState.beingHelped;
	}
	public void msgAccountOpened(Account a){
		myAccount = a;
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
	}
	public void msgTransactionCompleted(double n){
		money = money + n;
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
	}
	public void msgLoanApproved(double n){
		hasLoan = true;
		amountDue = n;
		money = money + n;
		state1 = bankState.nothing;
		state2 = inLineState.leaving;
	}


	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		if (!(state1 == bankState.nothing) || !(state2 == inLineState.leaving)){
			if (state2 == inLineState.notInLine && state1 == bankState.loan){
				getInLine(5);
				return true;
				//line 5 is the loan line
			}
			if (state2 == inLineState.notInLine){
				getInLine(lineNum);
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
				if (state1 == bankState.loan){
					IWantALoan();
					return true;
				}
			}
		}
		return false;
	}
	//Actions

	private void getInLine(int n){
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
		teller.msgLoan(requestAmount);
	}
	
	//other
/*	public void setGui(ClientGui g) {
		clientGui = g;
	}

	public ClientGui getGui() {
		return clientGui;
	}
*/	
	public String getName() {
		return name;
	}

	public String toString() {
		return "Bank Teller  " + getName();
	}
	public boolean HasLoan(){
		return hasLoan;
	}

}
