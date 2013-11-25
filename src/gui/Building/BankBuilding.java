package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;

public class BankBuilding extends Building {	
	
	
	
	public BankBuilding( Building b ) {
		super( b.x, b.y, b.width, b.height );
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(230, 220, 40));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
		}
		else{
			
			g.setColor(new Color(230, 220, 40));
			super.draw(g);
			g.setColor(Color.orange);
			
		}
		
	}
	
}