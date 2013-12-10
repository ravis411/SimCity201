package mikeRestaurant;

import mikeRestaurant.interfaces.Waiter;

public class OldWaiterRole extends WaiterRole {

	public OldWaiterRole(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveOrderToCook(MyCustomer customer) {
		// TODO Auto-generated method stub
			DoGiveOrderToCook(customer);
			//wait for the gui to get to the cook
			try {
				atCook.acquire();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			cook.msgHereIsAnOrder(this, customer.choice, customer.table);
			customer.state = CustomerState.CustomerWaitingForFood;
			
			//stateChanged();
		
	}

	
}
