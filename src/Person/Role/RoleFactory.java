package Person.Role;

import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;
import restaurant.luca.LucaCookRole;
import ryansRestaurant.RyansCookRole;
import building.Building;
import building.BuildingList;
import building.Restaurant;

public class RoleFactory {
	
	public static Employee employeeFromString(String string, String restLocation){
		try {
			Class c = Class.forName(string);
			Employee e = (Employee) c.getDeclaredConstructor(String.class).newInstance(restLocation);
			//make sure your building is set to appropriate building type in addBuildingPanel method in BuildingsPanels class
			//for example restaurant needs to be specified as a restaurant not a building or the line below
			//"Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());"
			//or else you will get cast error
			
			return e;
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static Role roleFromString(String string){
		Class c;
		try {
			c = Class.forName(string);
			Role r = (Role) c.newInstance();
			if(r instanceof GenericCustomer){
				GenericCustomer gc = (GenericCustomer) r;

				return gc;
			}
			/*
			if(r instanceof Employee){
				Employee e = (Employee) r;
				if(e instanceof GenericWaiter){
					GenericWaiter gw = (GenericWaiter) e;
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					rest.getName();
					gw.setHost(rest.getHostRole());
					gw.setCashier(rest.getCashierRole());
					gw.setCook(rest.getCookRole());
					return gw;
				}else if(e instanceof GenericHost){
					GenericHost gh = (GenericHost) e;
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					return gh;
				}else if(e instanceof GenericCustomer){
					GenericCustomer gc = (GenericCustomer) e;
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					gc.setHost(rest.getHostRole());
					gc.setCashier(rest.getCashierRole());
					return gc;
				}else if(e instanceof GenericCook){
					GenericCook gc = (GenericCook) e;
					return gc;
				}else if(e instanceof GenericCashier){
					GenericCashier gc = (GenericCashier) e;
					return gc;
				}else{
					return e;
				}
				
			}*/
			
			return r;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
