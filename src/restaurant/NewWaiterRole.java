package restaurant;

import trace.AlertLog;
import trace.AlertTag;


public class NewWaiterRole extends WaiterAgent {

	public NewWaiterRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void TakeOrderToCook(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Taking " + c.customer.getName() + "'s order to cook.");
		waiterGui.DoGoToCookingArea();
		try {
			atCookingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.getRevolvingStand().addOrder(this, c.mealChoice, c.customer.getTableNum(), c.customer);
		c.customer.msgOrderOnItsWay();
	}

	@Override
	public String getNameOfRole() {
		return "restaurant.NewWaiterRole";
	}

}
