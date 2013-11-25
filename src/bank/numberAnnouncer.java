package bank;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

public class numberAnnouncer extends Agent{
	List<bankClientRole> clients = new ArrayList<bankClientRole>();
	List<bankTellerRole> tellers = new ArrayList<bankTellerRole>();
	Queue<bankTellerRole> openTeller = new ArrayDeque<bankTellerRole>();
	loanTellerRole loanTeller;
	private int doneTeller;
	private int tellerNumber = 0;
	private int loanNumber = 0;
	public enum numberState{pending, announceB, announceL};
	public numberState state = numberState.pending;
	private String name;
	
	public void setLoanTeller(loanTellerRole lt){
		loanTeller = lt;
	}
	
	public numberAnnouncer(String n){
		super();
		name = n;
	}
	public void msgAddLoanTeller(loanTellerRole l){
		loanTeller = l;
		stateChanged();
	}
	public void msgAddBankTeller(bankTellerRole b){
		Do(b + " is ready to work.");
		tellers.add(b);
		stateChanged();
	}
	public void msgAddClient(bankClientRole c){
		Do(c + " added.");
		clients.add(c);
		stateChanged();
	}
	public void msgTransactionComplete(int b, bankTellerRole btr){
		Do("Recieved done message from line " + b);
		doneTeller = b;
		openTeller.add(btr);
		state = numberState.announceB;
		stateChanged();
	}
	public void msgLoanComplete(){
		Do("Recieved done message from loan line.");
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
			b.msgCallingTicket(tellerNumber, doneTeller, openTeller.peek());
		}	state = numberState.pending;

	}
	private void announceNumberLoan(){
		loanNumber++;
		Do("Calling loan ticket " + loanNumber);
		for (bankClientRole b : clients){
			b.msgCallingLoanTicket(loanNumber, 5, loanTeller);
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
