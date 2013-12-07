package Transportation;

import agent.Agent;
import gui.agentGuis.CarVehicleGui;
import gui.agentGuis.VehicleGui;
import interfaces.Car;
import interfaces.Person;

public class CarAgent extends Agent implements Car{
	CarAgent(Person person) {
		passenger = new myPassenger(person);
		destination = "N/A";
		state = CarState.parked;
	}
	/**
	 * Default Constructor
	 */
	public CarAgent(String name){
		super();
		this.name = name;
	}
	
		
	//Data
	String name = null;
	private CarVehicleGui agentGui = null;
	
	private class myPassenger {
		myPassenger(Person person) {
			p = person;
		}
		Person p;
		PassengerState state;
	}
	private myPassenger passenger; //Only have single passengers right now
	private String destination = new String(); 
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
		
//		//Test stuff
//		destination = "House 1";
//		goToDestination();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "House 2";
//		goToDestination();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Market 1";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Apartment Building";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Apartment Building 4";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Restaurant 1";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Apartment 5";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Apartment 15";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Default";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Food Court";
//		goToDestination();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		destination = "Restaurant 3";
//		goToDestination();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if(true)
//			return true;
//		//end Test stuff
		
		
		
		
		
		
		
		
		
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
		agentGui.DoGoTo(destination);
		//Semaphore.acquire/Thread sleep
		//Person.weAreHere();
	}
	
	private void parkCar() {
		//GUI.doTurnInvisible();
	}
	
	
	//Utilities
	public void setGui(CarVehicleGui carGui){
		this.agentGui = carGui;
	}
	
	public String toString(){
		return "" + name;
	}
	
	public String getName(){
		return name;
	}
	
}
