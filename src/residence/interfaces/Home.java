package residence.interfaces;

/**
 * HomeRole interface
 *
 * @author Dylan Resnik
 *
 */

public interface Home {
	public void msgRentDue (int amount);
	public void msgTired();
	public void msgRestockItem (String itemName, int quantity);
	public void msgFixedFeature (String name);
}