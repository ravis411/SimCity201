package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;

public class ApartmentBuilding extends Building {	
	
	
	
	public ApartmentBuilding( Building b ) {
		super( b.x, b.y, b.width, b.height );
		
			
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(100, 180, 200));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
		}else
		{
			g.setColor(new Color(100, 180, 200));
			int xs[] = {(int)super.x, (int)(super.x + super.width/2), (int)(super.x + super.width)};
			int ys[] = {(int)(super.y + super.height/2), (int)(super.y), (int)(super.y + super.height/2)};
			g.fillPolygon(xs, ys, 3);
			g.setColor(Color.blue);
			g.fillRect((int)(super.x), (int)(super.y + super.height/2), (int)super.width, (int)super.height/2);
			g.setColor(Color.orange);
			//super.draw(g);
		}
		
	}
	
}