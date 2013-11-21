package gui;

import java.awt.GridLayout;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800;
	
	SimCityLayout layout = null;// <-This holds the grid information
	CityAnimationPanel cityPanel = null;//<-AnimationPanel draws the layout and the GUIs
	BuildingsPanels buildingsPanels = null;//<-Zoomed in view of buildings
	GuiJMenuBar menuBar = null; //<<-- a menu for the user
	SetUpWorldFactory factory = null; //<<-- used to Initialize all agents guis etc
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		setTitle("SimCity201 V0.5  - Team 29");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		
		menuBar = new GuiJMenuBar(this);
		this.setJMenuBar(menuBar);
		
		
		
		loadConfig("Default");
		//loadConfig("GUI Test 1");
	}
	
	public SimCity201Gui(String config) {
		setTitle("SimCity201 V0.5  - Team 29 --" + config);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		menuBar = new GuiJMenuBar(this);
		this.setJMenuBar(menuBar);
		loadConfig(config);
	}
	
	
	/** Loads the config
	 * 
	 */
	void loadConfig(String config){
		this.getContentPane().removeAll();
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
			System.out.println("ERROR");
			break;
		}
		
		
		//setJMenuBar(menuBar);
		add(cityPanel);
		add(buildingsPanels);	
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
		
	}
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
	}
}
