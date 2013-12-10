package interfaces.generic_interfaces;

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

}
