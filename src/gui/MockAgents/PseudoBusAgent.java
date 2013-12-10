package gui.MockAgents;

import interfaces.Bus;
import interfaces.Passenger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;







import trace.AlertLog;
import trace.AlertTag;
import gui.LocationInfo;
import gui.agentGuis.BusGui;
import Transportation.myBusPassenger;
import agent.Agent;

public class PseudoBusAgent extends Agent implements Bus {

	
	String name;
	boolean traveled = false;
	boolean goToBusStop3 = false;
	public BusGui agentGui;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the stops to go to
	
	public PseudoBusAgent(String name, Queue<String> busStops) {
		super();
		StopsQueue.addAll(busStops);
		this.name = name;
//		if(order) {
//			StopsQueue.add("Bus Stop " + 1);
//			StopsQueue.add("Bus Stop " + 3);
//			StopsQueue.add("Bus Stop " + 5);
//			
//		}
//		else
//		{
//			StopsQueue.add("Bus Stop " + 2);
//			StopsQueue.add("Bus Stop " + 4);
//			StopsQueue.add("Bus Stop " + 6);
//		}
	}
	
	public String getName(){
		return name;
	}
	
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

	public void msgGettingOnBus(Passenger p) {
		// TODO Auto-generated method stub
		
	}

	public void msgFreeToLeave() {
		// TODO Auto-generated method stub
		
	}

	public void msgArrivedAtStop() {
		// TODO Auto-generated method stub
		
	}

	public void msgGettingOffBus(Passenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereArePassengers(List<myBusPassenger> passengers) {
		// TODO Auto-generated method stub
		
	}


	
	
}