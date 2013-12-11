package gui.MockAgents;

import gui.Building.ResidenceBuildingPanel;
import gui.agentGuis.PersonGui;
import interfaces.Person;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Person.PersonAgent;
import Person.Role.Role;
import Person.Role.ShiftTime;
import agent.Agent;

public class PseudoPerson extends Agent implements Person{

	String name;
	PersonGui agentGui = null;
	Queue<String> StopsQueue = new LinkedList<>(); //<--a list of the places to go to
	
	int times = 0;
	
	boolean test = true;
	
	public PseudoPerson(String name){
		this.name = name;
		StopsQueue.offer("Bus Stop 1");
		StopsQueue.offer("Bus Stop 2");
		StopsQueue.offer("Bus Stop 3");
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		System.out.println("Closest Stop: " + agentGui.DoGoToClosestBusStop());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Arrived at: " + agentGui.DoRideBusTo("Bus Stop 6"));
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

	@Override
	public void msgWeHaveArrived(String currentDestination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgINeedMoney(double moneyNeeded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouHaveALoan(double loan) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgGoToMarket(String item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReceiveSalary(double salary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayBackLoanUrgently() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayBackRentUrgently() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddObjectToBackpack(String object, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPartyInvitation(Person p, Calendar rsvpDeadline,
			Calendar partyTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmComing(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmNotComing(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoneyNeeded(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void stateChanged(){
		// TODO Auto-generated method stub

	}
	@Override
	
	public int getAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMoneyNeeded() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public ResidenceBuildingPanel getHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgReportForWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouCanLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dateAction(int month, int day, int hour, int minute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workIsOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialRole(Role roleFromString, String string,
			ShiftTime shift) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateCurrentRole() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ShiftTime getCurrentShift() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgRespondToInviteUrgently(Person myPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPartyOver(Person myPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Person> getFriends() {
		// TODO Auto-generated method stub
		return null;
	}
}
