package gui.Building;

import gui.BuildingsPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Default class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 */
public class BuildingPanel extends JPanel{
	Rectangle2D myRectangle;
	protected String myName;
	protected BuildingsPanels myCity;
	
	public BuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels) {
		myRectangle = r;
		myName = name;
		myCity = buildingPanels;
		
		setBackground( Color.green );
		Dimension d = new Dimension(myCity.getSize());
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		JLabel j = new JLabel( myName );
		add( j );
	}
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}

}
