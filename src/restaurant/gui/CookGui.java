package restaurant.gui;

import restaurant.CookRole;
import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;

import java.awt.*;

public class CookGui implements Gui {

    private CookRole agent = null;

    private int xPos = 405, yPos = 80;//default waiter position
    private int xDestination = 405, yDestination = 80;//default start position
    
    int plate = 0;
    int ingredients = 1;

    public CookGui(CookRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        if(xPos == 405 && yPos == 120) {
        	plate = 1;
        	xDestination = 405;
        	yDestination = 80; 
        }
        if(xPos == 415 && yPos == 40) {
        	ingredients = 0;
        	xDestination = 405;
        	yDestination = 80;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
        
        if(plate == 1) {
        	g.setColor(Color.white);
            g.fillOval(370, 150, 15, 15);
            g.setColor(Color.green);
            g.fillOval(376, 155, 5, 5);
            g.setColor(Color.orange);
            g.fillOval(378, 158, 5, 5);
            g.setColor(Color.cyan);
            g.fillOval(374, 152, 5, 5);
        }
        if(ingredients == 1) {
        	g.setColor(Color.green);
            g.fillOval(450, 35, 5, 5);
            g.fillOval(452, 32, 5, 5);
            g.setColor(Color.orange);
            g.fillOval(455, 40, 5, 5);
            g.fillOval(457, 45, 5, 5);
            g.setColor(Color.cyan);
            g.fillOval(448, 42, 5, 5);
            g.fillOval(445, 37, 5, 5);
        }
    }

    public void DoGoToPlatingArea() {
    	xDestination = 405;
    	yDestination = 120;
    }
    
    public void DoGoToFridge() {
    	xDestination = 415;
    	yDestination = 40;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public boolean isPresent() {
        return true;
    }
    
    public void setPlate(int p) {
    	plate = p;
    }
    
    public void setIngredients(int i) {
    	ingredients = i;
    }
}
