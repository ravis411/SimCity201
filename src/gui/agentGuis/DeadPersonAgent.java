package gui.agentGuis;

import agent.Agent;



/** This
 * 
 * @author Ryan
 *
 */
public class DeadPersonAgent extends Agent{

	
	
	public DeadPersonAgent(String name) {
		this.name = name;
		agentGui = new DeadPersonGui(name);
	}
	
	
	//Messages
	public void msgRunIntoTheRoad(){
		
	}
		
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	
	//Actions
	
	
	public String toString(){
		return getName();
	}
	
	public String getName(){
		return name;
	}
	
	
	
	
	
	//DATA
	enum AgentState {none, spazzing, dead};
	DeadPersonGui agentGui = null;
	String name = new String();
	
}
