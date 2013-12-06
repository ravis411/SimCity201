package gui.Building.restaurants;

import gui.Building.BuildingGui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class FoodCourtAnimationPanel extends JPanel implements MouseListener, ActionListener {

	
	List<BuildingGui> restBuildings = new ArrayList<BuildingGui>();
	
	
	
	
	public void addBuildingGui(BuildingGui rest){
		restBuildings.add(rest);
	}
	
	
	
	
	
	
	
	public FoodCourtAnimationPanel() {
		
		addMouseListener(this);
		
		Timer timer = new Timer(20, this );
    	timer.start();
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();		
	}	
	
	protected void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.fillRect(0, 0, 800, 400);
		
		g.setColor(Color.green);

		for(BuildingGui b : restBuildings){
			b.draw(g);
		}
		
	}
	
	
	
	
	
	
	/**	Sets the test view
	 * 
	 * @param test
	 */
	public void setTestView(boolean test){
		for(BuildingGui b : restBuildings){
			b.setTestView(test);
		}
	}
	
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for(BuildingGui b : restBuildings){
			if(b.contains(e.getPoint())){
				b.displayBuilding();
			}
		}
	}

	
	
	
	
	
	
	
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	

}
