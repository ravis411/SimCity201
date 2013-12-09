package gui.Building;

import gui.BuildingsPanels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import residence.gui.AnimationPanel;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class ResidenceBuildingPanel extends BuildingPanel{
	
	AnimationPanel residencePanel;
	public boolean isApartment = true;
	
	public ResidenceBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels, boolean isApartment) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		residencePanel = new AnimationPanel();
		
		this.isApartment = isApartment;
		
		setLayout(new GridLayout(1,1));
		
		
		//JLabel j = new JLabel( myName );
		add( residencePanel );
	}
	
	
	public String getName() {
		return myName;
	}
	
	public AnimationPanel getPanel() {
		return residencePanel;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
