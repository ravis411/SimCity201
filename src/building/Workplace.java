package building;

import gui.Building.BuildingPanel;

public abstract class Workplace extends Building {

	public Workplace(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Message sent to the workplace that it is time to open
	 */
	public abstract void open();
	
	/**
	 * Message sent to the workplace that it is time to close
	 */
	public abstract void close();
}
