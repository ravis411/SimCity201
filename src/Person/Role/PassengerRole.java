package Person.Role;

import gui.agentGuis.PersonGui;
import gui.interfaces.Passenger;

public class PassengerRole extends Role implements Passenger{
	
	PassengerRole(String name) {
		this.name = name;
		personGui = myPerson.getPersonGui();
	}
	
	//Data
	private String name;
	private String destination; //Remember to set as null upon arrival
	private String nextLocation;
	private PersonGui personGui = null;
	
	public enum AgentState {waitingForBus, busArrived, riding, disembarking, notInTransit};
	private AgentState state = AgentState.notInTransit;
	
	//Passenger role needs to figure out which bus is the closest one
	//Messages
	
	public void setDestination(String d) {
		destination = d;
		state = AgentState.waitingForBus;
		//setDestination only gets called when at the bus stop
		stateChanged();
	}
	
	public void msgBusIsHere() {
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
		if(state == AgentState.notInTransit){
			goToClosestStop();
		}
		
		if (state == AgentState.busArrived) {
			getOnBus();
			return true;
		}
		if (state == AgentState.riding) {
			if (nextLocation.equals(destination)) {
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
		String startingBusStop = personGui.DoGoToClosestBusStop();
	}
	
	private void getOnBus() {
		//Get onto the bus
	}
	
	private void getOffBus() {
		destination = "N/A";
		state = AgentState.disembarking;
	}
}
