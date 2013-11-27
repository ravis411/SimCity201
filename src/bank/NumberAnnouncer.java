package bank;

import agent.Agent;
import bank.interfaces.AnnouncerA;
import bank.interfaces.BankClient;
import bank.interfaces.BankTeller;

import java.util.*;
/**
 * 
 * @author Byron Choy
 *
 */

public class NumberAnnouncer extends Agent implements AnnouncerA{
	List<BankClient> clients = new ArrayList<BankClient>();
	List<BankTeller> tellers = new ArrayList<BankTeller>();
	Queue<BankTeller> openTeller = new ArrayDeque<BankTeller>();
	private int doneTeller;
	private int tellerNumber = 0;
	public enum numberState{pending, announceB};
	public numberState state = numberState.pending;
	private String name;
	private boolean onTheWay = false;

	public NumberAnnouncer(String n){
		super();
		name = n;
	}
	
	//messages
	
	public void msgOnTheWay(){
		onTheWay = true;
		stateChanged();
	}
	public void msgAddBankTeller(BankTeller b){
		Do(b + " is ready to work.");
		tellers.add(b);
		stateChanged();
	}
	public void msgAddClient(BankClient c) {
		Do(c + " added.");
		clients.add(c);
		stateChanged();
	}
	public void msgTransactionComplete(int b, BankTeller btr, BankClient bcr) {
		Do("Recieved done message from line " + b);
		doneTeller = b;
		openTeller.add(btr);
		clients.remove(bcr);
		tellerNumber++;
		state = numberState.announceB;
		onTheWay = false;
		stateChanged();
		
	}
	public void msgGoodbye(BankTeller o){
		tellers.remove(o);
		stateChanged();
	}
	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (!(tellers.isEmpty()) && state == numberState.announceB){
			announceNumberBank();
			return true;
		}
		if (tellers.isEmpty()){
			Reset();
		}
		return false;
	}
	//actions
	private void announceNumberBank(){
		while (onTheWay == false){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Do("Calling ticket " + tellerNumber);
			for (BankClient b : clients){
				b.msgCallingTicket(tellerNumber, doneTeller, openTeller.peek());
			}
			state = numberState.pending;
		}
	}
	private void Reset(){
		state = numberState.pending;
		tellerNumber = 0;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return "Bank Teller  " + getName();
	}




}
