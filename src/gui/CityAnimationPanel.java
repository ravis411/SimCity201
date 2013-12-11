package gui;

import gui.Building.BuildingGui;
import gui.agentGuis.DrunkDriverAgent;
import gui.agentGuis.DrunkPersonAgent;

import javax.swing.*;

import util.MasterTime;

import java.awt.*;
import java.awt.event.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import util.MasterTime;
import building.Building;
import building.BuildingList;




public class CityAnimationPanel extends JPanel implements MouseListener, ActionListener  {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int WINDOWX;
	private final int WINDOWY;


	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private List<BuildingGui> buildings = new ArrayList<BuildingGui>(); 

	private SimCityLayout layout = null;

	private float ampmAlpha = 0f;
	
	
	boolean testView = false;
	

	public CityAnimationPanel(SimCityLayout layout) {
		WINDOWX = layout.getWINDOWX();
		WINDOWY = layout.getWINDOWY();
		
		setSize(WINDOWX, WINDOWY);
		setMinimumSize( new Dimension( WINDOWX, WINDOWY ) );
		setMaximumSize( new Dimension( WINDOWX, WINDOWY ) );
		setPreferredSize( new Dimension( WINDOWX, WINDOWY ) );

		this.layout = layout;
		
		

		
		setVisible(true);

		this.setBackground(Color.BLACK);
		addMouseListener(this);

		Timer timer = new Timer(20, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		MasterTime.getInstance().add(Calendar.SECOND, 5);
		Building bdg = BuildingList.findBuildingWithName("Mike's Restaurant");
		//System.out.println(bdg.getInhabitants().size());
		//AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, "Calendar", calendar.toString());
		
		
		
		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );


		//Paint the cityLayout
		if(layout != null) {
			layout.draw(g2);
		}
		
		g2.setColor(Color.orange);

		synchronized (guis) {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
		
		g2.setColor(Color.orange);
		
//		for(Building b : buildings){
//			//why would a building not be present? //if(b.isPresent())
//			{
//			//	b.draw(g2);
//			}
//		}
		
		
		
		
		
		//This section makes the city dark at night and draws the clock.
		if(MasterTime.getInstance().get(Calendar.HOUR_OF_DAY) >= 18 && ampmAlpha < .3f){
			ampmAlpha += 0.001f;
		}else if(MasterTime.getInstance().get(Calendar.HOUR_OF_DAY) <= 6 && ampmAlpha > .01f)
		{
			ampmAlpha -= 0.001f;
		}
		if(!testView){
			AlphaComposite orig = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ampmAlpha);
			g2.setComposite(ac);
			g2.setColor(Color.black);
			g2.fillRect(0, 0, WINDOWX, WINDOWY);
			g2.setComposite(orig);
		}
		g2.setColor(Color.white);
		String time = MasterTime.getInstance().calendarString();
		g2.drawString(time, g.getClipBounds().width - 225, g.getClipBounds().height - 30);
	}

	
	
	
	
	
	
	
	public void addGui(Gui gui) {
		guis.add(gui);
	}
	
	public void addGui(BuildingGui gui){
		addBuilding(gui);
	}
	
	public void addBuilding(BuildingGui b) {
		buildings.add(b);
		guis.add(b);
		
	}


	public void setTestView(boolean testView){
		synchronized (guis) {
			for(Gui g : guis){
				g.setTestView(testView);
		}}
		this.testView = testView;
	}

	public void clear(){
		guis.clear();
		buildings.clear();
		layout = null;
	}
	
	public void mouseClicked(MouseEvent me) {
		for(BuildingGui b : buildings) {
			if(b.contains(me.getX(), me.getY())){
				b.displayBuilding();
			}
		}
	}
	
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	Point heldPoint = null;
	@Override
	public void mousePressed(MouseEvent e) {
		heldPoint = e.getPoint();
	}
	
	DrunkDriverAgent dca = null;
	DrunkPersonAgent dpa = null;
	
	@Override
	public void mouseReleased(MouseEvent e) {
		

		if( ( heldPoint.x < ((int)WINDOWX/2) && e.getPoint().x > ((int)WINDOWX/2) ) || 
				( heldPoint.x > ((int)WINDOWX/2) && e.getPoint().x < ((int)WINDOWX/2) ) ||
				( heldPoint.y < ((int)WINDOWY/2) && e.getPoint().y > ((int)WINDOWY/2) ) ||
				( heldPoint.y > ((int)WINDOWY/2) && e.getPoint().y < ((int)WINDOWY/2) )	){

			for (BuildingGui b : buildings) {
				if (b.contains(e.getPoint())) {

					if(e.getButton() == MouseEvent.BUTTON1){
						//Send the drunk Person here to run into the road.
						if (dca == null)
							dca = new DrunkDriverAgent("LIKE OMG");
						dca.msgGoTo(b.getName());
						dca.msgRunIntoTheRoad();
					}else{
						if (dpa == null)
							dpa = new DrunkPersonAgent("aahahaaaa");
						
						dpa.msgGoTo(b.getName());
						dpa.msgRunIntoTheRoad();
					}
				}
			}



		}//end drunk driver loop.
		
		
		
		
		
		heldPoint = null;
	}
	
}
