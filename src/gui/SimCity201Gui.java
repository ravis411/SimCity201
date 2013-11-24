package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import trace.*;


@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800;
	
	SimCityLayout layout = null;// <-This holds the grid information
	CityAnimationPanel cityPanel = null;//<-AnimationPanel draws the layout and the GUIs
	BuildingsPanels buildingsPanels = null;//<-Zoomed in view of buildings
	GuiJMenuBar menuBar = null; //<<-- a menu for the user
	SetUpWorldFactory factory = null; //<<-- used to Initialize all agents guis etc
	TracePanel tracePanel = new TracePanel();
	JPanel mainPanel = new JPanel(); //<<-- this holds the cityPanel and BuildingsPanels
	
	
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		setTitle("SimCity201 V0.5  - Team 29");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        menuBar = new GuiJMenuBar(this);
		this.setJMenuBar(menuBar);
        
        setBounds(50, 50, (int)(WINDOWX * 1.5), (WINDOWY + 50));
		//setLayout(new GridLayout(1, 2));
		setLayout(new GridBagLayout());
		
		
		
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; c.gridy = 0;
		this.add(mainPanel, c);
		
		c.gridx = 1; c.gridy = 0;
		//c.ipady = 200;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = .8;
		this.add(tracePanel, c);
		
		mainPanel.setLayout(new GridLayout(0, 1));
		//add(mainPanel);
		//add(tracePanel);
	
		
		loadConfig("Default");
		//loadConfig("GUI Test 1");
	}
	
	
	/** Loads the config
	 * 
	 */
	void loadConfig(String config){
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, "City", "Loading " + config + " configuration.");
		
		mainPanel.removeAll();
		cityPanel = null;
		layout = null;
		buildingsPanels = null;
		factory = null;
		System.gc();
		factory = new SetUpWorldFactory();
		
		switch (config) {
		case "Default":
			factory.LoadDefault();
			layout = factory.layout;
			cityPanel = factory.cityPanel;
			buildingsPanels = factory.buildingsPanels;
			break;
			
		case "GUI Test 1":
			factory.LoadGUITest1();
			layout = factory.layout;
			cityPanel = factory.cityPanel;
			buildingsPanels = factory.buildingsPanels;
			break;
		case"GUI Test 2":
			factory.LoadGUITest2();
			layout = factory.layout;
			cityPanel = factory.cityPanel;
			buildingsPanels = factory.buildingsPanels;
			break;
		default:
			AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, "City", "Error loading " + config + " configuration.");
			break;
		}
		
		
		//setJMenuBar(menuBar);
		mainPanel.add(cityPanel);
		mainPanel.add(buildingsPanels);
		mainPanel.revalidate();
		mainPanel.repaint();
		//this.getContentPane().revalidate();
		//this.getContentPane().repaint();
		
	}
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
	}
}
