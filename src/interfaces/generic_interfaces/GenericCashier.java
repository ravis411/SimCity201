package interfaces.generic_interfaces;

import interfaces.MarketManager;
import Person.Role.Employee;

public abstract class GenericCashier extends Employee{

	private double money = 0.00;
	
	protected GenericCashier(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	
	public double getMoney(){
		return money;
	}

	public void msgCashierHereIsMarketBill(int orderPrice, MarketManager market) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void sendMoneyToBank(){
		// TODO Auto-generated method stub
	}

	
	/**
	 * A message received by a bank teller signaling that the deposit has been received. 
	 * @param transactionAmount - the amount that has been transferred. Subtract this from your current capital. 
	 */
	public void msgReceivedDeposit(double transactionAmount) {
		
	}

}
