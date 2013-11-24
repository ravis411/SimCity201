package residence;

import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
import residence.gui.HomeRoleGui;
import residence.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Home Role
 */

public class HomeRole extends Role implements Home {
	private ApartmentManager landlord;
	private int rentOwed = 0;
	private int aptNumber = 0;
	//private Map <String, Integer> inventory = new HashMap<String, Integer>();
	private List <Item> inventory = new ArrayList<Item>();
	private List <HomeFeature> features = new ArrayList<HomeFeature>(); //includes appliances, toilets, sinks, etc (anything that can break)
	private Semaphore atKitchen = new Semaphore(0, true);
	private Semaphore atBedroom = new Semaphore(0, true);
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atFrontDoor = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	Timer timer = new Timer();

	private String name;
	
	public HomeRoleGui gui = null;

	public enum AgentState
	{DoingNothing, Cooking, Sleeping, Leaving, Eating};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, doneCooking, asleep};
	AgentEvent event = AgentEvent.none;

	public HomeRole(String name) {
		//super();
		this.name = name;
		
		inventory.add(new Item("Cooking Ingredients",2));
		inventory.add(new Item("Cleaning supplies", 2));
		
		features.add(new HomeFeature("Sink"));
	}
	
	public String getName() {
		return name;
	}
	
	public boolean canGoGetFood() {
		return true;
	}
	
	// Messages
	
	public void msgRentDue (int amount) {
		print("I just got charged rent.");
		rentOwed = amount;
		stateChanged();
	}
	public void msgTired() { //called by timer
		print("I'm tired.");
		state = AgentState.Sleeping;
		stateChanged();
	}
	public void msgRestockItem (String itemName, int quantity) {
		for(Item i : inventory) {
			if(i.name == itemName) {
				i.quantity = i.quantity + quantity;
			}
		}
		stateChanged();
	}
	public void msgFixedFeature (String name) {
		for (HomeFeature hf : features) {
			if (name == hf.name) {
				hf.working = true;
			}
		}
		stateChanged();
	}
	public void msgMakeFood() {
		print("I'm eating at home.");
		state = AgentState.Cooking;
		stateChanged();
	}
	
	public void msgAtKitchen() {
		atKitchen.release();
	}
	public void msgAtBedroom() {
		atBedroom.release();
	}
	public void msgAtBed() {
		atBed.release();
	}
	public void msgAtFrontDoor() {
		atFrontDoor.release();
	}
	public void msgAtTable() {
		atTable.release();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (rentOwed > 0) {
			payRent();
			return true;
		}
		if (state == AgentState.Cooking && event != AgentEvent.doneCooking) {
			cook();
			return true;
		}
		/*if (Person.stateOfNourishment == hungry) {
			eat()
			return true;
		}*/
		if (state == AgentState.Sleeping && event != AgentEvent.asleep) {
			goToSleep();
			return true;
		}
		for(Item i : inventory) {
			if(i.quantity < 2 && state == AgentState.DoingNothing) {
				goToMarket(i);
			}
			return true;
		}
		for (HomeFeature hf : features) {
			if(!hf.working) {
				fileWorkOrder(hf);
				return true;
			}
		}
		return false;
	}
	

	// Actions

	private void cook() {
		gui.DoGoToKitchen();
		event = AgentEvent.doneCooking;
		for(Item i : inventory) {
			if(i.name == "Cooking Ingredients") {
				i.quantity--;
			}
		}
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				goEat();
				state = AgentState.Eating;
			}
		},
		5000);
	}
	private void goEat() {
		print("Dinner's ready! Eating.");
		gui.DoGoToTable();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				state = AgentState.DoingNothing;
			}
		},
		5000);
	}
	private void goToMarket (Item item) {
		state = AgentState.Leaving;
		print("Low on " + item.name + ". I'm going to the market.");
		gui.DoGoToFrontDoor();
		try {
			atFrontDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void fileWorkOrder (HomeFeature brokenFeature) {
		landlord.msgBrokenFeature(brokenFeature.name, this);
	}
	private void payRent () {
		if(myPerson.getMoney() >= rentOwed) {
			landlord.msgRentPaid (this, rentOwed);
			myPerson.setMoney(myPerson.getMoney()-rentOwed);
			rentOwed = 0;
			print("Paid my rent. I have $" + myPerson.getMoney() + " left.");
		}
		else {
			//do nothing
		}
	}
	private void eat() { //chooses whether person cooks at home or goes out to a restaurant
		
	}
	private void goToSleep() {
		gui.DoGoToBedroom();
		try {
			atBedroom.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToBed();
		event = AgentEvent.asleep;
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				state = AgentState.DoingNothing;
				print("Getting up!");
				stateChanged();
			}
		},
		5000);
	}

	//utilities
	
	public void setLandlord (ApartmentManager role) {
		this.landlord = role;
	}
	
	private class HomeFeature {
		String name;
		boolean working;
		
		HomeFeature(String name) {
			this.name = name;
			working = true;
		}
	}
	private class Item {
		String name;
		int quantity;
		
		Item(String name, int quantity) {
			this.name = name;
			this.quantity = quantity;
		}
	}
}
