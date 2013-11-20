package gui;

import gui.Building.BuildingPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.Closeable;
import java.io.IOException;

import javax.swing.JPanel;


/** This class holds all the panels for various buildings and displays them if called.
 * 
 * 
 *
 */
public class BuildingsPanels extends JPanel{
	CardLayout cardLayout = new CardLayout();
	
	
	
	
	
	/**Default Constructor
	 * 
	 */
	public BuildingsPanels() {
		setLayout(cardLayout);
		setBackground(Color.yellow);
		
		
	}
	
	/**Adds a building to the panel
	 * 
	 * @param buildingPanel
	 */
	public void addBuildingPanel(BuildingPanel buildingPanel) {
		this.add(buildingPanel, buildingPanel.getName());
	}
	
	
	
	
	
	
	
	/** Returns true if a building with the given name already exists
	 * 
	 * @param name	The name to check for.
	 * @return True if name is already used. False otherwise.
	 */
	public boolean containsName(String name) {
		for(Component b : this.getComponents()){
			if(b instanceof BuildingPanel) {
				if(b.getName() == name){
					return true;
				}
			}
		}
		return false;
	}//end containsName
	
	
	
	/**This will display the building panel.
	 * 
	 */
	public void displayBuildingPanel(BuildingPanel buildingPanel) {
		//System.out.println("Showing: " + buildingPanel.getName());
		cardLayout.show(this, buildingPanel.getName());
		
		
	}
	
	public void clear(){
		this.removeAll();
		cardLayout = null;
	}


	
	
}
