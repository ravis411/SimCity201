package gui.interfaces;

import Person.PersonAgent;


public interface BusStop {
	
	
	/**
	 * Lets the BusStop know there is a Passenger waiting
	 */
	public abstract void msgWaitingForBus(Passenger p, String destination);
	
	/**
	 * Lets the BusStop know that a Bus has arrived
	 */
	public abstract void msgArrivedAtStop(Bus bus);
	
	void msgAtBusStop(PersonAgent p, String destStop);
	
	/**
	 * Lets the BusStop know that a passenger has boarded the bus 
	 * so it can remove the passenger from the passenger from the
	 * list of waiting passengers.
	 */
	
	public abstract void msgNewPassenger(Passenger p);
	
	public abstract void msgLeavingStop();
	
}
