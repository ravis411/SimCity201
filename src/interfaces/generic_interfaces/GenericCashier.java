package interfaces.generic_interfaces;

import trace.AlertLog;
import trace.AlertTag;
import interfaces.MarketManager;
import interfaces.Person;
import Person.Role.Employee;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import building.BuildingList;
import building.Restaurant;

public abstract class GenericCashier extends Employee{

	private double money = 0.00;
	
	protected GenericCashier(String workLocation) {
		super(workLocation);
		// TODO Auto-generated constructor stub
	}
	
	public double getMoney(){
		return money;
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
		if(rest.getCashierRole() != null){
			//kill();
			p.stateChanged();
		}
		
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
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
