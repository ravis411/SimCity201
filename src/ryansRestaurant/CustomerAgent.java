package ryansRestaurant;

import ryansRestaurant.HostAgent.Table;
import ryansRestaurant.gui.CustomerGui;
import ryansRestaurant.gui.RestaurantGui;
import ryansRestaurant.interfaces.Cashier;
import ryansRestaurant.interfaces.Customer;
import ryansRestaurant.interfaces.Host;
import ryansRestaurant.interfaces.Waiter;
import agent.Agent;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	//private Semaphore sem = new Semaphore(0, true);
	private CustomerGui customerGui;

	String choice; //the choice to eat
	List<String> menu; // to hold the menu
	String activity = new String(); //for the gui
	double totalBill;	//to hold how much needs to be paid
	double wallet = 16;	// the customers wallet or how much money he currently has.
	
	// agent correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;
	Customer cust = this;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, PickingAnOrder, WaitingForOrder, Eating, goingToPay, Paying, Leaving, WaitPatientlyInRestaurant};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, cantPayForAnything, readyToOrder, readyToEat, doneEating, atCashier, paid, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	private boolean readyToPay = false;//set to true when customer receives his bill
	protected boolean flake = false;
	protected boolean leaves = false;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		
		
		//hack for not checking wallet
		if( name.toLowerCase().contains("flake") )
				flake = true;
		//hack for not being patient
		if((name.toLowerCase().contains("leave")))
			leaves = true;
		
		//hack to set initial wallet amount
		if(name.contains("$")) {
			int pos = name.lastIndexOf("$");
			double wallet;
			try {
				wallet = Double.parseDouble(name.substring(pos + 1));
				this.wallet = wallet;
				print("I have $" + this.wallet);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Host host) {
		this.host = host;
	}
	
	

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		//print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgIntroduceWaiter(Waiter waiter) {
		this.waiter = waiter;
		stateChanged();
	}

	public void msgSitAtTable(List<String> menu, Cashier cashier) {
		event = AgentEvent.followWaiter;
		print("Received msgSitAtTable");
		this.menu = menu;
		this.cashier = cashier;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.readyToOrder;
		stateChanged();
	}
	
	public void msgOutOfChoice(List<String> menu) {
		event = AgentEvent.seated;
		state = AgentState.BeingSeated;
		this.menu = menu;
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		event = AgentEvent.readyToEat;
		activity = "Thanks!";
		timer.schedule(new TimerTask() {
			
			public void run() {
				stateChanged();
			}
		}, 3000);
		//print("Glad My food is here!");
	}
	
	public void msgHereIsCheck(double total) {
		this.totalBill = total;
		print("My bill is $" + total);
		readyToPay = true;
		stateChanged();
	}
	
	public void msgHereIsChange(double change) {
		event = AgentEvent.donePaying;
		if(change > 0){
			wallet += change;
		}
		print("My change is: " + change);
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		print("At seat.");
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	public void msgAnimationFinishedGoToCashier() {
		event = AgentEvent.atCashier;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitPatientlyInRestaurant;
			goToRestaurant();
			return true;
		}
		
		//non-norm customer still not seated...
		if(state == AgentState.WaitPatientlyInRestaurant && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			waitInRestaurant();
			return true;//return false;//want to stop the scheduler
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.PickingAnOrder;
			ChooseOrder();
			return true;
		}
		
		//non-norm
		if (state == AgentState.PickingAnOrder && event == AgentEvent.cantPayForAnything){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		
		if (state == AgentState.PickingAnOrder && event == AgentEvent.readyToOrder){
			state = AgentState.WaitingForOrder;
			Order();
			return true;
		}
		
		if (state == AgentState.WaitingForOrder && event == AgentEvent.readyToEat){
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating && readyToPay){
			state = AgentState.goingToPay;
			goToPay();
			return true;
		}
		
		if (state == AgentState.goingToPay && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			payCashier();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			//no action
			print("Finished Leaving.");
			activity = "";
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to ryansRestaurant");
		customerGui.DoEnterRestaurant();
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void waitInRestaurant() {
		//if(waiter == null) 
		{
			customerGui.DoWaitInRestaurant();
			
			if(leaves) {
				timer.schedule(new TimerTask() {

					public void run() {
						if(waiter == null) {
							host.msgIWantToLeave(cust);
							state = AgentState.Leaving;
							print("DECIDED TO LEAVE.");
							activity = "Leaving.";
							customerGui.DoImpatientExitRestaurant();
							stateChanged();
						}
					}
				}, 3000);
			}
		}
		print("Waiting In Restaurant.");
	}
	
	private void SitDown() {
		Do("Being seated. Going to table");
		activity = "Sitting down.";
		customerGui.DoGoToSeat();
	}
	
	private void ChooseOrder() {
		//Do("Picking an order.");
		activity = "Browsing menu";
		
		
		
		
		timer.schedule(new TimerTask() {
			
			public void run() {

				if(menu == null || menu.isEmpty()) {
					print("There's nothing to eat?, I'm outa here!");
					activity = "There's no food, I'm out of here!";
					state = AgentState.Paying;
					event = AgentEvent.donePaying;
					return;	}
				
				
				choice = menu.get( (new Random().nextInt(menu.size()))  );

				Map<String, Double> prices = ((CashierAgent)cashier).getMenuPrices();

				//This if block checks to make sure customer can afford menu item. If they aren't a flake.
				if(!flake && wallet < prices.get(choice) ) {
					for(String s : menu) {
						if(wallet >= prices.get(s)) {
							choice = s;
							break;
						}
					}
					if(wallet < prices.get(choice)) {
						//NOT ENOUGH MONEY TO PAY FOR ANYTHING LEAVE
						event = AgentEvent.cantPayForAnything;
						activity = "I can't afford anything.";
						print("I can't afford anything.");
						stateChanged();
						return;
					}
				}
												
				
				//hack to have customer choose his name
				for(String c : menu) {
					if(name.toLowerCase().contains(c.toLowerCase())) {
						choice = c;
					}
				}
				activity = "I know what to eat!";
				waiter.msgReadyToOrder(cust);
			}
		},
		4000);
	}

	private void Order() {
		Do("May I please have: " + choice);
		activity = "" + choice;
		
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
		}
		
		activity = "Thanks.";
		timer.schedule(new TimerTask() {
			
			public void run() {
				activity = "" + choice +"?";
			}
		}, 2000);
		
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void EatFood() {
		//Do("Eating Food");
		activity = "eating\n\n" + choice;
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
				print("Done eating, " + choice);
				activity = "Done.";
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
				
			}
		},
		6000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void goToPay() {
		activity = "Going to pay.";
		customerGui.DoGoToCashier();
				
	}
	
	
	private void payCashier() {
		
		
		activity = "Paying cashier $" + wallet + "  /$" + totalBill;
		
		timer.schedule(new TimerTask() {
			
			public void run() {
				event = AgentEvent.paid;
				
				cashier.msgHereIsPayment(wallet, cust);
				wallet = 0;
				readyToPay = false;
				stateChanged();
				
			}
		}, 5000);
		
	}

	private void leaveTable() {
		//Do("Leaving.");
		activity = "Leaving.";
		waiter.msgDoneEatingLeaving(this);
		customerGui.DoExitRestaurant();
		waiter = null;
	}

	// Accessors, etc.    

	public String getName() {
		return name;
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

	public String getActivity() {
		return activity;
	}
	
	public double getWallet() {
		return wallet;
	}
	
	public void setWallet(double money) {
		wallet = money;
	}
	
	public boolean getFlake() {
		return flake;	}
	public void setFlake(boolean flake) {
		this.flake = flake;	}
	public boolean getLeaves() {
		return this.leaves; }
	public void setLeaves(boolean leave) {
		this.leaves = leave;}

	
}
