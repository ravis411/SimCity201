package kushrestaurant;
import kushrestaurant.RevolvingStand;

public class NewWaiterRole extends WaiterRole {

	public NewWaiterRole(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void HereIsOrder(MyCustomer c) {
		// TODO Auto-generated method stub
		print("Placing order of "+ c.choice+ " on revolving stand");
		
		 this.cook.getRevolvingStand().addOrder(this, c.choice, c.table);
	}

}
