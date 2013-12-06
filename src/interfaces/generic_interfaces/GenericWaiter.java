package interfaces.generic_interfaces;

import Person.Role.Employee;

public abstract class GenericWaiter extends Employee{

	protected GenericWaiter(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	public abstract void setCook(GenericCook c);
	public abstract void setCashier(GenericCashier c);
	public abstract void setHost(GenericHost h);
	
}
