package residence.gui;

import gui.Building.Building;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ApartmentAnimationPanel extends JPanel implements MouseListener  {
	private List<Building> buildings = new ArrayList<>();
	
	public ApartmentAnimationPanel() {
		setVisible(true);

		this.setBackground(Color.BLACK);
		addMouseListener(this);

	}
	
	public void addBuilding(Building b) {
		buildings.add(b);
		
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