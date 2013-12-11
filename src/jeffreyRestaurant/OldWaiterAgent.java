package jeffreyRestaurant;

import Person.Role.Role;

public class OldWaiterAgent extends WaiterAgent {

	public OldWaiterAgent(String location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void tellCookOrder(myCustomer mc) {
		DoGoToCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ck.msgHereIsOrder(this, mc.ch, mc.table);
		mc.s = CustomerState.Ordered;
		hostGui.DoLeaveCustomer();
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_JEFFREY_OLD_WAITER_ROLE;
	}
	
}
