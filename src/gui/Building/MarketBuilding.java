package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;

public class MarketBuilding extends BuildingGui {	
	
	Image image = null;
	
	public MarketBuilding( BuildingGui b ) {
		super( b.x, b.y, b.width, b.height );
		
			
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(25, 128, 25));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
			g.setColor(Color.orange);
		}
		else{
			if(image == null){
				testView = true;
				return;
			}
			g.setColor(Color.black);
			super.draw(g);
			
			g.drawImage(image, (int)super.x, (int)super.y, (int)super.getWidth(), (int)super.getHeight(), null);
		}
		
	}
	
}