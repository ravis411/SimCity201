package Person.Role;

import Person.MarketPerson;

public abstract class Role {

	private boolean isActive;
	protected MarketPerson myPerson;
	public String role;
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
	public abstract String getName();
	
	//--------------------UTILITIES-----------------//
	
	protected void print(String s){
		System.out.println(myPerson.getName()+"-"+getName()+": "+s);
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
	public void setPerson(MarketPerson p){
		myPerson = p;
	}
	
	/**
	 * Standard accessor for myPerson
	 * @return the personAgent that owns the Role
	 */
	protected MarketPerson getPerson(){
		return myPerson;
	}
	
	/**
	 * Activate the role
	 */
	public void activate(){
		isActive = true;
	}
	
	/**
	 * Deactivate a specific role
	 */
	public void deactivate(){
		isActive = false;
	}
	
}
