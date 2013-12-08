package ryansRestaurant.test.mock;


import java.util.List;

import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansWaiter;

/**
 * A sample MockCustomer built to unit test a RyansCashierRole.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements RyansCustomer {

	/**
	 * Reference to the RyansCashier under test that can be set by the unit test.
	 */
	public RyansCashier cashier;
	public EventLog log = new EventLog();
	
	

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgHereIsCheck(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.msgHereIsPayment(0, this);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.msgHereIsPayment(Math.ceil(total), this);

		}else{
			//test the normative scenario
			cashier.msgHereIsPayment(total, this);
		}
	}

	
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(List<String> menu, RyansCashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(List<String> menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIntroduceWaiter(RyansWaiter waiter) {
		// TODO Auto-generated method stub
		
	}

}
