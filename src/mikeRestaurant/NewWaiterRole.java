package mikeRestaurant;

import mikeRestaurant.WaiterRole.CustomerState;
import building.BuildingList;
import building.Restaurant;

public class NewWaiterRole extends WaiterRole{

	public NewWaiterRole(String workLocation) {
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
		
		cook.getRevolvingStand().addOrder(this, customer.choice, customer.table);
		customer.state = CustomerState.CustomerWaitingForFood;
	}
	
	
	
	

}
