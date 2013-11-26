package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import gui.agentGuis.VehicleGui;
import gui.interfaces.BusStop;
import gui.interfaces.Passenger;
import gui.interfaces.Bus;
import agent.Agent;

public class BusAgent extends Agent //implements Bus 
{
	
	//May need to modify DoGoToLocation method to implement agent methodology

	//Data
	String name;
	public String location;
	private Map<Integer,String> stops = new HashMap<Integer,String>(); //Will change stop names to real names on implementation
	private Map<String, BusStop> stopAgents = new HashMap<String,BusStop>();
	private int count, stopSize;
	public BusStop currentStop;
	public VehicleGui agentGui;
	
	private List<myBusPassenger> passengers = Collections.synchronizedList(new ArrayList<myBusPassenger>());

	enum AgentState {none, atStop, readyToLeave, inTransit};
	AgentState state = AgentState.none;
	
	
	public BusAgent(String name) {
		super();
		//Remember to set initial location and initial stop
		
		/*stops.put(1,"Stop_1");
		stops.put(2,"Stop_2");
		stops.put(3,"Stop_3");*/
		//location = "Stop_1";
		count = 1;
		stopSize = 0;

		//stopSize = 3;

		this.name = name;
	}
	public void addBusStop(int stopNumber, String stop, BusStop stopAgent) {
		stops.put(stopNumber, stop);
		stopAgents.put(stop, stopAgent);
		stopSize++;
	}
	
	//Messages
	public void msgHereArePassengers(List<myBusPassenger> passengers ){
		this.passengers.addAll(passengers);
		stateChanged();
	}
	
		
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		myBusPassenger pas = null;
		
		synchronized (passengers) {
			for(myBusPassenger p : passengers){
				if(p.destination == location){
					pas = p; break; 	}	}
		}
		if(pas != null){
			notifyPassenger(pas);
			return true;
		}
		
		if(true){
			GoToNextStop();
			return true;
		}
		
		
		return false;
	}
	
	//Actions
	
	private void GoToNextStop(){
		
	}
	
	private void notifyPassenger(myBusPassenger pas) {
		pas.passenger.msgWeHaveArrived(location);
		passengers.remove(pas);
	}
	
		
	public String toString(){
		return "" + name;
	}
	
	public String getName(){
		return name;
	}

	public List<myBusPassenger> getPassengers() {
		return passengers;
	}
	
}