package ryansRestaurant.gui;

import javax.swing.JFrame;



public class RyansRestaurantGuiTestFrame extends JFrame {


	int WINDOWX = 800;
    int WINDOWY = 400;
	
	public RyansRestaurantGuiTestFrame() {
		setBounds(50, 50, WINDOWX , WINDOWY);
		
		this.add(new RestaurantGui());
		
	}
	
	
	
	
	  /**
     * Main routine to get gui started
   */
    public static void main(String[] args) {
        RyansRestaurantGuiTestFrame gui = new RyansRestaurantGuiTestFrame();
        gui.setTitle("csci201 Restaurant - Ryan Davis");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
  

}
