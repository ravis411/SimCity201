package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class ApartmentBuilding extends BuildingGui {	
	
	
	
	public ApartmentBuilding( BuildingGui b ) {
		super( b.x, b.y, b.width, b.height );
		
			
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(100, 180, 200));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
			g.setColor(new Color(200, 50, 50));
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			g.setColor(Color.yellow);
			g.fillRect((int)(super.x)+10, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+10, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+30, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+30, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			g.setColor(new Color(100, 180, 200));
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			
		}else
		{
			g.setColor(new Color(200, 50, 50));
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			g.setColor(Color.yellow);
			g.fillRect((int)(super.x)+10, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+10, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+30, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			g.fillRect((int)(super.x)+30, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			g.setColor(new Color(100, 180, 200));
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			//super.draw(g);
		}
		
	}
	
}