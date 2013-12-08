package ryansRestaurant.interfaces;

import java.util.List;




public interface RyansCustomer {
	

	public abstract void gotHungry();
	
	public abstract void msgIntroduceWaiter(RyansWaiter waiter);

	public abstract void msgSitAtTable(List<String> menu, RyansCashier cashier);
	
	public abstract void msgWhatWouldYouLike();
	
	public abstract void msgOutOfChoice(List<String> menu);
	
	public abstract void msgHereIsYourFood();
	
	public abstract void msgHereIsCheck(double total);
	
	public abstract void msgHereIsChange(double change);
}