package ryansRestaurant;

<<<<<<< HEAD
import ryansRestaurant.RyansWaiterRole.MyCustomer;
=======
import trace.AlertLog;
import trace.AlertTag;
>>>>>>> origin/develop

public class RyansOldWaiterRole extends RyansWaiterRole {

	public RyansOldWaiterRole(String workLocation) {
		super(workLocation);
		
	}

	@Override
	protected void Order(MyCustomer c) {
			activity = "Going to order food for " + c.customer;
			try {
				waiterGui.DoGoToCook();
			} catch (Exception e1) {
			}
			state=AgentState.atCook;
			
			activity = "" + c.customer + " would like " + c.choice;
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
<<<<<<< HEAD
				print("EXCEPTION!!!! caught while waiting for order.");
=======
				AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "EXCEPTION!!!! caught while waiting for order.");
>>>>>>> origin/develop
			}
			
			cook.msgHereIsOrder(this, c.choice, c.tableNumber);
			c.s = CustomerState.ordered;
			activity = "Ordered.";

	}

<<<<<<< HEAD
=======
	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return "Ryan's Old Waiter Role";
	}

>>>>>>> origin/develop
}
