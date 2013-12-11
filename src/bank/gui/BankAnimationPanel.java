package bank.gui;

import interfaces.GuiPanel;
import interfaces.Person;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import Person.PersonAgent;
import Person.Role.Role;
import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanNumberAnnouncer;
import bank.LoanTellerRole;
import bank.NumberAnnouncer;
import building.BuildingList;


/**
 * 
 * @author Byron Choy
 *
 */
public class BankAnimationPanel extends JPanel implements ActionListener, GuiPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int WINDOWX = 800;//
	private final static int WINDOWY = 400;
	final static int counterX = 0;//TABLEX and TABLEY describe where the table appears in the panel
	final static int counterY = 200;
	final static int counterWIDTH= 800;
	final static int counterXHEIGHT = 30;
 
	final static int TIMERCOUNTmilliseconds = 5;
	private Image bufferImage;
	private Dimension bufferSize;

	
    public NumberAnnouncer announcer = new NumberAnnouncer("NumberBot");
    public LoanNumberAnnouncer loanAnnouncer = new LoanNumberAnnouncer("LoanBot");
    

 
    
    
	private List<Gui> guis = new ArrayList<Gui>();

	public BankAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		
		bufferSize = this.getSize();
		

		
		Timer timer = new Timer(TIMERCOUNTmilliseconds, this );
		timer.start();
		

        

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
	}

	@Override
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );

		g2.setColor(Color.WHITE);
		//fillRect(xPosition,yPosition,xLength,yLength)
		g2.fillRect(0, 250,150,150);
		
		
		
		g2.setColor(Color.ORANGE);

		g2.fillRect(counterX, counterY, counterWIDTH, counterXHEIGHT);//store counter
		g2.fillRect(140, 0, 30, 175);
		g2.fillRect(240, 0, 30, 175);
		g2.fillRect(340, 0, 30, 175);
		g2.fillRect(440, 0, 30, 175);
		g2.fillRect(540, 0, 30, 175);
		g2.fillRect(640, 0, 30, 175);
		
		
		g2.setColor(Color.BLACK);//sets text color...or anything following this line
		g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.drawString("Break Area", 0, 20);
		g2.fillRect(150, 200, 10, 30);//dividers between counter windows 
		g2.drawString("Window #1", 170, 220);
		g2.fillRect(250, 200, 10, 30);
		g2.drawString("Window #2", 270, 220);
		g2.fillRect(350, 200, 10, 30);
		g2.drawString("Window #3", 370, 220);
		g2.fillRect(450, 200, 10, 30);
		g2.drawString("Window #4", 470, 220);
		g2.fillRect(550, 200, 10, 30);
		g2.drawString("Window #5", 570,220);
		g2.fillRect(650, 200, 10, 30);
		g2.drawString("Waiting Area", 30, 245);
		g2.drawString("Employee",700, 20);
		g2.drawString("Entrance",700,40);
		g2.drawString("Entrance", 700, 370);
		
		

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(ClientGui gui) {
		guis.add(gui);
	}

	public void addGui(LoanGui gui) {
		guis.add(gui);                
	}
	public void addGui(TellerGui gui) {
		guis.add(gui);        

	}

	public void addGuiForRole(Role r) {
		if (r instanceof BankClientRole){
		    BankClientRole clientRole = (BankClientRole) r;
		    ClientGui clientGui = new ClientGui(clientRole, this);
	        this.addGui(clientGui);
	        clientRole.setAnnouncer(announcer);
	        clientRole.setLoanAnnouncer(loanAnnouncer);
	        clientRole.setGui(clientGui);
		}
		if (r instanceof BankTellerRole){
		    BankTellerRole tellerRole = (BankTellerRole) r;
		    TellerGui tellerGui = new TellerGui(tellerRole, this, tellerRole.getLine());
	        this.addGui(tellerGui);
	        tellerRole.setAnnouncer(announcer);
	        tellerRole.setGui(tellerGui);
		}
		if (r instanceof LoanTellerRole){
		    LoanTellerRole loanTellereRole=(LoanTellerRole) r;
		    LoanGui loanGui = new LoanGui(loanTellereRole, this);
	        this.addGui(loanGui);
	        loanTellereRole.setAnnouncer(loanAnnouncer);
	        loanTellereRole.setGui(loanGui);
		}
	}

	public void removeGuiForRole(Role r) {
		if (r instanceof BankClientRole){
		    BankClientRole clientRole = (BankClientRole) r;
		    guis.remove(clientRole.getGui());
		}
		if (r instanceof BankTellerRole){
		    BankTellerRole tellerRole = (BankTellerRole) r;
		    guis.remove(tellerRole.getGui());
		}
		if (r instanceof LoanTellerRole){
		    LoanTellerRole loanTellereRole=(LoanTellerRole) r;
		    guis.remove(loanTellereRole.getGui());
		}
	}
	
	public NumberAnnouncer getAnnouncer(){
		return announcer;
	}
	
	public LoanNumberAnnouncer getLoanAnnouncer(){
		return loanAnnouncer;
	}


}