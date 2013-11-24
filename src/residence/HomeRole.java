package residence;

import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
import residence.gui.HomeRoleGui;
import residence.interfaces.*;

import java.util.*;

/**
 * Home Role
 */

public class HomeRole extends Role implements Home {
	private ApartmentManagerRole landlord;
	private int rentOwed = 0;
	private int aptNumber = 0;
	private boolean tired = false;
	//private Map <String, Integer> inventory = new HashMap<String, Integer>();
	private List <Item> inventory = new ArrayList<Item>();
	private List <HomeFeature> features = new ArrayList<HomeFeature>(); //includes appliances, toilets, sinks, etc (anything that can break)

	private String name;
	
	public HomeRoleGui gui = null;

	public enum AgentState
	{DoingNothing, Cooking, Sleeping};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none};
	AgentEvent event = AgentEvent.none;

	public HomeRole(String name) {
		//super();
		this.name = name;
		
		inventory.add(new Item("Cooking Ingredients",2));
		inventory.add(new Item("Cleaning supplies", 2));
	}
	
	public String getName() {
		return name;
	}
	
	public boolean canGoGetFood() {
		return true;
	}
	
	// Messages
	
	public void msgRentDue (int amount) {
		rentOwed = amount;
		stateChanged();
	}
	public void msgTired() { //called by timer
		tired = true;
		print("I'm tired.");
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
		print("I'm eating at home today!");
		state = AgentState.Cooking;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (state == AgentState.Cooking) {
			cook();
		}
		if (rentOwed > 0) {
			payRent();
			return true;
		}
		/*if (Person.stateOfNourishment == hungry) {
			eat()
			return true;
		}*/
		if (tired) {
			goToSleep();
			return true;
		}
		/*for(Integer i : inventory.values()) {
			if (i.equals(1)) {
				goToMarket(item);
				return true;
			}
		}*/
		for(Item i : inventory) {
			if(i.quantity < 2) {
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
	}
	private void goToMarket (Item item) {
		print("I'm going to the market.");
		gui.DoGoToFrontDoor();
	}
	private void fileWorkOrder (HomeFeature brokenFeature) {
		landlord.msgBrokenFeature(brokenFeature.name, this);
	}
	private void payRent () {
		landlord.msgRentPaid (this, rentOwed);
		rentOwed = 0;
	}
	private void eat() { //chooses whether person cooks at home or goes out to a restaurant
		
	}
	private void goToSleep() {
		gui.DoGoToBed();
	}

	//utilities
	
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
