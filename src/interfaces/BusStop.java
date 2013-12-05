package interfaces;

import Person.PersonAgent;
import Transportation.BusAgent;


public interface BusStop {
	
	
	/**
	 * Lets the BusStop know there is a Passenger waiting
	 */
	//public abstract void msgWaitingForBus(Passenger p, String destination);
	
	/**
	 * Lets the BusStop know that a Bus has arrived
	 */
	public abstract void msgArrivedAtStop(Bus bus);
	
	/**
	 * Let's the BusStop know that a person has arrive and is now
	 * waiting for the bus.
	 * @param person
	 * @param destinationStop
	 */
	
	public abstract void msgAtBusStop(Person person, String destinationStop);

	
	/**
	 * Lets the BusStop know that a passenger has boarded the bus 
	 * so it can remove the passenger from the passenger from the
	 * list of waiting passengers.
	 */
	
	//public abstract void msgNewPassenger(Passenger p);
	
	//public abstract void msgLeavingStop();
	
}
