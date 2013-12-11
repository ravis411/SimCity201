package building;

import gui.Building.BuildingPanel;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;
import Person.Role.Employee;
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
		synchronized(inhabitants){
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
		}
		
		return hasHost && hasCook && hasWaiter && hasCashier;
	}
	
	public GenericHost getHostRole(){
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof GenericHost){
					return (GenericHost) r;
				}
			}
		}
		
		return null;
	}
	
	public GenericCashier getCashierRole(){
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof GenericCashier){
					return (GenericCashier) r;
				}
			}
		}
		
		return null;
	}
		
	public GenericCook getCookRole(){
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof GenericCook){
					return (GenericCook) r;
				}
			}
		}
		return null;
	}
	
	public int getNumCustomers(){
		int ans = 0;
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof GenericCustomer){
					ans++;
				}
			}
		}
		return ans;
	}

	

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		super.addRole(r);
		if(ready){
			if(r instanceof Employee){
				Employee e = (Employee) r;
				if(r instanceof GenericWaiter){
					GenericWaiter gw = (GenericWaiter) r;
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					gw.setHost(rest.getHostRole());
					rest.getHostRole().addWaiter(gw);
					gw.setCashier(rest.getCashierRole());
					gw.setCook(rest.getCookRole());
				}
			}
		}
	}

	@Override
	public void notifyEmployeesTheyCanLeave() {
		
	}

}
