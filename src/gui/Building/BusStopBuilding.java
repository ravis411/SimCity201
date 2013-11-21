package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class BusStopBuilding extends Building {	
	
	public BusStopBuilding( Building b ) {
		super( b.x, b.y, b.width, b.height );
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(25, 128, 25));
		g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
		
		
		super.draw(g);
	}
	
}