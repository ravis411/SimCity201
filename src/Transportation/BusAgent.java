package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import gui.LocationInfo;
import gui.agentGuis.VehicleGui;
import gui.interfaces.Passenger;
import gui.interfaces.Vehicle;
import agent.Agent;

public class BusAgent extends Agent implements Vehicle {
	
	//May need to modify DoGoToLocation method to implement agent methodology

	//Data
	String name;
	boolean traveled = false;
	boolean goToBusStop3 = false;
	public String location = "N/A";
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
	
	public enum AgentState {inTransit, loading, loaded};
	public AgentState state;
	
	public BusAgent(String name) {
		super();
		StopsQueue.add("Bus Stop " + 1);
		StopsQueue.add("Bus Stop 3");
		StopsQueue.add("Bus Stop " + 5);
		this.name = name;
	}

	//Messages
	public void msgGettingOnBus(Passenger p) {
		getPassengers().add(new myPassenger(p, PassengerState.riding));
		stateChanged();
	}
	
	public void msgFreeToLeave() {
		print("msgFreeToLeave called");
		state = AgentState.loaded;
		stateChanged();
	}
	
	public void msgArrivedAtStop() {
		state = AgentState.loading;
		stateChanged();
	}
	
	public void msgGettingOffBus(Passenger p) {
		synchronized(getPassengers()) {
			for (myPassenger mp : getPassengers()) {
				if (mp.equals(p)) {
					mp.ps = PassengerState.disembarking;
					stateChanged();
				}
			}
		}
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		print("Location is " + location);
		synchronized(passengers) {
			for (myPassenger mp : passengers) {
				if (mp.ps == PassengerState.disembarking) {
					removePassenger(mp);
					return true;
				}
			}
		}
		if (state == AgentState.loading){
			notifyPassengers();
			return true;
		}
		if (state == AgentState.loaded) {
			GoToNextStop();
			return true;
		}
		
		return false;
	}
	
	//Actions
	private void Travel(){
		//agentGui.DoEnterWorld();
		//agentGui.DoPark();
		traveled = true;
	}
	
	private void removePassenger(myPassenger mp) {
		passengers.remove(mp);
	}
	
	private void GoToNextStop(){
		print("going to next stop");
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Going to next stop");
		state = AgentState.inTransit;
		
		location = StopsQueue.poll();//<--removes location from front of queue
		print(location);
		StopsQueue.add(location);//<--adds location to end of queue
		agentGui.DoGoTo(location);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Need some way of notifying bus that we have arrived at next stop
	}
	
	private void notifyPassengers() {
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Notifying passenges of the current stop: " + location);
		synchronized(passengers) {
			for (myPassenger mp : passengers) {
				if (mp.ps == PassengerState.riding) {
					mp.passenger.msgArrivedAtDestination(location);
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