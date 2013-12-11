package residence;

import interfaces.Home;
import interfaces.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import residence.gui.HomeRoleGui;
import trace.AlertLog;
import trace.AlertTag;
import util.MasterTime;
import MarketEmployee.MarketManagerRole;
import Person.PersonAgent;
import Person.Role.Role;
import building.BuildingList;

/**
 * Home Role
 */

public class HomeRole extends Role implements Home {
	protected double rentOwed = 0;

	public boolean leaveHome = false;
	public boolean enterHome = false;
	public boolean callMarket = false;

	private List <Item> inventory = new ArrayList<Item>();
	private List <HomeFeature> features = new ArrayList<HomeFeature>(); //includes appliances, toilets, sinks, etc (anything that can break)
	public List <Person> partyAttendees = new ArrayList<Person>();
	public List <Person> partyInvitees= new ArrayList<Person>();
	private Semaphore atKitchen = new Semaphore(0, true);
	private Semaphore atBedroom = new Semaphore(0, true);
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atFrontDoor = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCenter = new Semaphore(0, true);
	Timer timer = new Timer();
	
	public Calendar rsvpDate = Calendar.getInstance();
	public Calendar partyDate = Calendar.getInstance();
	public Calendar featureRepairDate = Calendar.getInstance();
	
	public HomeRoleGui gui;

	public enum AgentState
	{DoingNothing, Cooking, Sleeping, Leaving};
	public AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, cooking, eating, asleep, leaving};
	public AgentEvent event = AgentEvent.none;
	
	public enum PartyState
	{none, sendInvites, resendInvites, setUp, host, cleanUp};
	public PartyState partyState = PartyState.none;

	public HomeRole(PersonAgent myPerson) {
		this.myPerson = myPerson;
		
//		gui = new HomeRoleGui(this);
//		myPerson.home.getPanel().addGui(gui);
		
		inventory.add(new Item("Steak",2));
		inventory.add(new Item("Chicken", 2));
		inventory.add(new Item("Burger", 2));
		
		features.add(new HomeFeature("Sink"));
		features.add(new HomeFeature("Stove"));
		features.add(new HomeFeature("Refrigerator"));
	}
	
	public void deactivate(){
		msgLeaveBuilding();
		super.deactivate();
	}
	
	public void setGui(HomeRoleGui gui){
		this.gui = gui;
	}
	
	public String getNameOfRole() {
		return Role.HOME_ROLE;
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
	
	public void msgRentDue (double amount, int date) {

		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I just got charged rent for the " + date + "th.");
		setRentOwed(amount);
		
		rentOwed = amount;

		stateChanged();
	}
	public void msgTired() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I'm tired.");
		state = AgentState.Sleeping;
		stateChanged();
	}
	public void msgRestockItem (String itemName, int quantity) {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Just received " + quantity + " " + itemName + "s.");
		event = AgentEvent.none;
		for(Item i : inventory) {
			if(i.name == itemName) {
				i.quantity = i.quantity + quantity;
			}
		}
		stateChanged();
	}
	public void msgFixedFeature () {
		String name = "My broken appliance";
		for (HomeFeature hf : features) {
			if (!hf.working) {
				hf.working = true;
				hf.workOrderFiled = false;
				name = hf.name;
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), name + " is fixed.");
		stateChanged();
	}
	public void msgMakeFood() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I'm eating at home.");
		state = AgentState.Cooking;
		stateChanged();
	}
	public void msgLeaveBuilding() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Leaving home.");
		event = AgentEvent.leaving;
		leaveHome = true;
		stateChanged();
	}
	public void msgEnterBuilding() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Home sweet home!");
		event = AgentEvent.none;
		enterHome = true;
		stateChanged();
	}
	public void msgThrowParty() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I'm planning a party!");
		partyState = PartyState.sendInvites;
		stateChanged();
	}
	public void msgResendInvites() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Resending invites to those who didn't RSVP.");
		partyState = PartyState.resendInvites;
		stateChanged();
	}
	public void msgHostParty() {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "There's a party at my house soon!");
		partyState = PartyState.host;
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
		kill();
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
		if(partyInvitees.size() > 0 && partyState == PartyState.resendInvites) {
			resendInvites();
			partyState=PartyState.none;
			return true;
		}
		if(partyState == PartyState.host) {
			hostParty();
			return true;
		}
		if(partyState == PartyState.sendInvites) {
			sendOutInvites();
			return true;
		}
		if(partyState != PartyState.host) {
			for (HomeFeature hf : features) {
				if(!hf.working && !hf.workOrderFiled) {
					fileWorkOrder(hf);
					return true;
				}
			}
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
			if (state == AgentState.Sleeping && event == AgentEvent.none) {
				goToSleep();
				return true;
			}
			for(Item i : inventory) {
				if(i.quantity < 2 && state == AgentState.DoingNothing && event == AgentEvent.none) {
					goToMarket(i);
					return true;
				}	
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
		Random rand = new Random();
	    int foodChoice = rand.nextInt(3);
	    if(foodChoice == 0) {
			for(Item i : inventory) {
				if(i.name == "Steak") {
					i.quantity--;
				}
			}
	    }
	    else if(foodChoice == 1) {
	    	for(Item i : inventory) {
				if(i.name == "Chicken") {
					i.quantity--;
				}
			}
	    }
	    else if(foodChoice == 2) {
	    	for(Item i : inventory) {
				if(i.name == "Burger") {
					i.quantity--;
				}
			}
	    }
	    int brokenAppliance = rand.nextInt(9);
	    boolean anythingBrokenAlready = false;
	    for (HomeFeature hf : features) {
			if(!hf.working) {
				anythingBrokenAlready = true;
			}
		}
	    if(brokenAppliance == 0 && anythingBrokenAlready == false) {
	    	features.get(0).working = false;
	    	AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "My sink isn't working. Filing a work order.");
	    }
	    else if(brokenAppliance == 2 && anythingBrokenAlready == false) {
	    	features.get(1).working = false;
	    	AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "My stove isn't working. Filing a work order.");
	    }
	    else if(brokenAppliance == 5 && anythingBrokenAlready == false) {
	    	features.get(2).working = false;
	    	AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "My refrigerator isn't working. Filing a work order.");
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
				stateChanged();
			}
		},
		3000);
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
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Low on " + item.name + ". I'm calling the market.");
			List<Role> inhabitants = BuildingList.findBuildingWithName("Market 1").getInhabitants();
			for(Role r : inhabitants) {
				if (r.getNameOfRole() == "MARKET_MANAGER_ROLE") {
					MarketManagerRole mr = (MarketManagerRole) r;
					mr.msgMarketManagerFoodOrder("Cooking Ingredient", 5, this);
				}
			}
			myPerson.msgGoToMarket(item.name);
			callMarket = false;
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
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Low on " + item.name + ". I'm going to the market.");
			gui.DoGoToFrontDoor();
			try {
				atFrontDoor.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BuildingList.findBuildingWithName(myPerson.getHome().getName()).removeRole(this);
			myPerson.msgGoToMarket(item.name);
			callMarket = true;
		}
	}
	private void fileWorkOrder (HomeFeature brokenFeature) {
		AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), brokenFeature.name + " should be fixed in a day.");
		brokenFeature.workOrderFiled = true;
		featureRepairDate.set(MasterTime.getInstance().get(Calendar.YEAR), MasterTime.getInstance().get(Calendar.MONTH), MasterTime.getInstance().get(Calendar.DAY_OF_MONTH), MasterTime.getInstance().get(Calendar.HOUR_OF_DAY), MasterTime.getInstance().get(Calendar.MINUTE), MasterTime.getInstance().get(Calendar.SECOND));
		featureRepairDate.add(Calendar.DAY_OF_MONTH, 1);
		MasterTime.getInstance().registerDateListener(featureRepairDate.get(Calendar.MONTH), featureRepairDate.get(Calendar.DAY_OF_MONTH), featureRepairDate.get(Calendar.HOUR_OF_DAY), featureRepairDate.get(Calendar.MINUTE), myPerson);
	}
	private void payRent () {

//		if(myPerson.getMoney() >= getRentOwed()) {
//			//landlord.msgRentPaid (this, getRentOwed());
//			myPerson.setMoney(myPerson.getMoney()-getRentOwed());
//			setRentOwed(0);
//			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Paid my rent. I have $" + myPerson.getMoney() + " left.");
//		}

		if(myPerson.getMoney() >= rentOwed) {
			//landlord.msgRentPaid (this, rentOwed);
			myPerson.setMoney(myPerson.getMoney()-rentOwed);
			rentOwed = 0;
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Paid my rent. I have $" + myPerson.getMoney() + " left.");
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
				AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I'm awake!");
				stateChanged();
			}
		},
		5000);
	}
	private void leaveHome() {
		gui.DoGoToCenter();
		//kill();
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
		BuildingList.findBuildingWithName(myPerson.getHome().getName()).removeRole(this);
		BuildingList.findBuildingWithName(myPerson.getHome().getName()).removeInhabitants();
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
		if(myPerson.getFriends().size() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "I have no friends to invite to a party.");
			partyState = PartyState.setUp;
		}
		else {
			rsvpDate.set(MasterTime.getInstance().get(Calendar.YEAR), MasterTime.getInstance().get(Calendar.MONTH), MasterTime.getInstance().get(Calendar.DAY_OF_MONTH), MasterTime.getInstance().get(Calendar.HOUR_OF_DAY), MasterTime.getInstance().get(Calendar.MINUTE), MasterTime.getInstance().get(Calendar.SECOND)); 
			rsvpDate.add(Calendar.HOUR_OF_DAY, 6);
			MasterTime.getInstance().registerDateListener(rsvpDate.get(Calendar.MONTH), rsvpDate.get(Calendar.DAY_OF_MONTH), rsvpDate.get(Calendar.HOUR_OF_DAY), rsvpDate.get(Calendar.MINUTE), myPerson);
			
			partyDate.set(MasterTime.getInstance().get(Calendar.YEAR), MasterTime.getInstance().get(Calendar.MONTH), MasterTime.getInstance().get(Calendar.DAY_OF_MONTH), MasterTime.getInstance().get(Calendar.HOUR_OF_DAY), MasterTime.getInstance().get(Calendar.MINUTE), MasterTime.getInstance().get(Calendar.SECOND)); 
			partyDate.add(Calendar.DAY_OF_MONTH, 1);
			MasterTime.getInstance().registerDateListener(partyDate.get(Calendar.MONTH), partyDate.get(Calendar.DAY_OF_MONTH), partyDate.get(Calendar.HOUR_OF_DAY), partyDate.get(Calendar.MINUTE), myPerson);
			
			AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Inviting friends to my party.");
			for(int i=0; i<myPerson.getFriends().size(); i++) {
				myPerson.getFriends().get(i).msgPartyInvitation(myPerson, rsvpDate, partyDate);
				partyInvitees.add(myPerson.getFriends().get(i));
			}
			partyState = PartyState.setUp;
		}
	}
	private void resendInvites() {
		for(Person p : partyInvitees) {
			p.msgRespondToInviteUrgently(myPerson);
		}
	}
	private void hostParty() {
		partyState = PartyState.cleanUp;
		gui.hostingParty = true;
		timer.schedule(new TimerTask() {
			public void run() {
				partyState = PartyState.none;
				AlertLog.getInstance().logMessage(AlertTag.HOME_ROLE, myPerson.getName(), "Party's over! Thanks for coming!");
				for(Person p : partyAttendees) {
					p.msgPartyOver(myPerson);
				}
			}
		},
		15000);
		timer.schedule(new TimerTask() {
			public void run() {
				gui.hostingParty = false;
			}
		},
		25000);
	}

	//utilities
	
	public double getRentOwed() {
		return rentOwed;
	}

	public void setRentOwed(double rentOwed) {
		this.rentOwed = rentOwed;
	}

	public class HomeFeature {
		public String name;
		public boolean working;
		public boolean workOrderFiled;
		
		HomeFeature(String name) {
			this.name = name;
			working = true;
			workOrderFiled = false;
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
