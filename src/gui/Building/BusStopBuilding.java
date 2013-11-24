package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class BusStopBuilding extends Building {	
	
	public BusStopBuilding( Building b ) {
		super( b.x, b.y, b.width, b.height );
	}
	
	ImageIcon imageI = new ImageIcon(this.getClass().getResource("/images/bus-stop.png").getPath());
	
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(25, 128, 25));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
		}
		else{
			if(imageI == null){
				testView = true;
				return;
			}
			g.setColor(Color.black);
			super.draw(g);
			Image image = imageI.getImage();
			g.drawImage(image, (int)super.x, (int)super.y, (int)super.getWidth(), (int)super.getHeight(), null);
		}
		
	}
	
}