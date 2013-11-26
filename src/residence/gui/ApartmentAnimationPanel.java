package residence.gui;

import gui.Building.BuildingGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ApartmentAnimationPanel extends JPanel implements MouseListener  {
	private List<BuildingGui> apartments = new ArrayList<>();
	
	static final int XCOOR = 0;
	static final int YCOOR = 0;
	
    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
	
	
	public ApartmentAnimationPanel() {
		setVisible(true);

		this.setBackground(Color.BLACK);
		addMouseListener(this);

	}
	
	/*public void addBuilding(Building b) {
		buildings.add(b);
		
	}*/
	public void mouseClicked(MouseEvent me) {
		/*for(Building b : buildings) {
			if(b.contains(me.getX(), me.getY())){
				b.displayBuilding();
			}
		}*/
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
	
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(getBackground());
        g2.fillRect(XCOOR, YCOOR, WINDOWX, WINDOWY);
        
        g2.setColor(Color.orange);
        g.fillOval(50, 50, 100, 100);
        g.fillOval(200, 50, 100, 100);
        g.fillOval(50, 200, 100, 100);
        g.fillOval(200, 200, 100, 100);
	}
}