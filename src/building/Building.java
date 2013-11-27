package building;

import gui.Building.BuildingPanel;

import java.util.ArrayList;
import java.util.List;

import Person.Role.Role;

/**
 * This class is the data-level building class which holds information about the inhabitants
 * of the place.
 * @author MSILKJR
 *
 */
public class Building {
	
	protected BuildingPanel panel;

	protected String name;
	protected List<Role> inhabitants;
	
	/**
	 * Constructor for a Building
	 * @param name the name of the Building
	 * @param panel the panel to which the Building should be linked
	 */
	public Building(BuildingPanel panel){
		this.panel = panel;
		this.name = panel.getName();
		
		inhabitants = new ArrayList<Role>();
	}
	
	/**
	 * Getter for name
	 * @return name of building
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Add a Person to the building
	 * @param r the role of the person to be added
	 */
	public void addRole(Role r){
		inhabitants.add(r);
		this.panel.getPanel().addGuiForRole(r);
	}
	
	/**
	 * Getter for inhabitants
	 * @return
	 */
	public List<Role> getInhabitants(){
		return inhabitants;
	}
	
	/**
	 * Removes a person from the building
	 * @param r
	 */
	public void removeRole(Role r){
		inhabitants.remove(r);
		this.panel.getPanel().removeGuiForRole(r);
	}
}
