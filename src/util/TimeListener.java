package util;

/**
 * Agents to which we send updates for specific times
 *
 */
public interface TimeListener {

	void timeAction(int hour, int minute);
	
}
