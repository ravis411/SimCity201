package gui;

import gui.Building.BuildingPanel;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;


/** This class holds all the panels for various buildings and displays them if called.
 * 
 * 
 *
 */
public class BuildingsPanels extends JPanel {
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
	
	
	/**This will display the building panel.
	 * 
	 */
	public void displayBuildingPanel(BuildingPanel buildingPanel) {
		//System.out.println("Showing: " + buildingPanel.getName());
		cardLayout.show(this, buildingPanel.getName());
	}
	
}
