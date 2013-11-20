package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;
import gui.MockAgents.MockBusAgent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

import astar.AStarTraversal;

@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800 + 20;
	
	SimCityLayout layout = null;// <-This holds the grid information
	CityAnimationPanel cityPanel = null;//<-AnimationPanel draws the layout and the GUIs
	BuildingsPanels buildingsPanels = null;//<-Zoomed in view of buildings
	GuiJMenuBar menuBar = null; //<<-- a menu for the user
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		
		menuBar = new GuiJMenuBar(this);
		this.setJMenuBar(menuBar);
		
		loadDefaultConfig();
	}
	
	
	/** Loads the default settings
	 * 
	 */
	void loadDefaultConfig(){
		this.getContentPane().removeAll();
		
		SetUpWorldFactory.LoadDefault();
		layout = SetUpWorldFactory.layout;
		cityPanel = SetUpWorldFactory.cityPanel;
		buildingsPanels = SetUpWorldFactory.buildingsPanels;
		
		//setJMenuBar(menuBar);
		add(cityPanel);
		add(buildingsPanels);
		
		
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
		
	}
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
        gui.setTitle("SimCity201 V0.5  - Team 29");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
