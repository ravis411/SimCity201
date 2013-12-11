package interfaces.generic_interfaces;

import trace.AlertLog;
import trace.AlertTag;
import interfaces.Person;
import building.BuildingList;
import building.Restaurant;
import Person.Role.Employee;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;

public abstract class GenericHost extends Employee{


	protected GenericHost(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	
	public void deactivate(){
		if(this.getPerson().getCurrentShift() == ShiftTime.NONE){
			super.deactivate();
			workState = WorkState.ReadyToLeave;
		}
	}

	public abstract void addWaiter(GenericWaiter waiter);
	
	@Override
	public void setPerson(Person customerPerson) {
		// TODO Auto-generated method stub
		Person p = this.getPerson();
		super.setPerson(customerPerson);
		Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
		if(rest.getHostRole() != null){
			//kill();
			p.stateChanged();
		}
		
	}
	
}
