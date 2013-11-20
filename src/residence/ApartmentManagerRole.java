package residence;

import agent.Agent;

import java.util.*;

/**
 * Apartment Manager Role
 * @param <HomeRole>
 */

public class ApartmentManagerRole extends Agent {
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
	public void msgRentPaid (Agent person, int amount) {
		stateChanged();
	}
	public void msgBrokenFeature(String name, Agent p) {
		thingsToFix.add(new BrokenFeature(name, p));
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (collectRent == true) {
			for (Agent p : residents) {
				chargeRent(p);
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

	private void chargeRent (Agent p) {
		p.msgPayBackRent();
	}
	private void demandRent (Agent p) {
		p.msgPayBackRentUrgently();
	}
	private void fixFeature(BrokenFeature bf) {
		bf.resident.msgFixedFeature(bf.name);
	}

	//utilities
	
	private class BrokenFeature {
		String name;
		Agent resident;
		
		public BrokenFeature(String name, Agent p) {
			this.name = name;
			resident = p;
		}
	}
}
