package Transportation;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;



public class AddressBook {
	private Map<String, Dimension> addresses = new HashMap<>();
	
	public AddressBook(){
		
	}
	
	/**
	 * 
	 * @param address  A String representation of building or place
	 * @return A Dimension that is the grid position of the building
	 */
	public Dimension getAddress(String address) {
		return new Dimension(addresses.get(address));
	}
}
