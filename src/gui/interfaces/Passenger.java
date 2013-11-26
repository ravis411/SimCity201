package gui.interfaces;

public interface Passenger {
	
	
	/**
	 * Notifies the passenger that a bus has arrived
	 */
	public abstract void msgBusIsHere();
	
	/**
	 * Notifies the passenger that the bus is at a particular destination
	 */
	public abstract void msgNextDestination(String location);
	
	/**
	 * Utility getter for name
	 */
	public abstract String getNameOfRole();
}
