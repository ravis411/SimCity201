package gui;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class AnimationPanel extends JPanel implements ActionListener{



	private final int WINDOWX;
	private final int WINDOWY;
	private final int GRID_SIZEX = 25;
	private final int GRID_SIZEY = 25;
	public int numxGrids = 0;
	public int numyGrids = 0;

	

	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	private SimCityLayout layout = null;



	public AnimationPanel(SimCityLayout layout) {
		WINDOWX = layout.WINDOWX;
		WINDOWY = layout.WINDOWY;
		
		setSize(WINDOWX, WINDOWY);

		this.layout = layout;
		

		

		setVisible(true);

		bufferSize = this.getSize();
		this.setBackground(Color.BLACK);

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


		//Paint some boxes;//and labels

		//String p = new String();
		   


		//Paint waiter home Positions
		if(layout != null) {
			layout.draw(g2);
		}



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

}
