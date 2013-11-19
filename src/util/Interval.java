package util;

import java.util.Calendar;
import java.util.Date;

public class Interval {
	
	private final Time startTime;
	private final Time endTime;
	
	public Interval(Time startTime, Time endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	
	/**
	 * Utility for determining if the time is within the interval
	 * @param time the time to compare against
	 * @return true if time is within interval, false otherwise
	 */
	public boolean intersectsWithTime(Calendar time){
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), 
				startTime.getHour(), startTime.getMinute(), startTime.getSecond());
		
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), 
				endTime.getHour(), endTime.getMinute(), endTime.getSecond());
		
		return startCalendar.compareTo(time) <= 0 && endCalendar.compareTo(time) >= 0;
	}

}
