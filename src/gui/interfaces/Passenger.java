package gui.interfaces;
	/**
	 * Handles the traveling messages for the Person
	 */
public interface Passenger {
	/**
	 * Notifies the passenger that a bus has arrived
	 */
	public abstract void msgBusIsHere();
	/**
	 * Notifies the passenger that the bus is at a particular destination
	 */
	public abstract void msgArrivedAtDestination();
	
}
