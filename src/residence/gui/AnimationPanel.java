package residence.gui;

import gui.Building.BuildingPanel;
import gui.Building.ResidenceBuildingPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import residence.HomeGuestRole;
import residence.HomeRole;
import Person.Role.Role;

public class AnimationPanel extends JPanel implements ActionListener, GuiPanel {
	
	static final int XCOOR = 0;
	static final int YCOOR = 0;
	
    private final int WINDOWX = 800;
    private final int WINDOWY = 400;
    private ImageIcon floor;
    BufferedImage img;
    ImageObserver observer;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        
        /*File f1 = new File("wood-floor-lowres.jpg");  
    	String path = f1.getAbsolutePath();
    	System.out.println(path);*/
        
        //String path = getClass().getClassLoader().getResource(".").getPath();
        //System.out.println(path);
        
        //String s = System.getProperty("user.dir");
        //System.out.println(s);
        
        /*try {
            img = ImageIO.read(new File(s + "/images/wood-floor-lowres.jpg"));
        } catch (IOException e) {
        }*/
        
        //floor = new ImageIcon("src/residence/gui/wood-floor-lowres.jpg");
        //this.add(floor);

    	Timer timer = new Timer(10, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		ResidenceBuildingPanel bp = (ResidenceBuildingPanel) this.getParent();
		 for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		 
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(XCOOR, YCOOR, WINDOWX, WINDOWY);
//        
        //g.drawImage(img, 0, 0, 800, 400, observer);

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
        g2.fillRect(370, 220, 75, 55);
        g2.setColor(Color.orange); //chairs
        g2.fillRect(371, 195, 20, 20);
        g2.fillRect(396, 195, 20, 20);
        g2.fillRect(421, 195, 20, 20);
        
        //bedroom
        g2.setColor(Color.white); //bed
        g2.fillRect(50,70,70,60);
        
        //living room
        g2.setColor(Color.black); //tv
        g2.fillRect(625, 50, 40, 20);
        g2.setColor(Color.ORANGE); //rug
        g2.fillRect(620, 100, 50, 50);
        
        //walls
        g2.setColor(Color.lightGray);
        g2.fillRect(200, 10, 5, 100); //bedroom kitchen division
        g2.fillRect(200, 200, 5, 100);
        g2.fillRect(10, 300, 750, 5); //perimeter
        g2.fillRect(10, 10, 5, 290);
        g2.fillRect(10, 10, 750, 5);
        g2.fillRect(760, 10, 5, 110);
        g2.fillRect(760, 210, 5, 95);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(HomeRoleGui gui) {
        guis.add(gui);
    }

	@Override
	public void addGuiForRole(Role r) {
		// TODO Auto-generated method stub
		if(r instanceof HomeRole){
			HomeRole hr = (HomeRole) r;
			HomeRoleGui gui = new HomeRoleGui(hr);
			hr.setGui(gui);
			guis.add(gui);
			BuildingPanel bp = (BuildingPanel) this.getParent();
			System.out.println("Added to "+bp.getName());
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
		if(r instanceof HomeGuestRole){
			HomeGuestRole hgr = (HomeGuestRole) r;
			HomeGuestGui gui = new HomeGuestGui(hgr);
			hgr.setGui(gui);
			guis.add(gui);
			BuildingPanel bp = (BuildingPanel) this.getParent();
			System.out.println("Added to "+bp.getName());
			//System.out.println("My person is: " + hr.myPerson.getName());
		}
	}

	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}
    
}