package building;

import gui.Building.BuildingPanel;

import java.util.ArrayList;
import java.util.List;

import residence.HomeRole;
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
	public synchronized void addRole(Role r){
		boolean b = !inhabitants.contains(r);
		System.err.println("Trying to add to inhabitant list");
		if(!inhabitants.contains(r)){
			System.err.println("already in inhabitant list");
			inhabitants.add(r);
			this.panel.getPanel().addGuiForRole(r);
		}
	}
	
	/**
	 * Getter for inhabitants
	 * @return
	 */
	public List<Role> getInhabitants(){
		return inhabitants;
	}
	
	private List<Role> removalList = new ArrayList<Role>();
	
	public synchronized void removeInhabitants(){
		for(int i = removalList.size()-1; i >= 0; i--){
			inhabitants.remove(removalList.get(i));
			this.panel.getPanel().removeGuiForRole(removalList.get(i));
			if(i <= (removalList.size()-1)) {
				removalList.remove(i);
			}
		}
	}
	
	/**
	 * Removes a person from the building
	 * @param r
	 */
	public synchronized void removeRole(Role r){
			removalList.add(r);
	}
}
