package kushrestaurant.interfaces;

import java.util.HashMap;

import kushrestaurant.WaiterRole.WaiterEvent;

public interface Market {
	
	public abstract void msgReceivedOrderFromCook(HashMap<String,Integer> h, Cook c);
	public abstract void msgHereIsPayment(int bill);
	public abstract String getName();
	public abstract void msgHereIsGiftCard(double val,double paymeny);
	public abstract void msgNoPayment();

}
