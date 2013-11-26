package residence;

import MarketEmployee.MarketManagerRole;
import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
import building.BuildingList;
import residence.ApartmentManagerRole.AgentEvent;
import residence.gui.HomeRoleGui;
import residence.interfaces.*;
import trace.AlertLog;
import trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Home Role
 */

public class HomeRole extends Role implements Home {
	private ApartmentManager landlord;
	private int rentOwed = 0;
	private int aptNumber = 0;

	public boolean leaveHome = false;
	public boolean enterHome = false;
	public boolean callMarket = false;

	
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
	
	Calendar rsvpDate;
	Calendar partyDate;

	private String name;
	
	public HomeRoleGui gui;

	public enum AgentState
	{DoingNothing, Cooking, Sleeping, Leaving};
	public AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, cooking, eating, asleep, leaving};
	public AgentEvent event = AgentEvent.none;
	
	public enum PartyState
	{none, sendInvites, setUp, host};
	public PartyState partyState = PartyState.none;

	public HomeRole(PersonAgent myPerson) {
		this.myPerson = myPerson;
		
//		gui = new HomeRoleGui(this);
//		myPerson.home.getPanel().addGui(gui);
		
		inventory.add(new Item("Cooking Ingredient",2));
		inventory.add(new Item("Cleaning supply", 2));
		
		features.add(new HomeFeature("Sink"));
	}
	
	public void setGui(HomeRoleGui gui){
		this.gui = gui;
	}
	
	public String getNameOfRole() {
		return "HomeRole";
	}
	public List<Item> getInventory(){
		return inventory;
	}
	public List<HomeFeature> getHomeFeatures(){
		return features;
	}
	public boolean canGoGetFood() {
		return true;
	}
	
	// Messages
	
	public void msgRentDue (int amount) {

		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I just got charged rent.");
		setRentOwed(amount);

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
		if (getRentOwed() > 0) {
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
		if(partyState == PartyState.sendInvites) {
			sendOutInvites();
			return true;
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
		if(callMarket == true) {
			gui.DoGoToCenter();
			try {
				atCenter.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			print("Low on " + item.name + ". I'm calling the market.");
			List<Role> inhabitants = BuildingList.findBuildingWithName("Market 1").getInhabitants();
			for(Role r : inhabitants) {
				if (r.getNameOfRole() == "MARKET_MANAGER_ROLE") {
					MarketManagerRole mr = (MarketManagerRole) r;
					mr.msgMarketManagerFoodOrder("Cooking Ingredient", 5, this);
				}
			}
		}
		else if(callMarket == false) {
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
			callMarket = true;
		}
	}
	private void fileWorkOrder (HomeFeature brokenFeature) {
		landlord.msgBrokenFeature(brokenFeature.name, this);
	}
	private void payRent () {

		if(myPerson.getMoney() >= getRentOwed()) {
			landlord.msgRentPaid (this, getRentOwed());
			myPerson.setMoney(myPerson.getMoney()-getRentOwed());
			setRentOwed(0);
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Paid my rent. I have $" + myPerson.getMoney() + " left.");


		if(myPerson.getMoney() >= rentOwed) {
			landlord.msgRentPaid (this, rentOwed);
			myPerson.setMoney(myPerson.getMoney()-rentOwed);
			rentOwed = 0;
			print("Paid my rent. I have $" + myPerson.getMoney() + " left.");
		}
		else {
			//do nothing
		}}
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
	private void sendOutInvites() {
		rsvpDate = myPerson.realTime;
		rsvpDate.add(Calendar.DAY_OF_MONTH, 1);
		System.out.println(rsvpDate.toString());
		partyDate = myPerson.realTime;
		partyDate.add(Calendar.DAY_OF_MONTH, 3);
		System.out.println(partyDate.toString());
		for(int i=0; i<4; i++) {
			myPerson.friends.get(i).msgPartyInvitation(myPerson, rsvpDate, partyDate);
		}
	}

	//utilities
	
	public void setLandlord (ApartmentManager role) {
		this.landlord = role;
	}
	
	public int getRentOwed() {
		return rentOwed;
	}

	public void setRentOwed(int rentOwed) {
		this.rentOwed = rentOwed;
	}

	public class HomeFeature {
		public String name;
		public boolean working;
		
		HomeFeature(String name) {
			this.name = name;
			working = true;
		}
	}
	public class Item {
		public String name;
		public int quantity;
		
		Item(String name, int quantity) {
			this.name = name;
			this.quantity = quantity;
		}
	}
}
