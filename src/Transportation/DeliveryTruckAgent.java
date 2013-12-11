package Transportation;

import gui.agentGuis.TruckVehicleGui;
import interfaces.MarketDeliveryTruck;
import interfaces.MarketManager;

import java.util.LinkedList;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import agent.Agent;

public class DeliveryTruckAgent extends Agent implements MarketDeliveryTruck{
	
	
	/** Constructor for Delivery Truck
	 * 
	 * @param name The name of the truck Truck 1 for example.
	 * @param marketName The name of the Market Truck belongs to.
	 */
	public DeliveryTruckAgent(String name, String marketName) {
		state = CarState.parked;
		this.name = name;
		
		this.setGui(new TruckVehicleGui(name));
		homeMarket=marketName;
		agentGui.setCurrentLocation(marketName);
		this.startThread();
	}
	
	
	//Data
	String name = null;
	String homeMarket;
	private TruckVehicleGui agentGui = null;
	
	private Queue<String> destination = new LinkedList<>(); 
	private CarState state;
	public enum CarState {parked, driving, atDestination};
	private MarketManager marketManager;
	
	//Messages
	
	
	/** Will add the destination to the queue of places to go.
	 * 
	 */
	public void msgNewDestination(String destination, MarketManager marketManager) {
		this.destination.offer(destination);
		this.state = CarState.driving;
		this.marketManager=marketManager;
		stateChanged();
	}
	

	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {

		if (state == CarState.driving) {
			goToDestination();
			return true;
		}

		if (state == CarState.atDestination){
			
			goHome();
			return true;
		}

		return false;
	}
	
	//Actions



	private void goToDestination() {
		
		if(destination.size() == 0){
			state = CarState.parked;
			AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "No destinations in queue. Parking.");
			return;
		}
		
		String dest;
		dest = destination.poll();
		
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Going to " + dest );
		agentGui.DoGoTo(dest);
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Arrived at " + dest);
		marketManager.msgDeliveryTruckAtDestination();
		
		state = CarState.atDestination;
	}

	private void goHome(){
		String dest;
		dest = homeMarket;
		
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Going to " + dest );
		agentGui.DoGoTo(dest);
		AlertLog.getInstance().logMessage(AlertTag.VEHICLE_GUI, name, "Arrived at " + dest);
		state = CarState.parked;
		marketManager.atHome();
	}
	
	
	
	
	
	
	
	//Utilities
	public void setGui(TruckVehicleGui truckGui){
		this.agentGui = truckGui;
	}
	
	
	
	public String toString(){
		return "" + name;
	}
	
	public String getName(){
		return name;
	}






}
