package Transportation;

import interfaces.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import gui.interfaces.BusStop;
import gui.interfaces.Bus;
import Person.PersonAgent;

public class BusStopAgent implements BusStop {
	
	//May need to refactor and rename something not "Agent"
	
	//A map of all the busStop agents.
	public static Map<String, BusStop> stops = Collections.synchronizedMap(new HashMap<String, BusStop>());
	
	public static void addStop(String stop, BusStop agent) {
		stops.put(stop, agent);
	}
	
	
	BusStopAnimationPanel animationPanel = null;
	
	public BusStopAgent(String name, BusStopAnimationPanel animationPanel) {
		this.name = name;
		this.animationPanel = animationPanel;
	}
	
	
	private List<myBusPassenger> waitingPassengers = new ArrayList<myBusPassenger>();
	public Bus currentBus;
	private String name;

	/**
	 * Message received from person upon arrival at the bus stop
	 * @param person Passenger waiting at the stop
	 * @param destinationStop Passenger's destination
	 */
	public void msgAtBusStop(Person person, String destinationStop) {
		//Sent from passenger to bus stop
		//AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus\t"+waitingPassengers.size()+" people");
		waitingPassengers.add(new myBusPassenger(person, destinationStop));
		animationPanel.addWaitingPassenger(person.getName());
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus\t"+waitingPassengers.size()+" people");
	}

	/**
	 * Message received from bus upon arrival at the bus stop
	 * @param bus 
	 */
	public void msgArrivedAtStop(Bus bus) {
		//Sent from bus to bus stop
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A bus has arrived at the stop for "+waitingPassengers.size()+" people");
		currentBus = bus;
		currentBus.msgHereArePassengers(waitingPassengers);
		for(myBusPassenger b : waitingPassengers){
			animationPanel.removeWaitingPassenger(b.passenger.getName());
		}
		waitingPassengers.removeAll(waitingPassengers);
	}
	
	
	
	//Utilities
	/**
	 * Utility function returning the size of the passenger list
	 * @return Number of passengers waiting at the bus stop
	 */
	public int passengerSize() {
		return waitingPassengers.size();
	}
	/**
	 * Utility function returning the name of the bus stop
	 * @return Name of the stop
	 */
	public String getName() {
		return name;
	}
	
}