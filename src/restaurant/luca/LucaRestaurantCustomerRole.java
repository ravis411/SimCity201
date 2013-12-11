package restaurant.luca;

import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.gui.luca.CustomerGui;
import restaurant.interfaces.luca.LucaCustomer;
import restaurant.interfaces.luca.LucaWaiter;
import restaurant.test.mock.EventLog;
import Person.Role.Role;
import agent.Constants;

/**
 * Restaurant customer agent.
 */
public class LucaRestaurantCustomerRole extends GenericCustomer implements LucaCustomer{
	private int hungerLevel = 10;        // determines length of meal
	private boolean isHungry;
	private boolean gotMenu;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	static String orderChoice;
	private int tableNum;
	// agent correspondents
	private LucaHostRole host;
	private LucaWaiter waiter;
	private LucaCashierRole cashier;
	private double money;
	private boolean hasEnoughMoney;
	private double moneyOwedToResturant;
	private boolean willWaitforFood;
	private boolean willOrderFoodEvenIfNotEnoughMoney;
	private Random randomx = new Random(System.nanoTime());
	private Map<String, Integer> menu;
	public EventLog log= new EventLog();
	private Semaphore hasLeft = new Semaphore(0,false);

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, WaitingForOrder, Eating, DoneEating, WaitingForCheck,WaitingToFinishEating, PayCashier, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, willNotWaitToBeSeated, followWaiter, seated, waiterAskingForOrder, orderRecieved, doneEating, checkRecieved, donePaying, doneLeaving, ChangeRecieved};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public LucaRestaurantCustomerRole(){
		super();

		isHungry = false;
		gotMenu=false;
		hasEnoughMoney= false;
		menu= new HashMap<String, Integer>();
		moneyOwedToResturant=0;
		willWaitforFood = randomx.nextBoolean();
		
		
	}

	/**
	 * hack to establish connection to Host agent.
	 */

	public void setWaiter(LucaWaiter waiter) {
		this.waiter = waiter;
	}


	// Messages

	public void gotHungry() {//from animation
		Random random = new Random(System.nanoTime());
		money = myPerson.getMoney() + moneyOwedToResturant; //assigns a random amount of money between 0 and 29 to bring to restaurant + any amount it already owes the restaurant
		print("I'm hungry and I have $" + money + " of this money, $" + moneyOwedToResturant + " are brought to make sure I have atleast enough to pay for my last debt.");
		state = AgentState.DoingNothing;
		event = AgentEvent.gotHungry;
		setHungry(true);
		stateChanged();
	}
	public void msgYouAreOffWaitList() {//for customers who do not wait to be seated
		event = AgentEvent.willNotWaitToBeSeated;		
		state = AgentState.WaitingInRestaurant;
		stateChanged();
	}

	public void msgSitAtTable(int t, LucaWaiter w) {
		print("Received msgSitAtTable");
		setWaiter(w);
		event = AgentEvent.followWaiter;
		tableNum=t;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgHereIsAMenu(Map<String, Integer> menuUnmodifiable) {
		gotMenu=true;
		menu.putAll(menuUnmodifiable); //stores the passed menu in a local copy
		stateChanged();
		
	}
	public void msgCustomerWhatWouldYouLike()
	{
		event = AgentEvent.waiterAskingForOrder;
		stateChanged();

	}
	
	public void msgCustomerWhatIsYourSecondChoice(String choice) {
		state = AgentState.ReadyToOrder;
		event = AgentEvent.waiterAskingForOrder;
		menu.remove(choice);
		stateChanged();
	}

	public void msgCustomerHereIsYourChoice(String choice){
		event = AgentEvent.orderRecieved;
		orderChoice= choice;
		stateChanged();
	}
	public void msgCustomerHereIsYourCheck(double customersTab) {
		event = AgentEvent.checkRecieved;
		moneyOwedToResturant= customersTab;
		stateChanged();
	}

	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	public void msgCustomerHereIsChange(double change, double moneyIOwe) {
		money = change;
		moneyOwedToResturant = moneyIOwe;
		print(this + " has recieved change, $" +change);
		event = AgentEvent.ChangeRecieved;
		stateChanged();
		
	}
	public void msgLeftRestaurant() {//from animation
		hasLeft.release();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.willNotWaitToBeSeated ){
			state = AgentState.DoingNothing;
			leaveDoesNotWantToWait();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated && gotMenu){
			state = AgentState.ReadyToOrder;
			readyToOrder();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.waiterAskingForOrder){
			state = AgentState.WaitingForOrder;
			giveOrderAndWaitForFood();
			return true;
		}
		if (state == AgentState.WaitingForOrder  && event == AgentEvent.orderRecieved){
			state = AgentState.Eating;
			EatFood();
			return true;			
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.checkRecieved){
			state = AgentState.WaitingToFinishEating;
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.checkRecieved){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.WaitingToFinishEating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.PayCashier;
			payCashier();
			return true;
		}
		if (state == AgentState.PayCashier && event == AgentEvent.ChangeRecieved){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		print("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		print("Being seated. Going to table");
		customerGui.showMyOrderInAnimation("", "");
		customerGui.DoGoToSeat(1,tableNum);//hack; only one table
	}
	
	private void readyToOrder(){
		print(this.toString() + " ready to order");
		
		waiter.msgReadyToOrder(this);
		
	}
	private void giveOrderAndWaitForFood(){
		String randomKey;
		willOrderFoodEvenIfNotEnoughMoney= randomx.nextBoolean();
		print(this + " will order even if they don't have the money: " + willOrderFoodEvenIfNotEnoughMoney);
		hasEnoughMoney= false;
		Random random = new Random(System.nanoTime());
		List<String> keys = Collections.synchronizedList(new ArrayList<String>(menu.keySet()));
		
				if(keys.size() == 0)
				{
					print("You gave me a menu with nothing on it! I'm going home!");
					state = AgentState.Leaving;
					waiter.msgHereIsMyChoice(this, "nothing");//just to make the semaphore release so the waiter will leave table
					leaveTable();
				}
				else{
						for(Integer price : menu.values()){
							if ((money >= price)|| willOrderFoodEvenIfNotEnoughMoney){ //checks customer has at least enough money for one item on menu...unless customer will order anyway
								do{
									randomKey = keys.get( random.nextInt(keys.size()) );
									
									}while((menu.get(randomKey) > money) && !willOrderFoodEvenIfNotEnoughMoney);//Makes a new random choice from menu be picked if they can't afford it
								orderChoice =randomKey;																//....unless willOrderFoodEvenIfNotEnoughMoney		
								print(this.toString() + " Says he/she wants item " + orderChoice);
								customerGui.showMyOrderInAnimation(orderChoice, "?");
								waiter.msgHereIsMyChoice(this, orderChoice);
								hasEnoughMoney= true;
								break;
							}
						}
					
						if (!hasEnoughMoney){
							print("I don't have enough money for anything. I'm going home.");
							state = AgentState.Leaving;
							waiter.msgHereIsMyChoice(this, "nothing");//just to make the semaphore release so the waiter will leave table
							leaveTable();
						}
				}
	}

	private void EatFood() {
		customerGui.showMyOrderInAnimation(orderChoice, "");
		waiter.msgWaiterReadyForCheck(this);
		print("Thank you for my " + orderChoice + ", can I have my check now?");
		print("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating, Order number=" + orderChoice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		getHungerLevel() * Constants.SECOND);//getHungerLevel() * 1000);//how long to wait before running task
	}
	

	private void leaveTable() {
		customerGui.showMyOrderInAnimation("", "");
		print("Leaving and paying cashier.");
		waiter.msgDoneLeavingTable(this);
		isHungry = false;
		print(this + " is not hungry anymore since he is done eating...or just can't afford anything");
		customerGui.DoExitRestaurant();
		try {
			hasLeft.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deactivate();
	}
	private void leaveDoesNotWantToWait() {
		isHungry = false;
		print(this + "apparently is not hungry anymore since he is doesn't want to wait to be seated.");
		customerGui.DoExitRestaurant();
		try {
			hasLeft.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deactivate();
	}

	private void payCashier() {
		if (moneyOwedToResturant> money){ //if customer doesnt have enough money tell cashier to leave what is owed the money on tab for next time.
			print("Sorry I " + this + " don't have Enough money add what I owe to my tab next time.");
			
		}
		cashier.msgCashierPayForOrder(money, this);

		
		
	}

	// Accessors, etc.
	public String getName() {
		return myPerson.getName();
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

	public boolean isHungry() {
		return isHungry;
	}

	public void setHungry(boolean isHungry) {
		this.isHungry = isHungry;
	}

	public boolean getHasEnoughMoney(){
		return hasEnoughMoney;
	}
	public boolean getWillWaitforFood(){
		return willWaitforFood;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return Role.RESTAURANT_LUCA_CUSTOMER_ROLE;
	}

	@Override
	public void setCashier(GenericCashier c) {
		// TODO Auto-generated method stub
		this.cashier = (LucaCashierRole) c;
		
	}

	@Override
	public void setHost(GenericHost h) {
		this.host = (LucaHostRole) h;
				
	}








}

