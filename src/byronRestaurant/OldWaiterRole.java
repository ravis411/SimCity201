package byronRestaurant;

import trace.AlertLog;
import trace.AlertTag;

public class OldWaiterRole extends WaiterRole {

	public OldWaiterRole(String location) {
		super(location);
	}

	@Override
	protected void giveOrderToCook(MyCustomer customer) {
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(),"Giving " + customer.cust + "'s choice of " + customer.choice + " to cook");
		customer.state = CustomerState.noAction;
		cook.msgHereIsAnOrder(this, customer.table, customer.choice);
		cashier.msgHereIsOrder(customer.table, customer.choice, this);
		waiterGui.DoLeaveCustomer();


	}

}
