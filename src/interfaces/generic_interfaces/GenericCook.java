package interfaces.generic_interfaces;

import trace.AlertLog;
import trace.AlertTag;
import interfaces.Person;
import building.BuildingList;
import building.Restaurant;
import Person.Role.Employee;
import Person.Role.ShiftTime;

public abstract class GenericCook extends Employee{

	private GenericCashier cashier;
	
	protected GenericCook(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	
	public void deactivate(){
		if(this.getPerson().getCurrentShift() == ShiftTime.NONE){
			super.deactivate();
			workState = WorkState.ReadyToLeave;
		}
	}
	
	@Override
	public void setPerson(Person customerPerson) {
		// TODO Auto-generated method stub
		Person p = this.getPerson();
		super.setPerson(customerPerson);
		Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
		if(rest.getCookRole() != null){
			//kill();
			p.stateChanged();
		}
		
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
