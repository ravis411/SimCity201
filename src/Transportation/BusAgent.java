package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import gui.agentGuis.VehicleGui;
import gui.interfaces.Passenger;
import gui.interfaces.Bus;
import agent.Agent;

public class BusAgent extends Agent implements Bus {
	
	//May need to modify DoGoToLocation method to implement agent methodology

	//Data
	String name;
	boolean traveled = false;
	boolean goToBusStop3 = false;
	public String location;
	private Map<Integer,String> stops = new HashMap<Integer,String>(); //Will change stop names to real names on implementation
	private int count;
	public VehicleGui agentGui;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the stops to go to
	
	private class myPassenger {
		myPassenger(Passenger p, PassengerState pass) {
			passenger = p;
			ps = pass;
		}
		Passenger passenger;
		PassengerState ps;
	}
	
	private List<myPassenger> passengers = Collections.synchronizedList(new ArrayList<myPassenger>());
	private enum PassengerState {riding, disembarking};
	
	public enum AgentState {inTransit, unloading, loading, loaded};
	public AgentState state;
	
	public BusAgent(String name) {
		super();
		stops.put(1,"Stop_1");
		stops.put(2,"Stop_2");
		stops.put(3,"Stop_3");
		location = "Stop_1";
		count = 1;
		this.name = name;
	}

	//Messages
	public void msgGettingOnBus(Passenger p) {
		state = AgentState.loading;
		getPassengers().add(new myPassenger(p, PassengerState.riding));
		stateChanged();
	}
	
	public void msgFreeToLeave() {
		print("msgFreeToLeave called");
		state = AgentState.loaded;
		stateChanged();
	}
	
	public void msgArrivedAtStop() {
		state = AgentState.unloading;
		stateChanged();
	}
	
	public void msgGettingOffBus(Passenger p) {
		state = AgentState.unloading;
		synchronized(getPassengers()) {
			for (myPassenger mp : getPassengers()) {
				if (mp.passenger.equals(p)) {
					print("Found passenger leaving");
					mp.ps = PassengerState.disembarking;
					stateChanged();
				}
			}
		}
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		//print("Location is " + location);
		if (state == AgentState.unloading) {
			synchronized(passengers) {
				for (myPassenger mp : passengers) {
					if (mp.ps == PassengerState.disembarking) {
						removePassenger(mp);
						return true;
					}
				}
			}
			
		}
		if (state == AgentState.loaded) {
			GoToNextStop();
			notifyPassengers();
			return true;
		}
		
		return false;
	}
	
	//Actions
	
	private void removePassenger(myPassenger mp) {
		print("Removing passenger from list");
		//notify passenger gui to leave
		passengers.remove(mp);
	}
	
	private void GoToNextStop(){
		print("going to next stop");
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Going to next stop");
		state = AgentState.inTransit;
		
		//print("Count is now: " + count);
		count++;
		location = stops.get(count%3);
		//print("Location is now: " + location);
		//print("Count is now: " + count);
		//agentGui.DoGoTo(location);
		//Need some way of notifying bus that we have arrived at next stop
	}
	
	private void notifyPassengers() {
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Notifying passenges of the next stop: " + location);
		synchronized(passengers) {
			for (myPassenger mp : passengers) {
				if (mp.ps == PassengerState.riding) {
					mp.passenger.msgNextDestination(location);
				}
			}
		}
	}
	
	public String toString(){
		return "" + name;
	}
	
	public String getName(){
		return name;
	}

	public List<myPassenger> getPassengers() {
		return passengers;
	}
	
}