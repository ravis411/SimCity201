package gui.MockAgents;

import java.util.LinkedList;
import java.util.Queue;

import gui.agentGuis.PersonGui;
import agent.Agent;

public class MockPerson extends Agent{

	String name;
	PersonGui agentGui = null;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the places to go to
	
	public MockPerson(String name){
		this.name = name;
		
		StopsQueue.add("Bus Stop 5");
		StopsQueue.add("Bus Stop 1");
		StopsQueue.add("Bus Stop 6");
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		GoToLocation(name);

		
		return true;
	}

	
	private void GoToLocation(String location){
				
		location = StopsQueue.poll();//<--removes location from front of queue
		StopsQueue.add(location);//<--adds location to end of queue
		print("Going to " + location);
		
		agentGui.DoGoTo(location);
		
		print("Arrived at " + location);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void setAgentGui(PersonGui gui){
		this.agentGui = gui;
	}
	
	public String getName(){
		return name;
	}

	public String toString(){
		return "" + name;
	}
}
