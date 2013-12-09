package kushrestaurant.interfaces;
import kushrestaurant.*;
import kushrestaurant.HostRole.Table;
import kushrestaurant.MarketRole.Order2;

public interface Cook {

	public abstract void MsgHereisTheOrder(Waiter w, Customer c, Table t, String s);
	public abstract void msgMarketReStock(Order2 o);
	public abstract void msgCantRestock();
	public abstract void takenFood(String x);
	public abstract RevolvingStand getRevolvingStand();
	
}
