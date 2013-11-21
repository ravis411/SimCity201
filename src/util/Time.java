package util;

/**
 * This class is meant only to facilitate the Interval class. It holds time, it does not update time.
 * @author MSILKJR
 *
 */
public class Time {

	private final int hour;
	private final int minute;
	private final int second;
	
	/**
	 * Initializes a Time with all components
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 */
	public Time(int hour, int minute, int second){
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	//auto-generated getters and setters
	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}
	
}
