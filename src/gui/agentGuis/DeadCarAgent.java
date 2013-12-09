package gui.agentGuis;

import agent.Agent;



/** This
 * 
 * @author Ryan
 *
 */
public class DeadCarAgent extends Agent{

	
	
	public DeadCarAgent(String name) {
		this.name = name;
		agentGui = new DeadCarVehicleGui(name);
		this.startThread();
	}
	
	
	//Messages
	
	
	public void msgRunIntoTheRoad(){
		state = AgentState.spazzing;
		stateChanged();
	}
	
	
	
	public void msgGoTo(String destination){
		goTo = true;
		this.destination = destination;
	}
		

	public void setStartLocation(String location){
		
	}
	
	//////////////////////////////
	//Scheduler
	
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(goTo){
			DoGoTo(destination);
		}
		if(state == AgentState.spazzing){
			DoGoGetKilled();
			return true;
		}
		if(state == AgentState.dead){
			TeleportToDestination(defaultStartLocation);
			return true;
		}
		
		return false;
	}

	
	//Actions
	private void DoGoGetKilled(){
		state = AgentState.dead;
		agentGui.DoGetHitByCar();
	}
	
	private void TeleportToDestination(String destination){
		agentGui.setCurrentLocation(destination);
		state = AgentState.none;
	}
	
	private void DoGoTo(String destination){
		
		agentGui.DoGoTo(destination);
		if(this.destination.equals(destination)){
			goTo = false;
			destination = null;
		}
	}
	
	
	
	public String toString(){
		return getName();
	}
	
	public String getName(){
		return name;
	}
	
	
	
	
	
	//DATA
	enum AgentState {none, spazzing, dead, hasDestination, newStartLocation};
	AgentState state = AgentState.none;
	String destination = null;
	boolean goTo = false;
	DeadCarVehicleGui agentGui = null;
	String name = new String();
	
	public String defaultStartLocation = "Food Court";
	
}
