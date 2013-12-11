package Person.Role;

import trace.AlertLog;
import trace.AlertTag;
import building.BuildingList;
import building.Workplace;

public abstract class Employee extends Role{
	
	protected String workLocation;
	protected ShiftTime shift;
	
	public enum WorkState {AtWork, ReadyToLeave, ToldHost}
	protected WorkState workState = WorkState.AtWork;
	
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
	
	@Override
	public boolean pickAndExecuteAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deactivate(){
		super.deactivate();
		workState = WorkState.ReadyToLeave;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		super.kill();
		AlertLog.getInstance().logError(AlertTag.PERSON, "Employee", "KILLED "+getNameOfRole());
		Workplace w = (Workplace) BuildingList.findBuildingWithName(workLocation);
		w.removeRole(this);
		w.removeInhabitants();
		roleState = RoleState.Inactive;
		workState = WorkState.AtWork;
	}

	/**
	 * Mutator for the Work Location
	 * @param workLocation
	 */
	public void setWorkLocation(String workLocation){
		this.workLocation = workLocation;
	}
	
}
