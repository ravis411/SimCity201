package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class ResidenceBuilding extends BuildingGui {	
	
	boolean apartment;
	
	int xs[];
	int ys[];
	
	public ResidenceBuilding( BuildingGui b, boolean apartment ) {
		super( b.x, b.y, b.width, b.height );
		this.apartment = apartment;
		
		int xss[] = {(int)super.x, (int)(super.x + super.width/2), (int)(super.x + super.width)};
		int yss[] = {(int)(super.y + super.height/2), (int)(super.y), (int)(super.y + super.height/2)};
		xs = xss;
		ys = yss;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(100, 180, 200));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
			g.setColor(new Color(100, 180, 200));
			g.fillPolygon(xs, ys, 3);
			g.setColor(Color.blue);
			g.fillRect((int)(super.x), (int)(super.y + super.height/2), (int)super.width, (int)super.height/2);
			g.setColor(Color.orange);
		}
		else if (apartment == false)
		{
			g.setColor(new Color(100, 180, 200));
			g.fillPolygon(xs, ys, 3);
			g.setColor(Color.blue);
			g.fillRect((int)(super.x), (int)(super.y + super.height/2), (int)super.width, (int)super.height/2);
			g.setColor(Color.orange);
			//super.draw(g);
		}
		else if (apartment == true)
		{
			g.setColor(Color.lightGray);
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			g.setColor(Color.black);
			g.fillRect((int)(super.x)+10, (int)(super.y)+10, (int)super.width-20, (int)super.height-20);
		}
		
	}
	
}