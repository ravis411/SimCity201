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
		
	}
	
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Actions
	
}
