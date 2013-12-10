package byronRestaurant;

import trace.AlertLog;
import trace.AlertTag;

public class NewWaiterRole extends WaiterRole {

	public NewWaiterRole(String location) {
		super(location);
	}

	@Override
	protected void giveOrderToCook(MyCustomer customer) {
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, myPerson.getName(),"Giving " + customer.cust + "'s choice of " + customer.choice + " to cook");
		customer.state = CustomerState.noAction;
		waiterGui.doGoToKitchen();
		try{
			atKitchen.acquire();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		cook.getRevolvingStand().addOrder(this, customer.table, customer.choice);
		cashier.msgHereIsOrder(customer.table, customer.choice, this);
		waiterGui.DoLeaveCustomer();
	}

}
