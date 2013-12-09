package Person.Role;

import util.Interval;

public abstract class Employee extends Role{
	
	protected String workLocation;
	
	protected Employee(String workLocation){
		this.workLocation = workLocation;
	}
	
	/**
	 * Returns the interval over which an employee is expected to work
	 * @return interval over which an employee is expected 
	 */
	public abstract ShiftTime getShift();
	
	/**
	 * Returns hourly wage of employee
	 * @return
	 */
	public abstract Double getSalary();
	
	/**
	 * Accessor for the Work location
	 * @return
	 */
	public String getWorkLocation(){
		return workLocation;
	}
	
	/**
	 * Mutator for the Work Location
	 * @param workLocation
	 */
	public void setWorkLocation(String workLocation){
		this.workLocation = workLocation;
	}
	
}
