package bank;

import agent.Agent;
import bank.NumberAnnouncer.numberState;
import interfaces.AnnouncerB;
import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.LoanTeller;

import java.util.*;
/**
 * 
 * @author Byron Choy
 *
 */

public class LoanNumberAnnouncer extends Agent implements AnnouncerB{
	List<BankClient> clients = new ArrayList<BankClient>();
	LoanTeller loanTeller;
	private int loanNumber = 0;
	public enum numberState{pending, announceL};
	public numberState state = numberState.pending;
	private String name;
	private boolean onTheWay = false;

	public void setLoanTeller(LoanTellerRole lt){
		loanTeller = lt;
	} 

	public LoanNumberAnnouncer(String n){
		super();
		name = n;
	}
	
	//Messages
	
	public void msgOnTheWay(){
		onTheWay = true;
		stateChanged();
	}
	public void msgAddLoanTeller(LoanTeller l){
		loanTeller = l;
		stateChanged();
	}
	public void msgAddClient(BankClient c){
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

	public void msgRemoveClient(BankClient b){
		clients.remove(b);
		stateChanged();
	}

	public void msgGoodbye(){
		loanTeller = null;
		stateChanged();
	}

	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (loanTeller == null){
			Reset();
		}
		if (loanTeller != null && state == numberState.announceL && !(clients.isEmpty())){
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
				e.printStackTrace();
			}
			Do("Calling loan ticket " + loanNumber);
			for (BankClient b : clients){
				b.msgCallingLoanTicket(loanNumber, 5, loanTeller);
			}
		}
		state = numberState.pending;
	}
	private void Reset(){
		state = numberState.pending;
		loanNumber = 0;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return "Bank Teller  " + getName();
	}

	public int getNumClients(){
		return clients.size();
	}

}
