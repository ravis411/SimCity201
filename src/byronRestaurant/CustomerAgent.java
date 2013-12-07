package byronRestaurant;

import agent.Agent;
import byronRestaurant.WaiterAgent;
import byronRestaurant.gui.CustomerGui;
import byronRestaurant.gui.RestaurantGui;
import byronRestaurant.interfaces.Customer;

import java.util.*;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent {
	private String name;
	private double money = (Math.random() * 50);
	private double cost = 0;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private List<String> menu = new ArrayList<String>();
	// agent correspondents
	private WaiterAgent waiter;
	private HostAgent host;
	private CashierAgent cashier;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, EnteringRestaurant, WaitingInRestaurant, Seated, WaiterCalled, WaitingForFood, Eating, requestingCheck, GoingToCashier, Paying};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, atWaitingArea, followingWaiter, beingSeated, decidedChoice, takeOrder, foodDelivered, doneEating, gettingCheck, atCashier, paid};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host){
		this.host = host;
	}
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
	public void setWaiter(WaiterAgent waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void setHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgFollowMeToTable(WaiterAgent w, int n, List<String> m){
		setWaiter(w);
		print("Going to seat");
		event = AgentEvent.followingWaiter;
		customerGui.DoGoToSeat(n);//hack; only one table
		for (String m1 : m){
			menu.add(m1);
		}
		stateChanged();
	}
	public void msgAtWaitingArea(){
		event = AgentEvent.atWaitingArea;
		stateChanged();
	}
	
	public void msgAtTable(){
		event = AgentEvent.beingSeated;
		stateChanged();
	}

	public void msgAtCashier(){
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void msgDecided(){
		event = AgentEvent.decidedChoice;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		event = AgentEvent.takeOrder;
		stateChanged();
	}
	
	public void msgHereIsYourFood(){
		event = AgentEvent.foodDelivered;
		stateChanged();
	}
	
	public void msgDoneEating(){
		event = AgentEvent.doneEating;
		menu.clear();
		stateChanged();
	}
	
	public void msgHereIsCheck(double w){
		event = AgentEvent.gettingCheck;
		cost = w;
		stateChanged();
	}
	
	public void msgHereIsChange(double w){
		event = AgentEvent.paid;
		money = w;
		stateChanged();
		
	}
	
	// Non-normative messages
	
	public void msgNoMoreChoice(List<String> m){
		menu.clear();
		for (String m1 : m){
			menu.add(m1);
		}
		orderNewFood();		
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.EnteringRestaurant;
			enterRestaurant();
			return true;
		}
		if (state==AgentState.EnteringRestaurant && event == AgentEvent.atWaitingArea){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.beingSeated){
			state = AgentState.Seated;
			makeChoice();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.decidedChoice){
			state = AgentState.WaiterCalled;
			callWaiter();
			return true;
		}

		if (state == AgentState.WaiterCalled && event == AgentEvent.takeOrder){
			state = AgentState.WaitingForFood;
			orderFood();
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.foodDelivered){
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.requestingCheck;
			doneEating();
			return true;
		}
		if (state == AgentState.requestingCheck && event == AgentEvent.gettingCheck){
			state = AgentState.GoingToCashier;
			goToCashier();
			return true;
		}
		if (state == AgentState.GoingToCashier && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			payForFood();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paid){
			state = AgentState.DoingNothing;
			leaveRestaurant();
			return true;
		}
		return false;
	}

	// Actions
	private void enterRestaurant(){
		Do("Entering byronRestaurant");
		customerGui.DoGoToWaitingArea();		
	}
	
	private void goToRestaurant() {
		Do("Going to byronRestaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
 
	private void makeChoice(){
		print("Deciding menu choice");
		timer.schedule(new TimerTask() {
			public void run() {
				msgDecided();
			}}, 
			3000);
	}
	
	private void callWaiter(){
		print("I decided!");
		waiter.msgReadyToOrder(this);
	}
	
	private void orderFood(){
		String choice = menu.get((int)(Math.random()*(menu.size())));
		if (this.name.equals("Steak")){
			System.out.println("Exception: Mandatory Steak Order");
			choice = menu.get(0);
		}
		print ("Ordering the " + choice);
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void orderNewFood(){
		String choice = menu.get((int)(Math.random()*(menu.size())));
		print ("Ordering the " + choice);
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void eatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				msgDoneEating();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void doneEating(){
		waiter.msgDoneEating(this);
	}
	
	private void goToCashier(){
		Do("Going to cashier.");
		waiter.msgLeaving(this);
		customerGui.DoGoToLobby();
	}
	
	private void payForFood(){
		Do("Paying for food.");
		cashier.msgPayForFood(cost, money, this);
		
	}
	
	private void leaveRestaurant() {
		Do("Leaving.");
		cost = 0;
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

