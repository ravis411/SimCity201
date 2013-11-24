package bank;

import agent.Agent;
import java.util.*;

public class numberAnnouncer extends Agent{
	List<bankClientRole> clients = new ArrayList<bankClientRole>();
	List<bankTellerRole> tellers = new ArrayList<bankTellerRole>();
	loanTellerRole loanTeller;
	private int doneTeller;
	private int tellerNumber = 1;
	private int loanNumber = 1;
	public enum numberState{pending, announceB, announceL};
	public numberState state = numberState.pending;

	public void msgAddLoanTeller(loanTellerRole l){
		loanTeller = l;
		stateChanged();
	}
	public void msgAddBankTeller(bankTellerRole b){
		tellers.add(b);
		stateChanged();
	}
	public void msgAddClient(bankClientRole c){
		clients.add(c);
		stateChanged();
	}
	public void msgTransactionComplete(int b){
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
		for (bankClientRole b : clients){
			b.msgCallingTicket(tellerNumber, doneTeller);
		}
	}
	private void announceNumberLoan(){
		loanNumber++;
		for (bankClientRole b : clients){
			b.msgCallingTicket(loanNumber, 5);
		}
	}
}
