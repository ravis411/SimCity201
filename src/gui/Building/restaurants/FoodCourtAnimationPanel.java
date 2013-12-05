package gui.Building.restaurants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import trace.AlertLog;
import trace.AlertTag;

@SuppressWarnings("serial")
public class FoodCourtAnimationPanel extends JPanel implements MouseListener, ActionListener {

	
	
	
	
	
	
	public FoodCourtAnimationPanel() {
		
		addMouseListener(this);
		
		Timer timer = new Timer(20, this );
    	timer.start();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();		
	}	
	
	
	Rectangle2D r1 = new Rectangle2D.Double(30,30,50,50);
	Rectangle2D r2 = new Rectangle2D.Double(30,30,50,50);
	
	
	protected void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.fillRect(0, 0, 800, 400);
		
		g.setColor(Color.green);
		g.fill(r1);
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(r1.contains(e.getPoint())){
			AlertLog.getInstance().logDebug(AlertTag.RESTAURANT, "FOOD COURT", "STETSSesteste");
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
