package residence.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	
	static final int XCOOR = 0;
	static final int YCOOR = 0;
	
    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
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
        g2.fillRect(XCOOR, YCOOR, WINDOWX, WINDOWY );

        //Here is the kitchen
        g2.setColor(Color.lightGray); //counter top
        g2.fillRect(320, 30, 180, 30);
        g2.fillRect(500, 30, 30, 100);
        g2.setColor(Color.white); //range
        g2.fillRect(370, 30, 30, 30);
        g2.setColor(Color.red);
        g.fillOval(370, 30, 15, 15);
        g.fillOval(370, 45, 15, 15);
        g.fillOval(385, 30, 15, 15);
        g.fillOval(385, 45, 15, 15);
        g2.setColor(Color.white);
        g.fillOval(372, 32, 11, 11);
        g.fillOval(372, 47, 11, 11);
        g.fillOval(387, 32, 11, 11);
        g.fillOval(387, 47, 11, 11);
        g2.setColor(Color.red);
        g.fillOval(374, 34, 8, 8);
        g.fillOval(374, 49, 8, 8);
        g.fillOval(389, 34, 8, 8);
        g.fillOval(389, 49, 8, 8);
        g2.setColor(Color.white);
        g.fillOval(376, 36, 4, 4);
        g.fillOval(376, 51, 4, 4);
        g.fillOval(391, 36, 4, 4);
        g.fillOval(391, 51, 4, 4);
        g2.setColor(Color.black); //sink
        g2.fillRect(450, 32, 30, 26);
        g2.setColor(Color.cyan);
        g2.fillRect(453, 35, 24, 20);
        g2.setColor(Color.yellow); //table
        g2.fillRect(370, 100, 50, 30);
        
        //bedroom
        g2.setColor(Color.lightGray); //walls
        g2.fillRect(200, 30, 5, 100);
        g2.fillRect(200, 180, 5, 100);
        g2.setColor(Color.white); //bed
        g2.fillRect(50,70,70,60);
        
        //front door
        g2.setColor(Color.orange);
        g2.fillRect(750, 200, 5, 30);
        
        //living room
        g2.setColor(Color.black); //tv
        g2.fillRect(625, 50, 40, 20);
        g2.setColor(Color.ORANGE); //rug
        g2.fillRect(620, 100, 50, 50);
        

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

    public void addGui(HomeRoleGui gui) {
        guis.add(gui);
    }
    
}