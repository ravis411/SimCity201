package building;

import gui.Building.BuildingPanel;

public abstract class Workplace extends Building {

	public Workplace(String name, BuildingPanel panel) {
		super(name, panel);
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
