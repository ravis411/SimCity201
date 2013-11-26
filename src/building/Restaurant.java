package building;

import gui.Building.BuildingPanel;
import Person.Role.Role;

public class Restaurant extends Workplace {

	public Restaurant(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		boolean hasHost = false, hasCook = false, hasWaiter = false, hasCashier = false;
		for(Role r : inhabitants){
			if(r instanceof RestaurantHostRole){
				hasHost = true;
			}else if(r instanceof CookRole){
				hasCook = true;
			}else if(r instanceof Old)
		}
		return false;
	}
	
	public hostRole

}
