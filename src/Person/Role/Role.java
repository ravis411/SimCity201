package Person.Role;

import building.BuildingList;
import interfaces.Person;

public abstract class Role {

	public RoleState roleState = RoleState.Inactive;

	protected Person myPerson;
	
	private boolean isWaitingAtWork = false;
	
	public final static String MARKET_CUSTOMER_ROLE = "MarketEmployee.MarketCustomerRole";
	public final static String MARKET_EMPLOYEE_ROLE = "MarketEmployee.MarketEmployeeRole";
	public final static String MARKET_MANAGER_ROLE = "MarketEmployee.MarketManagerRole";
	public final static String BANK_CLIENT_ROLE = "bank.BankClientRole";
	public final static String BANK_TELLER_ROLE = "bank.BankTellerRole";
	public final static String LOAN_TELLER_ROLE = "bank.LoanTellerRole";
	public final static String PASSENGER_ROLE = "Person.Role.PassengerRole";
	public final static String HOME_ROLE = "residence.HomeRole";
	public final static String HOME_GUEST_ROLE = "residence.HomeGuestRole";
	public final static String RESTAURANT_CUSTOMER_ROLE = "restaurant.RestaurantCustomerRole";
	public final static String RESTAURANT_WAITER_ROLE = "restaurant.OldWaiterRole";
	public final static String RESTAURANT_NEW_WAITER_ROLE = "restaurant.NewWaiterRole";
	public final static String RESTAURANT_HOST_ROLE = "restaurant.HostRole";
	public final static String RESTAURANT_COOK_ROLE = "restaurant.CookRole";
	public final static String RESTAURANT_CASHIER_ROLE = "restaurant.CashierRole";
	public final static String RESTAURANT_LUCA_CUSTOMER_ROLE = "restaurant.luca.LucaRestaurantCustomerRole";
	public final static String RESTAURANT_LUCA_WAITER_ROLE = "restaurant.luca.LucaOldWaiterRole";
	public final static String RESTAURANT_LUCA_NEW_WAITER_ROLE = "restaurant.luca.LucaNewWaiterRole";
	public final static String RESTAURANT_LUCA_HOST_ROLE = "restaurant.luca.LucaHostRole";
	public final static String RESTAURANT_LUCA_COOK_ROLE = "restaurant.luca.LucaCookRole";
	public final static String RESTAURANT_LUCA_CASHIER_ROLE = "restaurant.luca.LucaCashierRole";
	public final static String APARTMENT_MANAGER_ROLE = "residence.ApartmentManagerRole";
	public final static String RESTAURANT_KUSH_CUSTOMER_ROLE = "kushrestaurant.CustomerRole";
	public final static String RESTAURANT_KUSH_WAITER_ROLE = "kushrestaurant.WaiterRole";
	public final static String RESTAURANT_KUSH_HOST_ROLE = "kushrestaurant.HostRole";
	public final static String RESTAURANT_KUSH_COOK_ROLE = "kushrestaurant.CookRole";
	public final static String RESTAURANT_KUSH_CASHIER_ROLE = "kushrestaurant.CashierRole";
    public final static String RESTAURANT_JEFFREY_CUSTOMER_ROLE = "jeffreyRestaurant.CustomerAgent";
    public final static String RESTAURANT_JEFFREY_NEW_WAITER_ROLE = "jeffreyRestaurant.NewWaiterAgent";
    public final static String RESTAURANT_JEFFREY_OLD_WAITER_ROLE = "jeffreyRestaurant.OldWaiterAgent";
    public final static String RESTAURANT_JEFFREY_HOST_ROLE = "jeffreyRestaurant.HostAgent";
    public final static String RESTAURANT_JEFFREY_COOK_ROLE = "jeffreyRestaurant.CookAgent";
    public final static String RESTAURANT_JEFFREY_CASHIER_ROLE = "jeffreyRestaurant.CashierAgent";
    public final static String RESTAURANT_MIKE_WAITER_ROLE = "mikeRestaurant.NewWaiterRole"; 
    public final static String RESTAURANT_MIKE_HOST_ROLE = "mikeRestaurant.HostRole"; 
    public final static String RESTAURANT_MIKE_COOK_ROLE = "mikeRestaurant.CookRole"; 
    public final static String RESTAURANT_MIKE_CASHIER_ROLE = "mikeRestaurant.CashierRole"; 
    public final static String RESTAURANT_MIKE_CUSTOMER_ROLE = "mikeRestaurant.CustomerRole"; 
    
    public final static String RESTAURANT_RYAN_HOST_ROLE = "ryansRestaurant.RyansHostRole";
    public final static String RESTAURANT_RYAN_COOK_ROLE = "ryansRestaurant.RyansCookRole"; 
    public final static String RESTAURANT_RYAN_CASHIER_ROLE = "ryansRestaurant.RyansCashierRole"; 
    public final static String RESTAURANT_RYAN_CUSTOMER_ROLE = "ryansRestaurant.RyansCustomerRole";
    public final static String RESTAURANT_RYAN_OLD_WAITER_ROLE = "ryansRestaurant.RyansOldWaiterRole";//"ryansRestaurant.RyansWaiterRole";
    public final static String RESTAURANT_RYAN_NEW_WAITER_ROLE = "ryansRestaurant.RyansNewWaiterRole";
    
    public final static String RESTAURANT_BYRON_NEW_WAITER_ROLE = "byronRestaurant.NewWaiterRole";
    public final static String RESTAURANT_BYRON_WAITER_ROLE = "byronRestaurant.OldWaiterRole";
    public final static String RESTAURANT_BYRON_HOST_ROLE = "byronRestaurant.HostRole"; 
    public final static String RESTAURANT_BYRON_COOK_ROLE = "byronRestaurant.CookRole"; 
    public final static String RESTAURANT_BYRON_CASHIER_ROLE = "byronRestaurant.CashierRole"; 
    public final static String RESTAURANT_BYRON_CUSTOMER_ROLE = "byronRestaurant.CustomerRole"; 
    
	
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
		return roleState == RoleState.Active;
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
		roleState = RoleState.Active;
		//stateChanged();
	}
	
	public void waitingAtWork(){
		this.isWaitingAtWork = true;
		stateChanged();
	}
	
	public void goingToWork(){
		this.isWaitingAtWork = false;
		stateChanged();
	}
	
	public boolean getWaitingAtWork(){
		return this.isWaitingAtWork;
	}
	
	/**
	 * Deactivate a specific role
	 */
	public void deactivate(){
		roleState = RoleState.Deactivating;
		stateChanged();
	}
	
	public void kill(){
		roleState = RoleState.Inactive;
		stateChanged();
	}
	
}
