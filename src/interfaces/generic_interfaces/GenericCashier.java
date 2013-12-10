package interfaces.generic_interfaces;

import interfaces.MarketManager;
import Person.Role.Employee;

public abstract class GenericCashier extends Employee{

	protected GenericCashier(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}

	public void msgCashierHereIsMarketBill(int orderPrice, MarketManager market) {
		// TODO Auto-generated method stub
		
	}

}
