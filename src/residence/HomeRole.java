package residence;

import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
import residence.ApartmentManagerRole.AgentEvent;
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
	private boolean leaveHome = false;
	private boolean enterHome = false;
	//private Map <String, Integer> inventory = new HashMap<String, Integer>();
	private List <Item> inventory = new ArrayList<Item>();
	private List <HomeFeature> features = new ArrayList<HomeFeature>(); //includes appliances, toilets, sinks, etc (anything that can break)
	private List <PersonAgent> partyAttendees = new ArrayList<PersonAgent>();
	private Semaphore atKitchen = new Semaphore(0, true);
	private Semaphore atBedroom = new Semaphore(0, true);
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atFrontDoor = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCenter = new Semaphore(0, true);
	Timer timer = new Timer();

	private String name;
	
	public HomeRoleGui gui;

	public enum AgentState
	{DoingNothing, Cooking, Sleeping, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, cooking, eating, asleep, leaving};
	AgentEvent event = AgentEvent.none;

	public HomeRole(PersonAgent myPerson) {
		this.myPerson = myPerson;
		
		gui = new HomeRoleGui(this);
		//myPerson.home.getPanel().addGui(gui);
		
		inventory.add(new Item("Cooking Ingredient",2));
		inventory.add(new Item("Cleaning supply", 2));
		
		features.add(new HomeFeature("Sink"));
	}
	
	public String getNameOfRole() {
		return "HomeRole";
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
		print("Just received " + quantity + " " + itemName + "s.");
		event = AgentEvent.none;
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
	public void msgLeaveBuilding() {
		print("Leaving home.");
		event = AgentEvent.leaving;
		leaveHome = true;
		stateChanged();
	}
	public void msgEnterBuilding() {
		print("Home sweet home!");
		event = AgentEvent.none;
		enterHome = true;
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
		deactivate();
		event = AgentEvent.none;
		atFrontDoor.release();
	}
	public void msgAtTable() {
		atTable.release();
	}
	public void msgAtCenter() {
		atCenter.release();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (leaveHome == true) {
			leaveHome();
			return true;
		}
		if (enterHome == true) {
			enterHome();
			return true;
		}
		if (rentOwed > 0) {
			payRent();
			return true;
		}
		if (state == AgentState.Cooking && event == AgentEvent.none) {
			cook();
			return true;
		}
		/*if (Person.stateOfNourishment == hungry) {
			eat()
			return true;
		}*/
		if (state == AgentState.Sleeping && event == AgentEvent.none) {
			goToSleep();
			return true;
		}
		for(Item i : inventory) {
			if(i.quantity < 2 && state == AgentState.DoingNothing && event == AgentEvent.none) {
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
		event = AgentEvent.cooking;
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToKitchen();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				goEat();
				state = AgentState.DoingNothing;
				event = AgentEvent.eating;
			}
		},
		5000);
		for(Item i : inventory) {
			if(i.name == "Cooking Ingredient") {
				i.quantity--;
			}
		}
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
				print("Done eating.");
				state = AgentState.DoingNothing;
				event = AgentEvent.none;
			}
		},
		5000);
	}
	private void goToMarket (Item item) {
		event = AgentEvent.leaving;
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Low on " + item.name + ". I'm going to the market.");
		gui.DoGoToFrontDoor();
		try {
			atFrontDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myPerson.msgGoToMarket(item.name);
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
	private void goToSleep() {
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				event = AgentEvent.none;
				print("I'm awake!");
				stateChanged();
			}
		},
		5000);
	}
	private void leaveHome() {
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToFrontDoor();
		try {
			atFrontDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leaveHome = false;
	}
	private void enterHome() {
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enterHome = false;
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
