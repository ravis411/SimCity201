package gui;

import gui.Building.BankBuildingPanel;
import gui.Building.BuildingPanel;
import gui.Building.MarketBuildingPanel;
import gui.Building.ResidenceBuildingPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;

import building.Bank;
import building.Building;
import building.BuildingList;
import building.Market;


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
	
	/**
	 * Adds a building to the panel
	 * @param buildingPanel
	 */
	public void addBuildingPanel(BuildingPanel buildingPanel) {
		if(buildingPanel instanceof BankBuildingPanel){
			BuildingList.getInstance().add(new Bank(buildingPanel));
		}else if(buildingPanel instanceof MarketBuildingPanel){
			BuildingList.getInstance().add(new Market(buildingPanel));
		}else{
			BuildingList.getInstance().add(new Building(buildingPanel));
		}
		
		this.add(buildingPanel, buildingPanel.getName());
	}
	
	
	
	
	
	/***	Returns the BuildingPanel with the given name.
	 * 
	 * @param name	The name of the building panel to search for.
	 * @return	The BuildingPanel with the given name if it exists. null otherwise.
	 */
	public BuildingPanel getBuildingPanel(String name){
		BuildingPanel b = null;
		for(Component c : this.getComponents()){
			if(c instanceof BuildingPanel) {
				if(c.getName() == name){
					b = (BuildingPanel) c;
					break;
				}
			}
		}
		return b;
	}//end getBuildingPanel
	
	public ResidenceBuildingPanel getResidenceBuildingPanel(String name){
		ResidenceBuildingPanel b = null;
		for(Component c : this.getComponents()){
			if(c instanceof ResidenceBuildingPanel) {
				if(c.getName() == name){
					b = (ResidenceBuildingPanel) c;
					break;
				}
			}
		}
		return b;
	}//end getResidenceBuildingPanel
	
	
	
	
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
	/**This will display the building panel.
	 * 
	 */
	public void displayBuildingPanel(String name){
		for(Component b : this.getComponents()){
			if(b instanceof BuildingPanel) {
				if(b.getName() == name){
					//System.out.println("SHOWING PANEL");
					displayBuildingPanel((BuildingPanel)b);
					return;
				}
			}
		}
	}
	
	public void clear(){
		this.removeAll();
		cardLayout = null;
	}


	
	
}
