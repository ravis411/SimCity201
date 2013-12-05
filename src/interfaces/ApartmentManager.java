package interfaces;

import residence.HomeRole;

/**
 * ApartmentManagerRole interface
 *
 * @author Dylan Resnik
 *
 */

public interface ApartmentManager {
	public void msgRentPaid (Home h, int amount);
	public void msgBrokenFeature(String name, Home h);
}