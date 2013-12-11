package gui.agentGuis;

import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import agent.Agent;



/** This
 * 
 * @author Ryan
 *
 */
public class DrunkPersonAgent extends Agent{

	
	
	public DrunkPersonAgent(String name) {
		this.name = name;
		agentGui = new DrunkPersonGui(name);
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
		stateChanged();
	}
		
	public void setStartLocation(String location){
		agentGui.setCurrentLocation(location);
		stateChanged();
	}
	
	//////////////////////////////
	//Scheduler
	
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(goTo){
			DoGoTo(destination);
			return true;
		}
		if(state == AgentState.spazzing){
			DoGoGetKilled();
			return true;
		}
		if(state == AgentState.dead){
			TeleportToDestination(defaultStartLocation);
			state = AgentState.none;
			return true;
		}
		
		return false;
	}

	
	//Actions
	private void DoGoGetKilled(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, getName(), "Running into the middle of the road.");
		state = AgentState.dead;
		agentGui.DoGetHitByCar();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {	}
		TeleportToDestination(defaultStartLocation);
	}
	
	private void TeleportToDestination(String destination){
		agentGui.setCurrentLocation(destination);
		defaultStartLocation = destination;
		//state = AgentState.none;
	}
	
	private void DoGoTo(String destination){
		defaultStartLocation = destination;
		agentGui.DoGoTo(destination);
		if(this.destination.equals(destination)){
			goTo = false;
			destination = null;
			TeleportToDestination(destination);
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
	DrunkPersonGui agentGui = null;
	String name = new String();
	
	public String defaultStartLocation = "Food Court";
	
}
