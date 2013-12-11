package restaurant.luca;

import java.util.concurrent.ExecutionException;

import restaurant.interfaces.luca.LucaCustomer;

public class LucaOldWaiterRole extends LucaWaiterRole {

	public LucaOldWaiterRole(String restLocation) {
		super(restLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers customer)
			throws ExecutionException {
		DoGiveCookOrder(customer);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgCookHeresAnOrder(customer.getCustomerTableNumber(), customer.getCustomerChoice(), this);
		print(customer.getCustomer() + "'s Order given to cook");
		waiterGui.showOrderInAnimation("", "");
		customer.setCookHasOrder(true);
		waiterGui.DoLeave();

	}



}
