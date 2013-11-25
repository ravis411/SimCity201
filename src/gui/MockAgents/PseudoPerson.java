package gui.MockAgents;

import java.util.LinkedList;
import java.util.Queue;

import gui.agentGuis.PersonGui;
import agent.Agent;

public class PseudoPerson extends Agent{

	String name;
	PersonGui agentGui = null;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the places to go to
	
	int times = 0;
	
	boolean test = true;
	
	public PseudoPerson(String name){
		this.name = name;
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		System.out.println("Closest Stop: " + agentGui.DoGoToClosestBusStop());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Arrived at: " + agentGui.DoRideBusTo("Building 12"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Going to building 7");
		agentGui.DoGoTo("Building 7");
		
		GoToLocation(name);
		times++;
		if(times >= 100)
			return false;
		
		return true;
	}

	
	private void GoToLocation(String location){
				
		location = StopsQueue.poll();//<--removes location from front of queue
		StopsQueue.offer(location);//<--adds location to end of queue
		print("Going to " + location);
		
		agentGui.DoGoTo(location);
		
		print("Arrived at " + location);
		try {
			Thread.sleep(1000);
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
