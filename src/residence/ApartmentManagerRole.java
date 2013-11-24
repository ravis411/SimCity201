package residence;

import Person.PersonAgent;
import Person.Role.Role;
import agent.Agent;
import residence.interfaces.*;

import java.util.*;

/**
 * Apartment Manager Role
 * @param <HomeRole>
 */

public class ApartmentManagerRole extends Role implements ApartmentManager {
	private List <HomeRole> residents = new ArrayList<HomeRole>();
	private List <BrokenFeature> thingsToFix = new ArrayList<BrokenFeature>();
	boolean collectRent = false;
	
	private String name;
	
	public enum AgentState
	{DoingNothing};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none};
	AgentEvent event = AgentEvent.none;

	public ApartmentManagerRole(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	// Messages
	
	public void msgCollectRent() { //called by timer when it's time to collect rent from residents
		collectRent = true;
		stateChanged();
	}
	public void msgRentPaid (HomeRole p, int amount) {
		stateChanged();
	}
	public void msgBrokenFeature(String name, HomeRole h) {
		thingsToFix.add(new BrokenFeature(name, h));
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (collectRent == true) {
			for (HomeRole h : residents) {
				chargeRent(h);
			}
			return true;
		}
		/*for (Agent p : residents) {
			if (p hasn't paid rent in 5 days) {
					demandRent(p);
			}
			return true; //should this statement be moved?!?!?!
		}*/
		if (!thingsToFix.isEmpty()) {
			fixFeature(thingsToFix.get(0));
			return true;
		}
		return false;
	}
	

	// Actions

	private void chargeRent (HomeRole h) {
		h.msgRentDue(20);
	}
	private void demandRent (PersonAgent p) {
		p.msgPayBackRentUrgently();
	}
	private void fixFeature(BrokenFeature bf) {
		bf.resident.msgFixedFeature(bf.name);
	}

	//utilities
	
	private class BrokenFeature {
		String name;
		HomeRole resident;
		
		public BrokenFeature(String name, HomeRole h) {
			this.name = name;
			resident = h;
		}
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}
}
