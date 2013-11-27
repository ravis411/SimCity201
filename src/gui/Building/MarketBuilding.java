package gui.Building;


import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class MarketBuilding extends BuildingGui {	
	
	Image image = null;
	
	public MarketBuilding( BuildingGui b ) {
		super( b.x, b.y, b.width, b.height );
		
			
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(testView){
		//	super.draw(g);
			
			g.setColor(new Color(150, 50, 150));
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			//g.setColor(Color.yellow);
			//g.fillRect((int)(super.x)+10, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+10, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+30, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+30, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			
			g.setColor(new Color(200, 250, 100));
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			
			g.setColor(new Color(25, 128, 25));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y+10);
			
			FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds("$$", g);

            g.setColor(Color.DARK_GRAY);
            g.fillRect((int)(super.x + super.width/2-6), (int)(super.y + super.height/2-6 ), (int) rect.getWidth(), (int) rect.getHeight());

            g.setColor(new Color(25, 128, 25));
			g.drawString("$$", (int)(super.x + super.width/2-6), (int)(super.y + super.height/ 2)+6 );
			g.drawString(super.myBuildingPanel.myName, (int)super.x, (int)super.y);
			g.setColor(Color.orange);
		}
		else{
			g.setColor(new Color(150, 50, 150));
			g.fillRect((int)(super.x), (int)(super.y), (int)super.width, (int)super.height);
			//g.setColor(Color.yellow);
			//g.fillRect((int)(super.x)+10, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+10, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+30, (int)(super.y)+6, (int)super.width/5, (int)super.height/5);
			//g.fillRect((int)(super.x)+30, (int)(super.y)+23, (int)super.width/5, (int)super.height/5);
			
			g.setColor(new Color(200, 250, 100));
			g.fillRect((int)(super.x)+17, (int)(super.y)+40, (int)super.width/3, (int)super.height/5);
			
			g.setColor(new Color(25, 128, 25));
			g.drawString(super.myBuildingPanel.getName(), (int)super.x,(int)super.y+10);
			
			FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds("$$", g);

            g.setColor(Color.DARK_GRAY);
            g.fillRect((int)(super.x + super.width/2-6), (int)(super.y + super.height/2-6 ), (int) rect.getWidth(), (int) rect.getHeight());

            g.setColor(new Color(25, 128, 25));
			g.drawString("$$", (int)(super.x + super.width/2-6), (int)(super.y + super.height/ 2)+6 );
			
			g.setColor(Color.orange);
		}
		
	}
	
}