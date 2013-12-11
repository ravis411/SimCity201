package restaurant.interfaces.luca;

import java.util.Map;

public interface LucaCustomer {
	

	public abstract void gotHungry();
	
	public abstract void msgYouAreOffWaitList();


	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgHereIsAMenu(Map<String, Integer> menuUnmodifiable);
	public abstract void msgCustomerWhatWouldYouLike();
	public abstract void msgCustomerWhatIsYourSecondChoice(String choice) ;

	public abstract void msgCustomerHereIsYourChoice(String choice);
	public abstract void msgCustomerHereIsYourCheck(double customersTab);

	
	public abstract void msgAnimationFinishedLeaveRestaurant();
	
	public abstract void msgCustomerHereIsChange(double d, double moneyIOwe) ;

	public abstract String getName();

	public abstract boolean getWillWaitforFood();

	public abstract void msgSitAtTable(int customerTableNumber,	LucaWaiter w);

	public abstract boolean getHasEnoughMoney();

	

}