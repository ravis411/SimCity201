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

			g.fill3DRect( (int)super.x,  (int)super.y+20,  (int)super.width, (int)super.height-20, true);
			
			g.fill3DRect( (int)super.x +10,  (int)super.y+10,  (int)super.width-20, (int)super.height-10, true);
			
			g.setColor(Color.white);
			int xs[] = {(int)super.x + 15, (int)(super.x + super.width/2), (int)(super.x + super.width)-15};
			int ys[] = {(int)(super.y + 15), (int)(super.y)+5, (int)(super.y + 15)};
			g.fillPolygon(xs, ys, 3);
			g.fill3DRect( (int)super.x +15,  (int)super.y+15,  (int)super.width-30, (int)super.height-45, false);
			
			g.fill3DRect( (int)(super.x),  (int)(super.y+super.height-5),  (int)(super.width), (int)5, true);
			g.fill3DRect( (int)super.x +15,  (int)(super.y + 20),  (int)5, (int)super.height-25, false);
			g.fill3DRect( (int)super.x +22,  (int)(super.y + 20),  (int)5, (int)super.height-25, false);
			g.fill3DRect( (int)super.x +30,  (int)(super.y + 20),  (int)5, (int)super.height-25, false);
			
			
			g.setColor(Color.orange);
		}
		
	}
	
}