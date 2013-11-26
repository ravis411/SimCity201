package Person.Role;

import Person.PersonAgent;

public abstract class Role {

	private boolean isActive = false;
	protected PersonAgent myPerson;
	
	public final static String MARKET_CUSTOMER_ROLE = "MarketCustomerRole";
	public final static String BANK_CUSTOMER_ROLE = "BankCustomerRole";
	public final static String PASSENGER_ROLE = "PassengerRole";
	public final static String HOME_ROLE = "HomeRole";
	public final static String RESTAURANT_CUSTOMER_ROLE = "RestaurantCustomerRole";
	public final static String RESTAURANT_WAITER_ROLE = "RestaurantWaiterRole";
	public final static String RESTAURANT_HOST_ROLE = "RestaurantHostRole";
	
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
	 * @param p the new person
	 */
	public void setPerson(PersonAgent p){
		myPerson = p;
	}
	
	/**
	 * Standard accessor for myPerson
	 * @return the personAgent that owns the Role
	 */
	public PersonAgent getPerson(){
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
	}
	
}
