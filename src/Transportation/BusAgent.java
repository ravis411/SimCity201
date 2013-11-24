package Transportation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;







import gui.LocationInfo;
import gui.agentGuis.VehicleGui;
import gui.interfaces.Passenger;
import gui.interfaces.Vehicle;
import agent.Agent;

public class BusAgent extends Agent implements Vehicle {

	//Data
	String name;
	boolean traveled = false;
	boolean goToBusStop3 = false;
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
	
	private List<myPassenger> passengers = new ArrayList<myPassenger>();
	private enum PassengerState {riding, disembarking};
	
	public enum AgentState {inTransit, loading, loaded};
	
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
		
	}
	
	public void msgGettingOffBus(Passenger p) {
		
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if(!traveled) {
			Travel();
			return true;
		}
		if(StopsQueue.peek() != null){
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
		String location;
		
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
	
	
	public String toString(){
		return "" + name;
	}
	
	public String getName(){
		return name;
	}
	
	
}