package building;

import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;
import ryansRestaurant.RyansCookRole;
import gui.Building.BuildingPanel;
import Person.Role.Employee;
import Person.Role.Role;

public abstract class Workplace extends Building{
	
	public Workplace(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}
	
	private boolean ready = false;

	public static int DAY_SHIFT_HOUR = 9;
	public static int DAY_SHIFT_MIN = 0;
	
	public static int NIGHT_SHIFT_HOUR = 17;
	public static int NIGHT_SHIFT_MIN = 0;
	
	public static int END_SHIFT_HOUR = 0;
	public static int END_SHIFT_MIN = 0;
	
	/**
	 * Message sent to the workplace that it is time to open
	 */
	public abstract void open();
	
	/**
	 * Message sent to the workplace that it is time to close
	 */
	public abstract void close();
	
	/**
	 * Find out who is in the store and determine if the store is open or not
	 * @return
	 */
	public abstract boolean isOpen();
	
	public abstract void notifyEmployeesTheyCanLeave();
	
	public void getReadyForWork(){
		for(Role role : inhabitants){
			if(role instanceof Employee){
				Employee e = (Employee) role;
				
				if(e instanceof GenericWaiter){
					GenericWaiter gw = (GenericWaiter) e;
					//System.out.println(e.getWorkLocation());
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					gw.setHost(rest.getHostRole());
					rest.getHostRole().addWaiter(gw);
					gw.setCashier(rest.getCashierRole());
					gw.setCook(rest.getCookRole());
					//return gw;
				}else if(e instanceof GenericHost){
					GenericHost gh = (GenericHost) e;
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					//return gh;
				}else if(e instanceof GenericCook){
					GenericCook gc = (GenericCook) e;
					if(gc instanceof RyansCookRole){
						RyansCookRole rcr = (RyansCookRole) gc;
						Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(rcr.getWorkLocation());
						rcr.setCashier(rest.getCashierRole());
					}
					//return gc;
				}else if(e instanceof GenericCashier){
					GenericCashier gc = (GenericCashier) e;
					//return gc;
				}else{
					//return e;
				}
				
			}
		}
		
		for(Role r : inhabitants){
			System.err.println("Activating: " + r.getNameOfRole() );
				r.getPerson().workIsOpen();
		}
	}

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		super.addRole(r);	
		System.err.println("Added "+r.getNameOfRole()+" to "+this.getName());
		if(isOpen() && !ready){
			ready = true;
			getReadyForWork();
		}
	}
	
	public void activateAll(){
		
		for(Role r : inhabitants){
			r.activate();
		}
	}
	
	
	
}
