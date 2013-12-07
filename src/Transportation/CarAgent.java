package Transportation;

import agent.Agent;
import astar.AStarTraversal;
import astar.VehicleAStarTraversal;
import gui.SetUpWorldFactory;
import gui.agentGuis.CarVehicleGui;
import gui.agentGuis.VehicleGui;
import interfaces.Car;
import interfaces.Person;

public class CarAgent extends Agent implements Car{
	public CarAgent(Person person, String name) {
		passenger = new myPassenger(person);
		destination = "N/A";
		state = CarState.parked;
		this.name = name;
		
		AStarTraversal ct = new VehicleAStarTraversal(SetUpWorldFactory.layout.getAgentGrid(), SetUpWorldFactory.layout.getRoadGrid());
		CarVehicleGui carGui = new CarVehicleGui( this, SetUpWorldFactory.layout, ct, SetUpWorldFactory.locationMap);
		this.setGui(carGui);
		SetUpWorldFactory.cityPanel.addGui(carGui);
		this.startThread();
		
	}
	/**
	 * Default Constructor
	 */
	public CarAgent(String name){
		super();
		this.name = name;
		state = CarState.parked;
		
		AStarTraversal ct = new VehicleAStarTraversal(SetUpWorldFactory.layout.getAgentGrid(), SetUpWorldFactory.layout.getRoadGrid());
		CarVehicleGui carGui = new CarVehicleGui( this, SetUpWorldFactory.layout, ct, SetUpWorldFactory.locationMap);
		this.setGui(carGui);
		SetUpWorldFactory.cityPanel.addGui(carGui);
		this.startThread();
		
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
		passenger.p.msgWeHaveArrived(destination);
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
