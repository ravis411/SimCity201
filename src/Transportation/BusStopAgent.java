package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import gui.interfaces.BusStop;
import gui.interfaces.Passenger;
import gui.interfaces.Bus;
import Person.PersonAgent;
import agent.Agent;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	
	
	//A map of all the busStop agents.
	private static Map<String, BusStop> stops = new HashMap<String, BusStop>();
	
	public static void addStop(String stop, BusStop agent) {
		stops.put(stop, agent);
	}
	
	
	
	
	public BusStopAgent(String name) {
		state = AgentState.Idle;
		this.name = name;
	}
	
	private enum PassengerState {waiting, boarded};
	
	public enum AgentState {Idle, BusInStop};
	
	public AgentState state;
	
	private List<myPassenger> waitingPassengers = Collections.synchronizedList(new ArrayList<myPassenger>());
	public Bus currentBus;
	private String name;

	
	//Messages
	public void msgWaitingForBus(PersonAgent person, String destinationStop) {
		//Sent from passenger to bus stop
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus");
		waitingPassengers.add(new myPassenger(person, PassengerState.waiting, destinationStop));
		stateChanged();
	}

	public void msgAtStop(Bus bus) {
		//Sent from bus to bus stop
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A bus has arrived at the stop");
		currentBus = bus;
		state = AgentState.BusInStop;
		stateChanged();
	}
	
	public void msgLeavingStop() {
		state = AgentState.Idle;
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A bus has left the stop");
		stateChanged();
	}
	
	public void msgNewPassenger(Passenger p) {
		//Sent from bus to bus stop when a new passenger boards the bus
		synchronized(waitingPassengers) {
			for (myPassenger mp : waitingPassengers) {
				if (mp.passenger.equals(p)) {
					AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A passenger has boarded the bus");
					//print("Passenger has boarded bus");
					mp.ps = PassengerState.boarded;
				}
			}
		}
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.BusInStop) {
			synchronized(waitingPassengers) {
				for (myPassenger mp : waitingPassengers) {
					if (mp.ps == PassengerState.waiting) {
						boardPassengers();
						return true;
					}
				}
			}
			synchronized(waitingPassengers) {
				for (myPassenger mp : waitingPassengers) {
					if (mp.ps == PassengerState.boarded) {
						removePassenger(mp);
						return true;
					}
				}
			}
			synchronized(waitingPassengers) {
				if (waitingPassengers.size() == 0) {
					releaseBus();
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	//Actions
	private void boardPassengers() {
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "Telling passengers to board");
		//print("Boarding passengers");
		for (myPassenger mp: waitingPassengers) {
			mp.passenger.msgBusIsHere(currentBus);
		}
	}
	
	private void removePassenger(myPassenger mp) {
		waitingPassengers.remove(mp);
	}
	
	private void releaseBus() {
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "Releasing bus to continue route");
		currentBus.msgFreeToLeave();
		state = AgentState.Idle;
	}
	
	public int passengerSize() {
		return waitingPassengers.size();
	}
	
	public String getName() {
		return name;
	}
	
}