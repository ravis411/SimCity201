package kushrestaurant.interfaces;
import kushrestaurant.*;
import kushrestaurant.HostRole.Table;


public interface Cook {

	public abstract void MsgHereisTheOrder(Waiter w, Customer c, Table t, String s);
	
	public abstract void msgCantRestock();
	public abstract void takenFood(String x);
	public abstract RevolvingStand getRevolvingStand();
	
}
