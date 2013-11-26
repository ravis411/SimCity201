package ryansRestaurant.interfaces;

import java.util.List;




public interface Customer {
	

	public abstract void gotHungry();
	
	public abstract void msgIntroduceWaiter(Waiter waiter);

	public abstract void msgSitAtTable(List<String> menu, Cashier cashier);
	
	public abstract void msgWhatWouldYouLike();
	
	public abstract void msgOutOfChoice(List<String> menu);
	
	public abstract void msgHereIsYourFood();
	
	public abstract void msgHereIsCheck(double total);
	
	public abstract void msgHereIsChange(double change);
}