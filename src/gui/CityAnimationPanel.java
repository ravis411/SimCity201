package gui;

import gui.Building.Building;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;




public class CityAnimationPanel extends JPanel implements MouseListener, ActionListener  {



	private final int WINDOWX;
	private final int WINDOWY;


	private List<Gui> guis = new ArrayList<Gui>();
	private List<Building> buildings = new ArrayList<>(); 

	private SimCityLayout layout = null;



	public CityAnimationPanel(SimCityLayout layout) {
		WINDOWX = layout.WINDOWX;
		WINDOWY = layout.WINDOWY;
		
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

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );


		//Paint the cityLayout
		if(layout != null) {
			layout.draw(g2);
		}
		
		g2.setColor(Color.orange);

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}

	}

	public void addGui(Gui gui) {
		guis.add(gui);
	}
	
	public void addGui(Building gui){
		addBuilding(gui);
	}
	
	public void addBuilding(Building b) {
		buildings.add(b);
		guis.add(b);
	}

	
	public void mouseClicked(MouseEvent me) {
		for(Building b : buildings) {
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

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
