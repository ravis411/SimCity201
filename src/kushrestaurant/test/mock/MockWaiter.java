package kushrestaurant.test.mock;

import kushrestaurant.HostAgent.Table;
import kushrestaurant.interfaces.Cook;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Host;
import kushrestaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgLeavingTable(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSeatCustomer(Customer c, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadytoOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereisChoice(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetFoodFromCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDontHaveThis(String food, Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouCantGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantCheck(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Customer c, double check) {
		// TODO Auto-generated method stub
		System.out.println("MockWaiter: Received check from cashier of "+ check+ " for customer "+ c.getName());
	}

	@Override
	public void changeBreakEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeBreakEvent2() {
		// TODO Auto-generated method stub
		
	}

	
	
}
