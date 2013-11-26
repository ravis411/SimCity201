package gui.interfaces;

public interface Bus {
	public abstract void msgGettingOnBus(Passenger p);
	
	public abstract void msgFreeToLeave();
	
	public abstract void msgArrivedAtStop();
	
	public abstract void msgGettingOffBus(Passenger p);
}
