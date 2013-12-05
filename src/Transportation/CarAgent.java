package Transportation;

import agent.Agent;
import interfaces.Car;
import interfaces.Person;

public class CarAgent extends Agent implements Car{
	CarAgent(Person person) {
		passenger = new myPassenger(person);
		destination = "N/A";
		state = CarState.parked;
	}
	
	
	//Data
	
	private class myPassenger {
		myPassenger(Person person) {
			p = person;
		}
		Person p;
		PassengerState state;
	}
	private myPassenger passenger; //Only have single passengers right now
	private String destination; 
	private CarState state;
	
	public enum CarState {parked, driving};
	public enum PassengerState {present, away};
	
	
	//Messages
	
	public void msgNewDestination(String destination) {
		this.destination = destination;
		this.state = CarState.driving;
		stateChanged();
	}
	
	public void msgParkingCar() {
		this.state = CarState.parked;
		stateChanged();
	}
	
	public void msgLeavingCar() {
		passenger.state = PassengerState.away;
		stateChanged();
	}
	
	public void msgEnteringCar() {
		passenger.state = PassengerState.present;
		stateChanged();
	}
	
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == CarState.driving && passenger.state == PassengerState.present) {
			goToDestination();
			return true;
		}
		
		if (state == CarState.parked && passenger.state == PassengerState.away) {
			parkCar();
			return true;
		}
		
		return false;
	}
	
	//Actions
	private void goToDestination() {
		//GUI.doTurnVisible();
		//GUI.doGoToDestination(destination);
		//Semaphore.acquire/Thread sleep
		//Person.weAreHere();
	}
	
	private void parkCar() {
		//GUI.doTurnInvisible();
	}
}
