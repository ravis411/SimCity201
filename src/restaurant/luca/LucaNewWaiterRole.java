package restaurant.luca;


public class LucaNewWaiterRole extends LucaWaiterRole {

	public LucaNewWaiterRole(String restLocation) {
		super(restLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void giveCookOrder(MyCustomers customer){
		DoGiveCookOrder(customer);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.getRevolvingStand().addOrder(this, customer.getCustomerChoice(), customer.getCustomerTableNumber());
		print(customer.getCustomer() + "'s Order given to cook Revloving Stand");
		waiterGui.showOrderInAnimation("", "");
		customer.setCookHasOrder(true);
		waiterGui.DoLeave();
		
	}

}
