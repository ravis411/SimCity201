package bank;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

public class numberAnnouncer extends Agent{
	List<bankClientRole> clients = new ArrayList<bankClientRole>();
	List<bankTellerRole> tellers = new ArrayList<bankTellerRole>();
	Queue<bankTellerRole> openTeller = new ArrayDeque<bankTellerRole>();
	private int doneTeller;
	private Timer timer;
	private int tellerNumber = 0;
	public enum numberState{pending, announceB};
	public numberState state = numberState.pending;
	private String name;
	private boolean onTheWay = false;

	public numberAnnouncer(String n){
		super();
		name = n;
	}
	public void msgOnTheWay(){
		onTheWay = true;
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
	public void msgTransactionComplete(int b, bankTellerRole btr, bankClientRole bcr){
		Do("Recieved done message from line " + b);
		doneTeller = b;
		openTeller.add(btr);
		clients.remove(bcr);
		tellerNumber++;
		state = numberState.announceB;
		onTheWay = false;
		stateChanged();
	}

	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (state == numberState.announceB){
			announceNumberBank();
			return true;
		}
		return false;
	}
	//actions
	private void announceNumberBank(){
		while (onTheWay == false){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Do("Calling ticket " + tellerNumber);
			for (bankClientRole b : clients){
				b.msgCallingTicket(tellerNumber, doneTeller, openTeller.peek());
			}
			state = numberState.pending;
		}
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return "Bank Teller  " + getName();
	}

}
