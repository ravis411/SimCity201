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

	
	public void msgAtBusStop(Person person, String destinationStop) {
		//Sent from passenger to bus stop
		//AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus\t"+waitingPassengers.size()+" people");
		waitingPassengers.add(new myBusPassenger(person, destinationStop));
		animationPanel.addWaitingPassenger(person.getName());
		AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, name, "A new passenger is waiting for the bus\t"+waitingPassengers.size()+" people");
	}

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
	public int passengerSize() {
		return waitingPassengers.size();
	}
	
	public String getName() {
		return name;
	}
	
}