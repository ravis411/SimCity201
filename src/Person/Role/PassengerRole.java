package Person.Role;

import java.util.HashMap;
import java.util.Map;

import gui.agentGuis.PersonGui;
import gui.interfaces.Bus;
import gui.interfaces.BusStop;
import gui.interfaces.Passenger;

public class PassengerRole extends Role implements Passenger{
	
	PassengerRole() {
		//this.name = name;
		//personGui = myPerson.getPersonGui();
	}
	
	public void setPersonGui(PersonGui gui){
		this.personGui = gui;
	}
	
	//Data
	private String name;
	private String destination; //Remember to set as null upon arrival
	private String nextLocation;
	private String startingBusStop;
	private String destinationBusStop;
	private PersonGui personGui = null;
	private Bus currentBus; 
	
	public enum AgentState {notAtStop, waitingForBus, busArrived, riding, disembarking, notInTransit};
	private AgentState state = AgentState.notInTransit;
	
	private static Map<String, BusStop> stops = new HashMap<String, BusStop>();
	
	public static void addStop(String stop, BusStop agent) {
		stops.put(stop, agent);
	}
	//Passenger role needs to figure out which bus is the closest one
	//Messages
	
	public void setDestination(String d) {
		destination = d;
		state = AgentState.notAtStop;
		//setDestination only gets called when at the bus stop
		stateChanged();
	}
	
	public void msgBusIsHere(Bus bus) {
		currentBus = bus;
		state = AgentState.busArrived;
		stateChanged();
	}
	
	public void msgNextDestination(String location) {
		nextLocation = location;
		stateChanged();
	}
	//Scheduler
	@Override
	public boolean pickAndExecuteAction() {
		if(state == AgentState.notAtStop){
			goToClosestStop();
		}
		
		if (state == AgentState.busArrived) {
			getOnBus();
			return true;
		}
		if (state == AgentState.riding) {
			if (nextLocation.equals(destinationBusStop)) {
				getOffBus();
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean canGoGetFood() {
		//Dead End method
		//Meant to tell person that he can't get food right now
		return false;
	}

	@Override
	public String getNameOfRole() {
		return name;
	}
		
	
	//Actions
	
	/**This has the person move from their current location to the bus stop nearest them.
	 * 
	 */
	private void goToClosestStop(){
		startingBusStop = personGui.DoGoToClosestBusStop();
	}
	
	/**This teleports the person to the bus stop they need to get off at.
	 * 
	 */
	private void getOnBus() {
		currentBus.msgGettingOnBus(this);
		destinationBusStop = personGui.DoRideBusTo(destination);
	}
	
	private void getOffBus() {
		currentBus.msgGettingOffBus(this);
		destination = "N/A";
		state = AgentState.disembarking;
	}
}
