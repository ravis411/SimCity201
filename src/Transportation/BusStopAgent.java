package Transportation;

import java.util.ArrayList;
import java.util.List;

import gui.interfaces.BusStop;
import agent.Agent;
import Person.PersonAgent;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	private List<PersonAgent> waitingPassengers = new ArrayList<PersonAgent>();
	
	
	//Messages
	public void msgWaitingForBus(PersonAgent p) {
		//Sent from passenger to bus stop
		waitingPassengers.add(p);
		stateChanged();
	}

	public void msgAtStop() {
		//Sent from bus to bus stop
		
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Actions
	
}