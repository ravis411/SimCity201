package restaurant.gui.luca;

import interfaces.GuiPanel;
import interfaces.MarketManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import restaurant.luca.LucaCashierRole;
import restaurant.luca.LucaCookRole;
import restaurant.luca.LucaHostRole;
import restaurant.luca.LucaRestaurantCustomerRole;
import restaurant.luca.LucaWaiterRole;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import building.Building;
import building.BuildingList;



public class AnimationPanel extends JPanel implements ActionListener,GuiPanel{

    private final static int WINDOWX = 800;//
    private final static int WINDOWY = 450;
    final static int TABLEX = 200;//TABLEX and TABLEY describe where the table appears in the panel
    final static int TABLEY = 250;
    final static int TABLEWIDTH = 50;
    final static int TABLEHEIGHT = 50;
    final static int TIMERCOUNTmilliseconds = 5;
    private Image bufferImage;
    private Dimension bufferSize;
    private Vector<MarketManager> markets = new Vector<MarketManager>();
    private List<Gui> guis = new ArrayList<Gui>();
    private LucaHostRole host;
    private int customerNumber=0;
    private int waiterNumber=0;
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		  
		for(Gui gui : guis) {
	            if (gui.isPresent()) {
	            	gui.updatePosition();
	            }
	        }
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        for(int i =0; i<LucaHostRole.getNTABLES(); i++)
        {
        g2.fillRect(TABLEX+i*60, TABLEY, TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        }
        
        g2.setColor(Color.BLACK);
        
        g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.drawString("Plating Area", 700, 120);
		g2.drawString("Cooking Area", 700, 20);
		g2.drawString("Refrigerator", 550, 20);
        for(Gui gui : guis) {
            if (gui.isPresent()) {
            	gui.draw(g2);
            }
        }

      
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }

	public void addGui(CookGui gui) {
		guis.add(gui);
		
	}

	@Override
	public void addGuiForRole(Role r) {
		if(r instanceof LucaCookRole){
			LucaCookRole cr = (LucaCookRole) r;
			CookGui gui = new CookGui(cr);
			cr.setGui(gui);
/*			for (Role role :BuildingList.findBuildingWithName(cr.getWorkLocation()).getInhabitants())
			{
				if (role instanceof MarketManager){
					MarketManager manager = (MarketManager) role;
					cr.msgAddMarket(manager);
				}
*/
			guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		
		if(r instanceof LucaHostRole){
		//	host= new LucaHostRole("Cary");
			

		}
		if(r instanceof LucaCashierRole){
		/*	LucaCashierRole cashr = (LucaCashierRole) r;
			for( Building m : BuildingList.findBuildingsWithType("Market")){
				for (Role role :m.getInhabitants())
				{
					if (role instanceof MarketManager){
						MarketManager market = (MarketManager) role;
						markets.add(market);
						break;
					}
				}
				}
			cashr.addMarkets(markets);
*/
			}
		
		if(r instanceof LucaRestaurantCustomerRole){
			LucaRestaurantCustomerRole rcr = (LucaRestaurantCustomerRole) r;
			CustomerGui gui = new CustomerGui(rcr, waiterNumber*50+120);
			rcr.setGui(gui);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, "CustomerGui", "Assigning the Customer Gui ---------");
		
		/*	for (Role role :BuildingList.findBuildingWithName(rcr.).getInhabitants())
			{
				if (role instanceof LucaHostRole){
					LucaHostRole host = (LucaHostRole) role;
					rcr.setHost(host);
					break;
				}
			}
			for (Role role :BuildingList.findBuildingWithName(rcr.getWorkLocation()).getInhabitants())
			{
				if (role instanceof LucaCashierRole){
					LucaCashierRole cashier = (LucaCashierRole) role;
					rcr.setCashier(cashier);
					break;
				}
			}*/
			customerNumber++;
			guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof LucaWaiterRole){
			LucaWaiterRole nwr = (LucaWaiterRole) r;
			WaiterGui gui = new WaiterGui(nwr, waiterNumber*50+120);
			nwr.setGui(gui);
			/*for (Role role :BuildingList.findBuildingWithName(nwr.getWorkLocation()).getInhabitants())
			{
				if (role instanceof LucaCookRole){
					LucaCookRole cook = (LucaCookRole) role;
					nwr.setCook(cook);
					break;
				}
			}
			for (Role role :BuildingList.findBuildingWithName(nwr.getWorkLocation()).getInhabitants())
			{
				if (role instanceof LucaHostRole){
					LucaHostRole host = (LucaHostRole) role;
					nwr.setHost(host);
					break;
				}
			}
			for (Role role :BuildingList.findBuildingWithName(nwr.getWorkLocation()).getInhabitants())
			{
				if (role instanceof LucaCashierRole){
					LucaCashierRole cashier = (LucaCashierRole) role;
					nwr.setCashier(cashier);
					break;
				}
			}*/
			waiterNumber++;
			guis.add(gui);
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
	
		
	}

	@Override
	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}
}
