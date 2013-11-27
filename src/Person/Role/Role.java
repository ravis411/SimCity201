package Person.Role;

import interfaces.Person;
import Person.PersonAgent;

public abstract class Role {

	private boolean isActive = false;
	protected Person myPerson;
	
	public final static String MARKET_CUSTOMER_ROLE = "MarketEmployee.MarketCustomerRole";
	public final static String MARKET_EMPLOYEE_ROLE = "MarketEmployee.MarketEmployeeRole";
	public final static String MARKET_MANAGER_ROLE = "MarketEmployee.MarketManagerRole";
	public final static String BANK_CLIENT_ROLE = "bank.BankClientRole";
	public final static String BANK_TELLER_ROLE = "bank.BankTellerRole";
	public final static String LOAN_TELLER_ROLE = "bank.LoanTellerRole";
	public final static String PASSENGER_ROLE = "Person.Role.PassengerRole";
	public final static String HOME_ROLE = "residence.HomeRole";
	public final static String RESTAURANT_CUSTOMER_ROLE = "restaurant.RestaurantCustomerRole";
	public final static String RESTAURANT_WAITER_ROLE = "restaurant.OldWaiterRole";
	public final static String RESTAURANT_NEW_WAITER_ROLE = "restaurant.NewWaiterRole";
	public final static String RESTAURANT_HOST_ROLE = "restaurant.HostRole";
	public final static String RESTAURANT_COOK_ROLE = "restaurant.CookRole";
	public final static String RESTAURANT_CASHIER_ROLE = "restaurant.CashierRole";
	public final static String APARTMENT_MANAGER_ROLE = "residence.ApartmentManagerRole";
	
	/**
	 * The scheduler function for a particular role
	 * @return true if a rule was satisfied, false otherwise
	 */
	public abstract boolean pickAndExecuteAction();
	
	/**
	 * Using the states for a particular role, return true if
	 * the role can go get food, false otherwise.
	 * 
	 * If the role can always get food, just return true
	 * @return true if Role can go get food, false otherwise
	 */
	public abstract boolean canGoGetFood();
	
	/**
	 * String accessor to get the name of the Role
	 * @return
	 */
	public abstract String getNameOfRole();
	
	//--------------------UTILITIES-----------------//
	
	protected void print(String s){
		System.out.println(myPerson.getName()+"-"+getNameOfRole()+": "+s);
	}
	
	/**
	 * Calls the stateChanged method to cue the scheduler in the person
	 */
	protected void stateChanged(){
		myPerson.stateChanged();
	}
	
	/**
	 * Returns a boolean representing the active state of the Role
	 * @return true if active, false otherwise
	 */
	public boolean isActive(){
		return isActive;
	}
	
	/**
	 * Set the person agent
	 * @param customerPerson the new person
	 */
	public void setPerson(Person customerPerson){
		myPerson = customerPerson;
	}
	
	/**
	 * Standard accessor for myPerson
	 * @return the personAgent that owns the Role
	 */
	public Person getPerson(){
		return myPerson;
	}
	
	/**
	 * Activate the role
	 */
	public void activate(){
		isActive = true;
		stateChanged();
	}
	
	/**
	 * Deactivate a specific role
	 */
	public void deactivate(){
		isActive = false;
		stateChanged();
	}
	
}
