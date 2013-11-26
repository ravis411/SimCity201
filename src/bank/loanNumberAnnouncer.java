package bank;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

public class loanNumberAnnouncer extends Agent{
	List<bankClientRole> clients = new ArrayList<bankClientRole>();
	loanTellerRole loanTeller;
	private int doneTeller;
	private int loanNumber = 0;
	public enum numberState{pending, announceL};
	public numberState state = numberState.pending;
	private String name;
	private boolean onTheWay = false;

	public void setLoanTeller(loanTellerRole lt){
		loanTeller = lt;
	} 

	public loanNumberAnnouncer(String n){
		super();
		name = n;
	}
	public void msgOnTheWay(){
		onTheWay = true;
		stateChanged();
	}
	public void msgAddLoanTeller(loanTellerRole l){
		loanTeller = l;
		stateChanged();
	}
	public void msgAddClient(bankClientRole c){
		Do(c + " added.");
		clients.add(c);
		stateChanged();
	}
	public void msgLoanComplete(){
		Do("Recieved done message from loan line.");
		loanNumber++;
		onTheWay = false;
		state = numberState.announceL;
		stateChanged();
	}

	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (state == numberState.announceL){
			announceNumberLoan();
			return true;
		}
		return false;
	}
	//actions
	private void announceNumberLoan(){
		while (onTheWay == false){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Do("Calling loan ticket " + loanNumber);
			for (bankClientRole b : clients){
				b.msgCallingLoanTicket(loanNumber, 5, loanTeller);
			}
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
