package gui.Building;

import gui.BuildingsPanels;
import residence.gui.AnimationPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
public class ResidenceBuildingPanel extends BuildingPanel{
	
	public ResidenceBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		super(r, name, buildingPanels);
		
		this.removeAll();
		
		setBackground( Color.yellow );
		
		AnimationPanel residencePanel = new AnimationPanel();
		
		setLayout(new GridLayout(1,1));
		
		
		JLabel j = new JLabel( myName );
		add( residencePanel );
	}
	
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}

}
