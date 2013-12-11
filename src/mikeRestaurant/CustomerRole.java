package mikeRestaurant;

import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import building.BuildingList;
import building.Restaurant;
import Person.Role.Role;
import mikeRestaurant.gui.CustomerGui;
import mikeRestaurant.interfaces.Customer;
import mikeRestaurant.interfaces.Waiter;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends GenericCustomer implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private CustomerGui customerGui;
	
	private final static double MAX_AMOUNT_OF_MONEY = 20.00;
	
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore waiterAtTable = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore cashierReply = new Semaphore(0, true);
	private Semaphore atStart = new Semaphore(0, true);
	private Semaphore atLeave = new Semaphore(0, true);
	
	private Map<String, Double> menu; //a customer does have a menu in a real restaurant
	private String choice;
	private WaiterRole waiter; //the waiter of this customer
	private CashierRole cashier;
	private HostRole host;
	private double check;
	private CustomerState status;
	private CustomerEvent event;
	
	private Random rnd;
	
	private double money;
	private double moneyOwedToRestaurant;
	
	private boolean isGoodPerson;

	//states and events for a finite-state machine
	public enum CustomerState {Waiting, MovingToSeat, Seated, WaitingForWaiterToOrder, Ordered, Served, Eating, AskedForCheck, ReadyToLeave, Left}
	public enum CustomerEvent {NoEvent, NotifiedHost, Waiting, RestaurantIsFull, MovingToSeat, Seated, WaiterAskedForOrder, Served, ReceivedCheck, NeedsCheck, PayingCheck, GoToJail, ReadyToLeave, InJail}

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(String name, CashierRole cashier, HostRole host){
		super();
		this.name = name;
		menu = null;
		rnd = new Random();
		status = CustomerState.Waiting;
		event = CustomerEvent.NoEvent;
		
		DecimalFormat df = new DecimalFormat("0.00");
		money = Double.parseDouble(df.format(rnd.nextDouble()*MAX_AMOUNT_OF_MONEY));
		
		moneyOwedToRestaurant = 0.0;
		
		isGoodPerson = rnd.nextBoolean();
		if(name.equalsIgnoreCase("flake") || name.equalsIgnoreCase("bad_person")){
			isGoodPerson = false;
		}
		
		if(name.equalsIgnoreCase("poor") || name.equalsIgnoreCase("good_person")){
			isGoodPerson = true;
		}
	}
	
	public CustomerRole(){
		super();
		
		menu = null;
		rnd = new Random();
		status = CustomerState.Waiting;
		event = CustomerEvent.NoEvent;
		
		DecimalFormat df = new DecimalFormat("0.00");
		money = Double.parseDouble(df.format(rnd.nextDouble()*MAX_AMOUNT_OF_MONEY));
		
		moneyOwedToRestaurant = 0.0;
		
		isGoodPerson = rnd.nextBoolean();
//		if(name.equalsIgnoreCase("flake") || name.equalsIgnoreCase("bad_person")){
//			isGoodPerson = false;
//		}
//		
//		if(name.equalsIgnoreCase("poor") || name.equalsIgnoreCase("good_person")){
//			isGoodPerson = true;
//		}
	}

	/**
	 * Accessor for customer name
	 * @return the Customer's name
	 */
	public String getCustomerName() {
		return name;
	}
	
	//-----------------------MESSAGES-------------------------//
	
	/**
	 * Message sent by the waiter for the customer to follow him/her to the customer's table
	 * @param menu An array of options to choose from
	 * @param waiter The waiter who sent the message
	 */
	public void msgFollowMeToTable(Map<String, Double> menu, Waiter sender){
		this.menu = menu;
		this.waiter = (WaiterRole)sender;
		event = CustomerEvent.MovingToSeat;
		
		//reset money each time a character enters the restaurant
		DecimalFormat df = new DecimalFormat("0.00");
		//make sure the customer has at least the amount owed the restaurant
		money = Double.parseDouble(df.format(rnd.nextDouble()*MAX_AMOUNT_OF_MONEY+moneyOwedToRestaurant));
		
//		if(name.equalsIgnoreCase("poor") || name.equalsIgnoreCase("flake")){
//			money = 0.0;
//		}
		
		stateChanged();
	}
	
	/**
	 * Message called by the gui for when the customer has arrived at the table
	 */
	public void msgArrivedAtTable(){
		event = CustomerEvent.Seated;
		stateChanged();
	}
	
	public void msgArrivedAtLeave(){
		atLeave.release();
		kill();
		Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(waiter.getWorkLocation());
		rest.removeRole(this);
		rest.removeInhabitants();
	}
	
	/**
	 * Message called by the waiter for when he has arrived at the table and is ready
	 * to take the customer's order
	 */
	public void msgWhatWouldYouLike(){
		event = CustomerEvent.WaiterAskedForOrder;
		stateChanged();
	}
	
	/**
	 * Message called by the Host to notify the customer that the restaurant is full
	 * and the customer can make a decision about it
	 */
	public void msgNotifyRestIsFull(){
		event = CustomerEvent.RestaurantIsFull;
		stateChanged();
	}
	
	/**
	 * Message called by the waiter to notify the customer that his food has arrived
	 */
	public void msgHereIsYourFood(){
		event = CustomerEvent.Served;
		stateChanged();
	}
	
	/**
	 * Message called by the waiter when a revised menu is needed by the customer.
	 */
	public void msgHereIsNewMenu(Map<String, Double> menu){
		this.menu = menu;
		event = CustomerEvent.Seated;
		status = CustomerState.Waiting;
		stateChanged();
	}
	
	/**
	 * Private message called by the eating-timer to signal when the customer
	 * has finished eating his/her meal
	 */
	private void msgDone(){
		event = CustomerEvent.NeedsCheck;
		stateChanged();
	}
	
	/**
	 * Message for when the waiter has arrived at the table for the customer to order
	 */
	public void msgWaiterAtTable(){
		waiterAtTable.release();
	}
	
	/**
	 * Message sent by the gui when the customer has arrived at the cashier
	 */
	public void msgArrivedAtCashier(){
		atCashier.release();
	}
	
	/**
	 * Message sent by the gui when the customer has arrived at jail
	 */
	public void msgArrivedAtStart(){
		if(atStart.availablePermits() == 0)
			atStart.release();
	}
	
	/**
	 * Message sent by the WaiterAgent to deliver a check to the customer
	 * @param check the amount owed
	 */
	public void msgHereIsCheck(double check){
		this.check = check;
		event = CustomerEvent.ReceivedCheck;
		stateChanged();
	}
	
	/**
	 * Message sent by the cashier that responds to the customer's attempted
	 * transaction.
	 * @param approved true if the transaction was approved, false otherwise
	 * @param change the change from the transaction, only used if the transaction was approved
	 */
	public void msgPaymentResponse(boolean approved, Double change){
		cashierReply.release();
		String s = approved ? " approved" : " rejected";
		print("Transaction was"+s);
		if(approved){
			event = CustomerEvent.ReadyToLeave;
			money = change.doubleValue();
			moneyOwedToRestaurant = 0.0;
			stateChanged();
			return;
		}else{
			event = CustomerEvent.ReadyToLeave;
			moneyOwedToRestaurant = check - money;
			money = 0.0;
			print("owes "+moneyOwedToRestaurant+" upon next arrival");
			stateChanged();
			return;
		}
	}
	
	//---------------------SCHEDULER--------------------//

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		
		if(event == CustomerEvent.NoEvent){
			if(status == CustomerState.Waiting){
				notifyHostIWantToEat();
				return true;
			}
		}
		
		//if the customer is waiting in line, decide if he/she should stay/leave
		if(event == CustomerEvent.RestaurantIsFull){
			stayOrLeave();
			return true;
		}

		//if the customer has a menu and is seated
		if(menu != null && event == CustomerEvent.Seated){
			if(status == CustomerState.Waiting){
				makeASelection();
				return true;
			}
		}
		
		//if the customer is served and waiting
		if(event == CustomerEvent.Served){
			if(status == CustomerState.Ordered){
				eatFood();
				return true;
			}
		}
		
		//if customer has been asked for the order by a present waiter
		if(event == CustomerEvent.WaiterAskedForOrder){
			if(status == CustomerState.WaitingForWaiterToOrder){
				tellWaiterChoice();
				return true;
			}
		}
		
		//if customer needs the check, ask for it
		if(event == CustomerEvent.NeedsCheck){
			if(status == CustomerState.Eating){
				signalForCheck();
				return true;
			}
		}
		
		//if the customer is ready to leave the restaurant, do so
		if(event == CustomerEvent.ReadyToLeave){
			leaveRestaurant();
			return true;
		}
		
		//if the customer received his check, pay it
		if(event == CustomerEvent.ReceivedCheck){
			payCheck();
			return true;
		}
		
		return false;
	}

	//--------------------ACTIONS-------------------//
	
	/**
	 * Message the Host that I would like to be seated
	 */
	private void notifyHostIWantToEat(){
		DoNotifyHostIWantToEat();
		this.host.msgIWantToEat(this);
		event = CustomerEvent.NotifiedHost;
	}
	
	/**
	 * Private method called by scheduler that has the Customer decide from the menu
	 * what meal he/she would like to order
	 */
	private void makeASelection(){
		DoMakeASelection();
		if(menu.size() == 0){
			System.err.println("MENU IS EMPTY");
			leaveRestaurant();
			return;
		}
		
		//a good person will only order what he/she can afford and 
		//will leave if they cannot afford anything
		if(isGoodPerson){
			ArrayList<String> viableChoices = new ArrayList<String>();
			for(String choice : menu.keySet()){
				//consider options that we can afford
				if(menu.get(choice).doubleValue() <= money){
					viableChoices.add(choice);
				}
			}
			
			//if we can't afford anything, leave the restaurant
			if(viableChoices.isEmpty()){
				signalForCheck();
				return;
			}else{
				//otherwise, select one of the choices we can afford
				choice = viableChoices.get((int)rnd.nextDouble()*viableChoices.size());
			}
		}else{
			//if we don't care if we can afford it, simply select something at random from the full menu
			//int index = Math.abs(rnd.nextInt()) % menu.size();
			int value = Math.abs(rnd.nextInt());
			int denom = menu.size();
			choice = (String) menu.keySet().toArray()[value % denom];
		}
		waiter.msgImReadyToOrder(this);
		waitForWaiter();
		status = CustomerState.WaitingForWaiterToOrder;
	}
	
	/**
	 * Private method called by the scheduler in which a customer
	 * signals his/her waiter to get the check (and thereafter leave)
	 */
	private void signalForCheck(){
		DoSignalForCheck();
		status = CustomerState.AskedForCheck;
		waiter.msgReadyForCheck(this);
	}
	
	/**
	 * Private method called by the scheduler in which a 
	 * customer in line when the restaurant is full decides whether 
	 * he/she will elave or stay
	 */
	private void stayOrLeave(){
		event = CustomerEvent.NoEvent;
		boolean willLeave = rnd.nextBoolean();
		if(willLeave){
			host.msgIWontWait(this);
			print("is leaving because the restaurant is full");
			customerGui.DoExitRestaurant();
		}else{
			print("will wait to enter the restaurant even though it's full");
		}
	}
	
	/**
	 * Private method called by scheduler that has the Customer relay to 
	 * his/her waiter exactly what he/she wants to eat
	 */
	private void tellWaiterChoice(){
		DoTellWaiterChoice();
		waiter.msgHereIsMyChoice(choice, this);
		status = CustomerState.Ordered;
	}
	
	/**
	 * Private method called by the scheduler for the customer to eat --
	 * it sets a timer that then calls a private message within this
	 * CustomerAgent class
	 */
	private void eatFood(){
		DoEatFood();
		status = CustomerState.Eating;
		Timer timer = new Timer(getHungerLevel()*1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				msgDone();
				((Timer) e.getSource()).stop();
			}
			
		});
		timer.start();
	}
	
	/**
	 * Private method called by the scheduler for the customer to leave
	 * the restaurant but not to pay -> because they don't have enough money
	 */
	private void leaveRestaurant(){
		waiter.msgDoneEatingAndPaying(this);
		status = CustomerState.Waiting;
		event = CustomerEvent.NoEvent;
		
		DoLeaveRestaurant();
		try {
			atLeave.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Private method called by the scheduler for the customer to leave
	 * the restaurant and pay
	 */
	private void payCheck(){
		DoGoToCashier();
		event = CustomerEvent.PayingCheck;
		//pay both the amount for this meal, and any prior debts to the restaurant
		cashier.msgHereIsPayment(check+moneyOwedToRestaurant, money, this);
		try {
			cashierReply.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Private method called by the scheduler when the customer tries
	 * to pay for a bill he cannot afford -- this method takes him to the 
	 * corner of the panel -- jail.
	 */
	/*
	private void goToJail(){
		waiter.msgDoneEatingAndPaying(this);
		DoGoToJail();
		customerGui.DoGoToJail();
		try {
			atJail.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = CustomerEvent.InJail;
		pause();
	}*/
	
	//-------------------DO XYZ---------------------//
	
	/*
	private void DoGoToJail(){
		print("Going to jail because of insufficient funds to pay for meal");
	}*/
	
	private void DoNotifyHostIWantToEat(){
		print("Notifying Host that I would like to eat");
	}
	
	private void DoSignalForCheck(){
		print("Signalling for check");
	}
	
	private void DoGoToCashier(){
		print("Going to the Cashier");
		customerGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void DoMakeASelection(){
		print("making a selection from menu: "+ menu);
		//don't continue with selection until the waiter gets to his position
	}
	
	private void DoTellWaiterChoice(){
		print("telling Waiter "+ waiter.getName()+" choice: "+choice);
	}
	
	private void DoEatFood(){
		print(" eating " + choice);
	}
	
	private void DoLeaveRestaurant(){
		DecimalFormat df = new DecimalFormat("0.00");
		String s = moneyOwedToRestaurant != 0.0 ? " (with a debt of $"+df.format(moneyOwedToRestaurant) : "";
		print("leaving restaurant"+s);
		customerGui.DoExitRestaurant();
		
	}

	//-----------------------UTILITIES----------------------//

	public String getName() {
		return this.myPerson.getName();
	}
	
	/**
	 * Utility used to sleep the thread while waiting for a waiter, 
	 * prevents in-method calls that sleep the wrong thread
	 */
	private void waitForWaiter(){
		try {
			waiterAtTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	protected void print(String msg) {
		//change print statement to include if the custoer is a good or bad person
		String goodPerson = isGoodPerson ? " (good person)" : " (bad person)";
		
		StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(goodPerson);
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        System.out.print(sb.toString());
	}

	@Override
	public void setCashier(GenericCashier c) {
		// TODO Auto-generated method stub
		this.cashier = (CashierRole) c;
	}

	@Override
	public void setHost(GenericHost h) {
		// TODO Auto-generated method stub
		this.host = (HostRole) h;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		this.status = CustomerState.Waiting;
		this.event = CustomerEvent.NoEvent;
		stateChanged();
	}
	
	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return Role.RESTAURANT_MIKE_CUSTOMER_ROLE;
	}

}

