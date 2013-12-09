package mikeRestaurant.test.mock;

import java.text.DecimalFormat;
import java.util.Set;

import mikeRestaurant.Table;
import mikeRestaurant.interfaces.Customer;
import mikeRestaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{

	public EventLog log;
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}

	@Override
	public void msgHereIsCheck(Customer sender, double price) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		log.add(new LoggedEvent(sender.getName()+" owes a bill of $"+df.format(price)));
	}
	
	//--------------------NOT USED IN CASHIER TEST ----------------------//

	@Override
	public void msgSitAtTable(Customer customer, Table table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForCheck(Customer sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(Customer sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(String choice, Customer sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Waiter waiter, String choice, Table table, int grillPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFoodForOrder(Waiter waiter, String choice,
			Table table, Set<String> remainingFood) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEatingAndPaying(Customer sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGuiAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGuiAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGuiAtStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGuiAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakReply(boolean canGoOnBreak) {
		// TODO Auto-generated method stub
		
	}
	
	

}
