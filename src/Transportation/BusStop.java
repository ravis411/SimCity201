package Transportation;

public interface BusStop {
	
	
	/**
	 * Lets the BusStop know there is a Passenger waiting
	 */
	public abstract void msgWaitingForBus();
	
	/**
	 * Lets the BusStop know that a Bus has arrived
	 */
	public abstract void msgAtStop();
	
	
	
}
