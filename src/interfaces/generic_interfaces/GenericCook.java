package interfaces.generic_interfaces;

import Person.Role.Employee;

public abstract class GenericCook extends Employee{

	private GenericCashier cashier;
	
	protected GenericCook(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	
	public GenericCashier getCashier(){
		return cashier;
	}
	
	public void setCashier(GenericCashier cashier){
		this.cashier = cashier;
	}

	public void msgOrderFilled(int numberThatIsAssociatedWithFoodsMenuNumber,
			int amountReadyToBeShipped) {
		// TODO Auto-generated method stub
		
	}

	public void msgOrderNotFilled(int numberThatIsAssociatedWithFoodsMenuNumber) {
		// TODO Auto-generated method stub
		
	}

	public void msgOrderPartiallyFilled(
			int numberThatIsAssociatedWithFoodsMenuNumber,
			int amountReadyToBeShipped, int amountNotBeingShippedFromMarket) {
		// TODO Auto-generated method stub
		
	}


}
