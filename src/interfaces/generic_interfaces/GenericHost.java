package interfaces.generic_interfaces;

import Person.Role.Employee;

public abstract class GenericHost extends Employee{


	protected GenericHost(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}

	public abstract void addWaiter(GenericWaiter waiter);
	
}
