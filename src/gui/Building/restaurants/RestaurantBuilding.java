package gui.Building.restaurants;


import gui.Building.BuildingGui;

import java.awt.Color;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class RestaurantBuilding extends BuildingGui {	
	
	public RestaurantBuilding( BuildingGui b ) {
		super( b.x, b.y, b.width, b.height );
			
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(100, 180, 200));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
			g.setColor(Color.blue);
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			g.setColor(Color.white);
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height/6);
			g.setColor(new Color(100, 180, 200));
			g.fillRect((int)(super.x)+17, (int)(super.y)+30, (int)super.width/3, (int)super.height/5);
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			
		}
		else
		{
			g.setColor(Color.blue);
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			g.setColor(Color.white);
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height/6);
			g.setColor(new Color(100, 180, 200));
			g.fillRect((int)(super.x)+17, (int)(super.y)+30, (int)super.width/3, (int)super.height/5);
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			
			//super.draw(g);
		}
	}
	
}