package residence.interfaces;

/**
 * ApartmentManagerRole interface
 *
 * @author Dylan Resnik
 *
 */

public interface ApartmentManager {
	public void msgCollectRent();
	public void msgRentPaid (/*Agent person*/Home h, int amount);
	public void msgBrokenFeature(String name, Agent p);
}