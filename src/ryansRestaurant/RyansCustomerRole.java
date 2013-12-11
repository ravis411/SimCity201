package ryansRestaurant;

import ryansRestaurant.RyansHostRole.Table;
import ryansRestaurant.gui.CustomerGui;
import ryansRestaurant.gui.RestaurantGui;
import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansHost;
import ryansRestaurant.interfaces.RyansWaiter;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import agent.Agent;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class RyansCustomerRole extends GenericCustomer implements RyansCustomer {
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
	private RyansHost host;
	private RyansWaiter waiter;
	private RyansCashier cashier;
	RyansCustomer cust = this;

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
	 * Constructor for RyansCustomerRole class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RyansCustomerRole(){
		super();
		
//		this.name = myPerson.getName();
//		//hack for not checking wallet
//		if( name.toLowerCase().contains("flake") )
//				flake = true;
//		//hack for not being patient
//		if((name.toLowerCase().contains("leave")))
//			leaves = true;
//		
//		//hack to set initial wallet amount
//		if(name.contains("$")) {
//			int pos = name.lastIndexOf("$");
//			double wallet;
//			try {
//				wallet = Double.parseDouble(name.substring(pos + 1));
//				this.wallet = wallet;
//				print("I have $" + this.wallet);
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			}
//		}
		
		
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgIntroduceWaiter(RyansWaiter waiter) {
		this.waiter = waiter;
		stateChanged();
	}

	public void msgSitAtTable(List<String> menu, RyansCashier cashier) {
		event = AgentEvent.followWaiter;
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Received msgSitAtTable");
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
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "My bill is $" + total);
		readyToPay = true;
		stateChanged();
	}
	
	public void msgHereIsChange(double change) {
		event = AgentEvent.donePaying;
		if(change > 0){
			wallet += change;
		}
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "My change is: " + change);
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		//AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "At seat.");
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
	
	
	public boolean pickAndExecuteAction() {
		//	RyansCustomerRole is a finite state machine

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
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Finished Leaving.");
			activity = "";
			super.kill();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
	//	Do("Going to ryansRestaurant");
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
							AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "DECIDED TO LEAVE.");
							activity = "Leaving.";
							customerGui.DoImpatientExitRestaurant();
							stateChanged();
						}
					}
				}, 3000);
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Waiting In Restaurant.");
	}
	
	private void SitDown() {
		//Do("Being seated. Going to table");
		activity = "Sitting down.";
		customerGui.DoGoToSeat();
	}
	
	private void ChooseOrder() {
		//Do("Picking an order.");
		activity = "Browsing menu";
		
		
		
		
		timer.schedule(new TimerTask() {
			
			public void run() {

				if(menu == null || menu.isEmpty()) {
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "There's nothing to eat?, I'm outa here!");
					activity = "There's no food, I'm out of here!";
					state = AgentState.Paying;
					event = AgentEvent.donePaying;
					return;	}
				
				
				choice = menu.get( (new Random().nextInt(menu.size()))  );

				Map<String, Double> prices = ((RyansCashierRole)cashier).getMenuPrices();

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
						AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "I can't afford anything.");
						stateChanged();
						return;
					}
				}
												
				
				//hack to have customer choose his name
				for(String c : menu) {
					if(getName().toLowerCase().contains(c.toLowerCase())) {
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
		//Do("May I please have: " + choice);
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
				AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Done eating, " + choice);
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

	
	
	
	
	
	
	
	@Override
	public void setCashier(GenericCashier c) {
		this.cashier = (RyansCashier) c;
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_RYAN_CUSTOMER_ROLE;
	}

	
	@Override
	public void setHost(GenericHost h) {
		this.host = (RyansHost) h;
	}
	
	@Override
	public void kill() {
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Killed called? But I'm not done eating?");
		//super.kill();
	}

	
}

