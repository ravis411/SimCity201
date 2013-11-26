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

public class BusStopAgent implements BusStop {
	
	
	//A map of all the busStop agents.
	public static Map<String, BusStop> stops = new HashMap<String, BusStop>();
	
	public static void addStop(String stop, BusStop agent) {
		stops.put(stop, agent);
	}
	
	
	
	
	public BusStopAgent(String name) {
		this.name = name;
	}
	
	
	private List<myBusPassenger> waitingPassengers = Collections.synchronizedList(new ArrayList<myBusPassenger>());
	public Bus currentBus;
	private String name;

	
	public void msgAtBusStop(PersonAgent person, String destinationStop) {
		//Sent from passenger to bus stop
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus");
		waitingPassengers.add(new myBusPassenger(person, destinationStop));
		//stateChanged();
	}

	public void msgArrivedAtStop(Bus bus) {
		//Sent from bus to bus stop
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A bus has arrived at the stop");
		currentBus = bus;
		currentBus.msgHereArePassengers(waitingPassengers);
	}
	
	
	
	//Utilities
	
	public int passengerSize() {
		return waitingPassengers.size();
	}
	
	public String getName() {
		return name;
	}
	
}