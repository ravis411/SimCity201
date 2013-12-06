package jeffreyRestaurant.interfaces;

public interface Market {
	public abstract void msgNeedFood(String food, int quantity);
	
	public abstract void msgOrderPayment(String food, Double price);
}
