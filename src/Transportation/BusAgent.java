package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;








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
	String location;
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
	private AgentState state = AgentState.inTransit;
	
	public BusAgent(String name, Queue<String> busStops) {
		super();
		StopsQueue.addAll(busStops);
		this.name = name;

	}

	//Messages
	public void msgGettingOnBus(Passenger p) {
		passengers.add(new myPassenger(p, PassengerState.riding));
		stateChanged();
	}
	
	public void msgFreeToLeave() {
		state = AgentState.loaded;
		stateChanged();
	}
	
	public void msgArrivedAtStop() {
		state = AgentState.loading;
		stateChanged();
	}
	
	public void msgGettingOffBus(Passenger p) {
		synchronized(passengers) {
			for (myPassenger mp : passengers) {
				if (mp.equals(p)) {
					mp.ps = PassengerState.disembarking;
					stateChanged();
				}
			}
		}
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		synchronized(passengers) {
			for (myPassenger mp : passengers) {
				if (mp.ps == PassengerState.disembarking) {
					passengers.remove(mp);
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
	
	private void GoToNextStop(){
		state = AgentState.inTransit;
		
		
		location = StopsQueue.poll();//<--removes location from front of queue
		StopsQueue.add(location);//<--adds location to end of queue
		//print("Going to " + location);
		agentGui.DoGoTo(location);
	//	print("Arrived at " + location);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void notifyPassengers() {
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
	
	
}