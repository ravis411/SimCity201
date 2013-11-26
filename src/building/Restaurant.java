package building;

import gui.Building.BuildingPanel;
import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.HostRole;
import restaurant.NewWaiterRole;
import restaurant.OldWaiterRole;
import Person.Role.Role;

public class Restaurant extends Building implements Workplace {

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
			if(r instanceof HostRole){
				hasHost = true;
			}else if(r instanceof CookRole){
				hasCook = true;
			}else if(r instanceof CashierRole){
				hasCashier = true;
			}else if(r instanceof OldWaiterRole || r instanceof NewWaiterRole){
				hasWaiter = true;
			}
		}
		
		System.out.println( hasHost && hasCook && hasWaiter && hasCashier);
		return hasHost && hasCook && hasWaiter && hasCashier;
	}
	
	public HostRole getHostRole(){
			for(Role r : inhabitants){
				if(r instanceof HostRole){
					return (HostRole) r;
				}
			}
		
		return null;
	}
	
	public CashierRole getCashierRole(){
		for(Role r : inhabitants){
			if(r instanceof CashierRole){
				return (CashierRole) r;
			}
		}
	
	return null;
	}
	

}
