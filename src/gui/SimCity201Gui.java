package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
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
		setBounds(50, 50, (int)(WINDOWX * 2), (WINDOWY));
		setLayout(new GridLayout(0, 2));
		
		menuBar = new GuiJMenuBar(this);
		this.setJMenuBar(menuBar);
		
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		AlertLog.getInstance().addAlertListener(tracePanel);
		//JFrame logs = new JFrame("Logs");
		//logs.add(tracePanel);
		//logs.setBounds(850, 50, 400, 400);
	//	logs.setVisible(true);
		
		mainPanel.setLayout(new GridLayout(0, 1));
		add(mainPanel);
		add(tracePanel);
		
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
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
		
	}
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
	}
}
