package gui.Building;

import gui.BuildingsPanels;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import market.gui.MarketAnimationPanel;
import Person.Role.Role;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class MarketBuildingPanel extends BuildingPanel{
	public MarketAnimationPanel animationPanel;

	public MarketBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		
		super(r, name, buildingPanels);
		animationPanel =  new MarketAnimationPanel(name);
		setLayout(new BorderLayout());
		this.removeAll();
		
		setBackground( new Color(100,100,200) );
		add(animationPanel, BorderLayout.CENTER);
		
	}
	
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public GuiPanel getPanel() {
		return animationPanel;
	}

}
