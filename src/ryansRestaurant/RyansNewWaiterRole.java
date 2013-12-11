package ryansRestaurant;

import Person.Role.Role;

public class RyansNewWaiterRole extends RyansWaiterRole {

	public RyansNewWaiterRole(String workLocation) {
		super(workLocation);
		
	}

	@Override
	protected void Order(MyCustomer c) {
			activity = "Adding " + c.customer + "'s order to stand.";
			
			cook.getRevolvingStand().addOrder(this, c.choice, c.tableNumber);
			
			c.s = CustomerState.ordered;
			activity = "Ordered.";
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return Role.RESTAURANT_RYAN_NEW_WAITER_ROLE;
	}

}
