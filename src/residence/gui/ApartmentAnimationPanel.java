package residence.gui;

import gui.Gui;
import gui.Building.ResidenceBuilding;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Person.Role.Role;

public class ApartmentAnimationPanel extends JPanel implements MouseListener, GuiPanel  {
	private List<Gui> guis = new ArrayList<Gui>();
	private List<ResidenceBuilding> apartments = new ArrayList<>();
	
	static final int XCOOR = 0;
	static final int YCOOR = 0;
	
    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
	
	
	public ApartmentAnimationPanel() {
		setVisible(true);

		this.setBackground(Color.BLACK);
		addMouseListener(this);

	}
	
	public void addApartment(ResidenceBuilding b) {
		apartments.add(b);
		guis.add(b);
	}
	
	public void addGui(ResidenceBuilding gui){
		addApartment(gui);
	}

	
	public void mouseClicked(MouseEvent me) {
		for(ResidenceBuilding rb : apartments) {
			if(rb.contains(me.getX(), me.getY())){
				rb.displayBuilding();
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
	
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(getBackground());
        g2.fillRect(XCOOR, YCOOR, WINDOWX, WINDOWY);
        
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
        
        g2.setColor(Color.orange);
        g2.fillRect(300,0,200,400);
	}

	@Override
	public void addGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}
}