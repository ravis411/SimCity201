package Transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gui.interfaces.BusStop;
import gui.interfaces.Passenger;
import agent.Agent;

public class BusStopAgent extends Agent implements BusStop {
	//Data
	private class myPassenger {
		myPassenger(Passenger p, PassengerState pass) {
			passenger = p;
			ps = pass;
		}
		Passenger passenger;
		PassengerState ps;
	}
	
	private enum PassengerState {waiting, boarded, departed};
	
	public enum AgentState {Idle, BusInStop};
	
	private AgentState state = AgentState.Idle;
	
	private List<myPassenger> waitingPassengers = Collections.synchronizedList(new ArrayList<myPassenger>());
	
	
	//Messages
	public void msgWaitingForBus(Passenger p) {
		//Sent from passenger to bus stop
		waitingPassengers.add(new myPassenger(p, PassengerState.waiting));
		stateChanged();
	}

	public void msgAtStop() {
		//Sent from bus to bus stop
		state = AgentState.BusInStop;
		stateChanged();
	}
	
	public void msgLeavingStop() {
		state = AgentState.Idle;
	}
	
	public void msgNewPassenger(Passenger p) {
		//Sent from bus to bus stop when a new passenger boards the bus
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == AgentState.BusInStop) {
			synchronized(waitingPassengers) {
				for (myPassenger p : waitingPassengers) {
					p.passenger.msgBusIsHere();
				}
			}
			return true;
		}
		return false;
	}
	
	//Actions
	
}