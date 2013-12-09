package Transportation;

import interfaces.Bus;
import interfaces.BusStop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import gui.agentGuis.BusGui;
import agent.Agent;

public class BusAgent extends Agent implements Bus 
{
	
	//May need to modify DoGoToLocation method to implement agent methodology

	//Data
	String name;
	public String location;
	private Map<Integer,String> stops = new HashMap<Integer,String>(); //Will change stop names to real names on implementation
	private Map<String, BusStop> stopAgents = new HashMap<String,BusStop>();
	private int count, stopSize;
	public BusStop currentStop;
	public BusGui agentGui;
	
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
		state = AgentState.readyToLeave;

		this.name = name;
		this.agentGui = new BusGui(this);
	}
	/**
	 * Utility function for filling in the map of Bus Stops for the Bus Agent. 
	 * @param stopNumber Internal stop number for the Bus Agent
	 * @param stop Name of the stop
	 * @param stopAgent Pointer to the Bus stop agent corresponding to the stop
	 */
	
	public void addBusStop(int stopNumber, String stop, BusStop stopAgent) {
		stops.put(stopNumber, stop);
		stopAgents.put(stop, stopAgent);
		stopSize++;
	}
	
	//Messages
	
	/**
	 * Message from the bus stop to the bus adding all people waiting
	 * at the bus stop to the bus's passenger list.
	 * @param passengers List of the myBusPassenger class sent from bus stop
	 */
	public void msgHereArePassengers(List<myBusPassenger> passengers ){
		if(passengers.size() > 0){
			AlertLog.getInstance().logMessage(AlertTag.BUS_STOP, "BusAgent", "Number of People added: "+passengers.size());
			this.passengers.addAll(passengers);
		}
		state = AgentState.readyToLeave;
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
		
		if(state == AgentState.readyToLeave)
		{
			GoToNextStop();
			return true;
		}
		
		
		return false;
	}
	
	//Actions
	
	private void GoToNextStop(){
		agentGui.setNumberPassengers(passengers.size());
		//print("going to next stop");
		//currentStop.msgLeavingStop();
		state = AgentState.inTransit;
		//AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Going to next stop");
		
		//print("Count is now: " + count);
		count++;
		location = stops.get(count%stopSize +1);
		currentStop = stopAgents.get(location);
		//print("Location is now: " + location);
		//print("Count is now: " + count);
		agentGui.DoGoTo(location);
		state = AgentState.atStop;
		//Need some way of notifying bus that we have arrived at next stop
		currentStop.msgArrivedAtStop(this);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void notifyPassenger(myBusPassenger pas) {
		pas.passenger.msgWeHaveArrived(location);
		passengers.remove(pas);
		agentGui.setNumberPassengers(passengers.size());
	}
	
	
	/**	Sets the starting bus stop...
	 * 
	 * @param start
	 */
	public void setStartStopNumber(int start){
		this.count = start;
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