package interfaces.generic_interfaces;

import Person.Role.Role;
import building.BuildingList;
import building.Restaurant;

public abstract class GenericCustomer extends Role {
	
	public abstract void setCashier(GenericCashier c);
	public abstract void setHost(GenericHost h);
	
	public abstract void gotHungry();
	
	public void setupCustomer(String location){
		Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(location);
		if(rest.isOpen()){
			setCashier(rest.getCashierRole());
			setHost(rest.getHostRole());
		}
	}
	
}
