package market.gui;

import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

import market.data.MarketData;
import MarketEmployee.MarketCustomerRole;
import MarketEmployee.MarketEmployeeRole;
import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import Transportation.DeliveryTruckAgent;
import building.BuildingList;
import building.Market;

public class MarketAnimationPanel extends JPanel implements ActionListener,GuiPanel {

	private final static int WINDOWX = 800;//
	private final static int WINDOWY = 400;
	final static int storeCounterX = 0;//TABLEX and TABLEY describe where the table appears in the panel
	final static int storeCounterY = 200;
	final static int storeCounterLeftWIDTH= 700;
	final static int storeCounterRightWIDTH= 50;
	final static int storeCounterXHEIGHT = 30;
	private MarketData marketData;
	final static int TIMERCOUNTmilliseconds = 1;
	private Image bufferImage;
	private Dimension bufferSize;
	private String marketName;
	private Map<Role,Gui> guis = new HashMap<Role,Gui>();

	public MarketAnimationPanel(String marketName1) {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		this.marketName=marketName1;
		
		Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		for(Gui gui : guis.values()) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		
		clearRemovedGuis();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );


		g2.setColor(Color.ORANGE);

		g2.fillRect(storeCounterX, storeCounterY, storeCounterLeftWIDTH, storeCounterXHEIGHT);//store counter
		g2.fillRect(750, storeCounterY, storeCounterRightWIDTH, storeCounterXHEIGHT);//store counter
		g2.fillRect(0,100,200,20);//top manager office wall
		g2.fillRect(200,100,20,40);//manager office top half right wall
		g2.fillRect(200,170,20,30);//manager office bottom half left wall
		g2.fillRect(400, 0, 30, 125);//shelve #1 from left
		g2.fillRect(500, 0, 30, 125);//shelve #2 from left
		g2.fillRect(600, 0, 30, 125);//shelve #3 from left
		g2.fillRect(700, 0, 30, 125);//shelve #4 from left

		g2.setColor(Color.BLACK);//sets text color...or anything following this line
		g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.fillRect(350, 200, 10, 30);//dividers between counter windows 
		g2.drawString("Window #1", 370, 220);
		g2.fillRect(450, 200, 10, 30);
		g2.drawString("Window #2", 470, 220);
		g2.fillRect(550, 200, 10, 30);
		g2.drawString("Window #3", 570, 220);
		g2.fillRect(650, 200, 10, 30);
		g2.drawString("Entrance", 380, 395);

		g2.drawString("Manager's Office", 60, 115);
		g2.drawString("Store Shelves Area", 510, 20);
		g2.drawString("Delivery Truck", 10, 20);

		int degrees = -90; // rotate text counter-clockwise
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(degrees));

		Font f = new Font("Serif", Font.PLAIN, 15);
		g2.setFont(f.deriveFont(at));
		g2.drawString("Steak", 420,100);
		g2.drawString("Chicken", 520, 100);
		g2.drawString("Burger", 620, 100);
		
		for(Gui gui : guis.values()) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

//	public void addGui(MarketEmployeeGui gui) {
//		guis.add(gui);
//	}
//
//	public void addGui(MarketManagerGui gui) {
//		guis.add(gui);		
//	}
//	public void addGui(MarketCustomerGui gui) {
//		guis.add(gui);	
//
//	}

	@Override
	public void addGuiForRole(Role r) {
		marketData=((Market) (BuildingList.findBuildingWithName(marketName))).getMarketData();
		if(r instanceof MarketCustomerRole){

			MarketCustomerRole marketCustomerRole = (MarketCustomerRole) r;
			if (marketData.getNumberOfCustomersInALine(0)==marketData.getNumberOfCustomersInALine(1) && marketData.getNumberOfCustomersInALine(0)==marketData.getNumberOfCustomersInALine(2)){
				MarketCustomerGui gui = new MarketCustomerGui(marketCustomerRole, 0);
				marketCustomerRole.setGui(gui);
				marketData.incrementNumerOfCustomersInALine(0);
				marketCustomerRole.setCounter(0);
				marketCustomerRole.setMarketData(marketData);
				guis.put(r, gui);
			}
			else if (marketData.getNumberOfCustomersInALine(0)>marketData.getNumberOfCustomersInALine(1) && marketData.getNumberOfCustomersInALine(1)==marketData.getNumberOfCustomersInALine(2)){
				MarketCustomerGui gui = new MarketCustomerGui(marketCustomerRole, 1);
				marketCustomerRole.setGui(gui);
				marketData.incrementNumerOfCustomersInALine(1);
				marketCustomerRole.setCounter(1);
				marketCustomerRole.setMarketData(marketData);
				guis.put(r, gui);
			}
			else if (marketData.getNumberOfCustomersInALine(0)==marketData.getNumberOfCustomersInALine(1)){
				MarketCustomerGui gui = new MarketCustomerGui(marketCustomerRole, 2);
				marketCustomerRole.setGui(gui);
				marketData.incrementNumerOfCustomersInALine(2);
				marketCustomerRole.setCounter(2);
				marketCustomerRole.setMarketData(marketData);
				guis.put(r, gui);
			}

		}
		if(r instanceof MarketEmployeeRole){
			MarketEmployeeRole marketEmployeeRole = (MarketEmployeeRole) r;
			MarketEmployeeGui gui = new MarketEmployeeGui(marketEmployeeRole);
			marketEmployeeRole.setGui(gui);
			marketEmployeeRole.setMarketData(marketData);
			guis.put(r, gui);

		}
		if(r instanceof MarketManagerRole){
			MarketManagerRole marketManagerRole = (MarketManagerRole) r;
			MarketManagerGui gui = new MarketManagerGui(marketManagerRole);
			marketManagerRole.setGui(gui);
			marketManagerRole.setDeliveryTruck(new DeliveryTruckAgent("Delivery Truck", marketName));
			marketData.setMarketManager(marketManagerRole);
			marketManagerRole.setMarketData(marketData);
			guis.put(r, gui);

		}
	}
	
	private List<Role> removalList = new ArrayList<Role>();

	private void clearRemovedGuis(){
		for(int i = removalList.size()-1; i >= 0; i--){
			guis.remove(removalList.get(i));
			removalList.remove(i);
		}
	}
	
	@Override
	public void removeGuiForRole(Role r) {
		
		removalList.add(r);
	}



}
