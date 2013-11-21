package gui.Building;


import gui.Gui;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Building extends Rectangle2D.Double implements Gui{
	BuildingPanel myBuildingPanel;

	public Building( double x, double y, double width, double height ) {
		super( x, y, width, height );
	}
	
	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}
	
	public void setBuildingPanel( BuildingPanel bp ) {
		myBuildingPanel = bp;
	}

	public void updatePosition() {
		
	}

	public void draw(Graphics2D g) {
		g.fill(this);
	}

	public boolean isPresent() {
		return true;
	}
}