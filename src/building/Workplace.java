package building;

import gui.Building.BuildingPanel;

public abstract class Workplace extends Building{
	
	protected Workplace(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	public static int DAY_SHIFT_HOUR = 9;
	public static int DAY_SHIFT_MIN = 0;
	
	public static int NIGHT_SHIFT_HOUR = 17;
	public static int NIGHT_SHIFT_MIN = 0;
	
	public static int END_SHIFT_HOUR = 0;
	public static int END_SHIFT_MIN = 0;
	
	/**
	 * Message sent to the workplace that it is time to open
	 */
	public abstract void open();
	
	/**
	 * Message sent to the workplace that it is time to close
	 */
	public abstract void close();
	
	/**
	 * Find out who is in the store and determine if the store is open or not
	 * @return
	 */
	public abstract boolean isOpen();
	
	public abstract void notifyEmployeesTheyCanLeave();
	
}
