package mikeRestaurant.test.mock;

import java.text.DecimalFormat;
import java.util.Map;

import mikeRestaurant.interfaces.Customer;
import mikeRestaurant.interfaces.Waiter;

public class MockCustomer extends Mock implements Customer{

	public EventLog log;
	
	public MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}

	@Override
	public void msgPaymentResponse(boolean approved, Double change) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		String approvedMessage = approved ? "Payment approved" : "Payment rejected";
		System.out.println(approvedMessage+"\tChange Due: $"+df.format(change.doubleValue()));
		log.add(new LoggedEvent(approvedMessage+"\tChange Due: $"+df.format(change.doubleValue())));
	}
	
	//-------------------------NOT USED FOR CASHIER TEST---------------------------//

	@Override
	public void msgFollowMeToTable(Map<String, Double> menu, Waiter sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrivedAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNotifyRestIsFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsNewMenu(Map<String, Double> menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWaiterAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrivedAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrivedAtStart() {
		// TODO Auto-generated method stub
		
	}

}
