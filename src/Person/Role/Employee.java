package Person.Role;

import util.Interval;

public abstract class Employee extends Role{
	
	private String workLocation;

	/**
	 * Returns the interval over which an employee is expected to work
	 * @return interval over which an employee is expected 
	 */
	public abstract Interval getShift();
	
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
	
	enum ShiftTime {DayShift, NightShift}
	
}
