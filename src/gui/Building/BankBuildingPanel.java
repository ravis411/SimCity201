package gui.Building;

import gui.BuildingsPanels;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import Person.Role.Role;
import bank.gui.BankAnimationPanel;
/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class BankBuildingPanel extends BuildingPanel{
	
	BankAnimationPanel animationPanel = new BankAnimationPanel();
	
	public BankBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
	    animationPanel.announcer.startThread();
	    animationPanel.loanAnnouncer.startThread();

		setBackground( Color.yellow );
	
		
		setLayout(new GridLayout(1,1));
		
		//JLabel j = new JLabel( myName );
		add(animationPanel);
		
		//
		
	}
	
	
	
	
	public String getName() {
		return myName;
	}
	

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public GuiPanel getPanel() {
		// TODO Auto-generated method stub
		return animationPanel;
	}




	@Override
	public void addPersonWithRole(Role r) {
		// TODO stub	
	}

}
