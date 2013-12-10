package Person.Role;

import building.BuildingList;
import util.Interval;

public abstract class Employee extends Role{
	
	protected String workLocation;
	protected ShiftTime shift;
	
	protected Employee(String workLocation){
		this.workLocation = workLocation;
	}
	
	/**
	 * Returns the interval over which an employee is expected to work
	 * @return interval over which an employee is expected 
	 */
	public ShiftTime getShift(){
		return shift;
	}
	
	public void setShift(ShiftTime shiftTime){
		this.shift = shiftTime;
	}
	
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
	
	public void activate(){
		super.activate();
		//BuildingList.findBuildingWithName(workLocation).addRole(this);
	}
	
	
	/**
	 * Mutator for the Work Location
	 * @param workLocation
	 */
	public void setWorkLocation(String workLocation){
		this.workLocation = workLocation;
	}
	
}
