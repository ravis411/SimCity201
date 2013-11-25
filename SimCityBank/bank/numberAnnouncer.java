package bank;

import agent.Agent;

import java.util.*;

public class numberAnnouncer extends Agent{
	List<bankClientRole> clients = new ArrayList<bankClientRole>();
	List<bankTellerRole> tellers = new ArrayList<bankTellerRole>();
	loanTellerRole loanTeller;
	private int doneTeller;
	private int tellerNumber = 0;
	private int loanNumber = 0;
	public enum numberState{pending, announceB, announceL};
	public numberState state = numberState.pending;
	private String name;

	public numberAnnouncer(String n){
		super();
		name = n;
	}

	public void msgAddLoanTeller(loanTellerRole l){
		loanTeller = l;
		stateChanged();
	}
	public void msgAddBankTeller(bankTellerRole b){
		Do(b + " added.");
		tellers.add(b);
		stateChanged();
	}
	public void msgAddClient(bankClientRole c){
		Do(c + " added.");
		clients.add(c);
		stateChanged();
	}
	public void msgGotTicket(){

	}
	public void msgTransactionComplete(int b){
		Do("Recieved done message from line " + b);
		doneTeller = b;
		state = numberState.announceB;
		stateChanged();
	}
	public void msgLoanComplete(){
		state = numberState.announceL;
		stateChanged();
	}

	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (state == numberState.announceB){
			announceNumberBank();
			return true;
		}
		if (state == numberState.announceL){
			announceNumberLoan();
			return true;
		}
		return false;
	}
	//actions
	private void announceNumberBank(){
		tellerNumber++;
		Do("Calling ticket " + tellerNumber);
		for (bankClientRole b : clients){
			b.msgCallingTicket(tellerNumber, doneTeller);
		}
		state = numberState.pending;
	}
	private void announceNumberLoan(){
		loanNumber++;
		for (bankClientRole b : clients){
			b.msgCallingTicket(loanNumber, 5);
		}
		state = numberState.pending;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return "Bank Teller  " + getName();
	}

}
