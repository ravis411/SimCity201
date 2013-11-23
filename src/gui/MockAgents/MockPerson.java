package gui.MockAgents;

import java.util.LinkedList;
import java.util.Queue;

import gui.agentGuis.PersonGui;
import agent.Agent;

public class MockPerson extends Agent{

	String name;
	PersonGui agentGui = null;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the places to go to
	
	int times = 0;
	
	public MockPerson(String name){
		this.name = name;
		
		StopsQueue.add("Bus Stop 5");
		StopsQueue.add("Bus Stop 4");
		StopsQueue.add("Bus Stop 1");
		StopsQueue.add("Bus Stop 3");
		StopsQueue.add("Bus Stop 6");
		StopsQueue.add("Bus Stop 2");
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		GoToLocation(name);
		times++;
		if(times >= 100)
			return false;
		
		return true;
	}

	
	private void GoToLocation(String location){
				
		location = StopsQueue.poll();//<--removes location from front of queue
		StopsQueue.add(location);//<--adds location to end of queue
		print("Going to " + location);
		
		agentGui.DoGoTo(location);
		
		print("Arrived at " + location);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			print("Exceipton caught while waiting at location!!!!!!!");
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
