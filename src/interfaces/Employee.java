package interfaces;

import util.Interval;

public interface Employee {

	/**
	 * Returns the interval over which an employee is expected to work
	 * @return interval over which an employee is expected 
	 */
	Interval getShift();
	
	/**
	 * Returns hourly wage of employee
	 * @return
	 */
	Double getSalary();
	
	enum ShiftTime {DayShift, NightShift}
	
}
