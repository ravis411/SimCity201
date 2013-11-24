package residence.interfaces;

import residence.HomeRole;

/**
 * ApartmentManagerRole interface
 *
 * @author Dylan Resnik
 *
 */

public interface ApartmentManager {
	public void msgCollectRent();
	public void msgRentPaid (HomeRole h, int amount);
	public void msgBrokenFeature(String name, HomeRole h);
}