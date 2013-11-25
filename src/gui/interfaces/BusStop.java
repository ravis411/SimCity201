package gui.interfaces;

public interface BusStop {
	
	
	/**
	 * Lets the BusStop know there is a Passenger waiting
	 */
	public abstract void msgWaitingForBus(Passenger p);
	
	/**
	 * Lets the BusStop know that a Bus has arrived
	 */
	public abstract void msgAtStop();
	
	/**
	 * Lets the BusStop know that a passenger has boarded the bus 
	 * so it can remove the passenger from the passenger from the
	 * list of waiting passengers.
	 */
	
	public abstract void msgNewPassenger(Passenger p);
	
	public abstract void msgLeavingStop();
	
}
