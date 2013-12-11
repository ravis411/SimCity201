package bank;

import agent.Agent;
import interfaces.AnnouncerA;
import interfaces.BankClient;
import interfaces.BankTeller;

import java.util.*;
/**
 * 
 * @author Byron Choy
 *
 */

public class NumberAnnouncer extends Agent implements AnnouncerA{
	public List<BankClient> clients = new ArrayList<BankClient>();
	public List<BankTeller> tellers = new ArrayList<BankTeller>();
	Queue<BankTeller> openTeller = new ArrayDeque<BankTeller>();
	private BankClient robber = null;
	private int doneTeller;
	private int tellerNumber = 0;
	private double robbedMoney = 0;
	public enum numberState{pending, gettingRobbed, announceB};
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

	public void msgRemoveClient(BankClient b){
		clients.remove(b);
		if (b == robber){
			robber = null;
			for (BankClient c : clients){
				c.msgUnfreeze();
			}
		}
		stateChanged();
	}

	public void msgGoodbye(BankTeller o){
		tellers.remove(o);
		stateChanged();
	}

	public void msgStealingMoney(double stealAmount, BankClient bcr) {
		robbedMoney = stealAmount;
		robber = bcr;
		stateChanged();
	}

	//scheduler
	protected boolean pickAndExecuteAnAction() {
		if (robbedMoney == 0){
			if (!(tellers.isEmpty()) && state == numberState.announceB && !(clients.isEmpty())){
				announceNumberBank();
				return true;
			}
			if (tellers.isEmpty()){
				Reset();
			}
		} else listenToRobber();

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
				System.out.println("Telling client the number");
				b.msgCallingTicket(tellerNumber, doneTeller, openTeller.peek());
			}
			state = numberState.pending;
		}
	}

	private void Reset(){
		state = numberState.pending;
		tellerNumber = 0;
	}
	
	private void listenToRobber(){
		for (BankClient b : clients){
			if (b != robber){
				b.msgFreeze();
			}
		}
		robber.msgTransactionCompleted(robbedMoney);
		robbedMoney = 0;
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
