package Person.Role;

import gui.interfaces.Passenger;

public class PassengerRole extends Role implements Passenger{
	//Data
	private String name;
	//Messages
	
	public void msgBusIsHere() {
		
	}
	
	public void msgArrivedAtDestination() {
		
	}
	//Scheduler
	@Override
	public boolean pickAndExecuteAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGoGetFood() {
		// Dead End method
		//Meant to tell person that he can't get food right now
		return false;
	}

	@Override
	public String getName() {
		return name;
	}
		
	
	//Actions
}
