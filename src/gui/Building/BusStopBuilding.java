package gui.Building;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

import trace.AlertLog;
import trace.AlertTag;

@SuppressWarnings("serial")
public class BusStopBuilding extends BuildingGui {	
	
	Image image = null;
	
	int waitingPassengers = 0;
	
	public BusStopBuilding( BuildingGui b ) {
		super( b.x, b.y, b.width, b.height );
		
		try {
			if(System.getProperty("os.name").contains("Windows")){
				ImageIcon imageI = new ImageIcon(this.getClass().getResource("/images/bus-stop.png").getPath());
				image = imageI.getImage();
			}
		} catch (Exception e) {
			//AlertLog.getInstance().logWarning(AlertTag.BUS_STOP, "Bus Stop Building", "Error loading image.");
		}
	
	
	}
	
	@Override
	public void updatePosition() {
		super.updatePosition();
		waitingPassengers = ((BusStopBuildingPanel)myBuildingPanel).getNumWaitingPassengers();
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
			g.setColor(new Color(25, 128, 25));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y);
			super.draw(g);
			
			
			//Draw some tiny circles for the passengers
			g.setColor(Color.red);
			int yOffset = 0;
			int xOffset = 0;
			for(int i = 0; i < waitingPassengers; i++){
				g.fillOval((int)super.x + xOffset *5, (int)super.y + yOffset*5, 5, 5);
				xOffset++;
				if(xOffset * 5 >= super.width){
					yOffset++;
					xOffset =0;
					if(yOffset * 5 >= super.height)
						break;
				}
			}//Waiting passenger dots have been drawn
			
			
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