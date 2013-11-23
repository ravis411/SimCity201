package Transportation;

import java.util.ArrayList;
import java.util.List;

import gui.interfaces.BusStop;
import gui.interfaces.Passenger;
import agent.Agent;
import Person.Role.PassengerRole;;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	private List<Passenger> waitingPassengers = new ArrayList<Passenger>();
	
	
	//Messages
	public void msgWaitingForBus(Passenger p) {
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