package building;

import gui.Building.BuildingPanel;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.Collection;
import java.util.Collections;

import kushrestaurant.HostRole;
//import kushrestaurant.OldWaiterRole;
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
			if(r instanceof GenericHost){
				hasHost = true;
			}else if(r instanceof GenericCook){
				hasCook = true;
			}else if(r instanceof GenericCashier){
				hasCashier = true;
			}else if(r instanceof GenericWaiter ){
				hasWaiter = true;
			}
		}
		
		return hasHost && hasCook && hasWaiter && hasCashier;
	}
	
	public GenericHost getHostRole(){
			for(Role r : inhabitants){
				if(r instanceof GenericHost){
					return (GenericHost) r;
				}
			}
		
		return null;
	}
	
	public GenericCashier getCashierRole(){
		for(Role r : inhabitants){
			if(r instanceof GenericCashier){
				return (GenericCashier) r;
			}
		}
		
		return null;
	}
		
	public GenericCook getCookRole(){
		for(Role r : inhabitants){
			if(r instanceof GenericCook){
				return (GenericCook) r;
			}
		}
		return null;
	}

	@Override
	public void notifyEmployeesTheyCanLeave() {
		// TODO Auto-generated method stub
		/*for(Role r : inhabitants){
			if(r instanceof Employee){
				r.deactivate();
				r.getPerson().msgYouCanLeave();
			}
		}*/
	}
}
