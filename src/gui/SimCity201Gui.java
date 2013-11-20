package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;
import gui.Building.BusStopBuilding;
import gui.Building.BusStopBuildingPanel;
import gui.MockAgents.MockVehicleAgent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import astar.AStarTraversal;

@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800;
	
	SimCityLayout layout = null;// <-This holds the grid information
	CityAnimationPanel cityPanel = null;//<-AnimationPanel draws the layout and the GUIs
	BuildingsPanels buildingsPanels = null;//<-Zoomed in view of buildings
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		
		
		
		SetUpWorld.LoadDefault();
		layout = SetUpWorld.layout;
		cityPanel = SetUpWorld.cityPanel;
		buildingsPanels = SetUpWorld.buildingsPanels;
		
		//add a mockVehicle
		MockVehicleAgent v1 = new MockVehicleAgent(true);
		VehicleGui v1Gui = new VehicleGui( v1, layout, new AStarTraversal(layout.getRoadGrid()) );
		v1.agentGui = v1Gui;
		cityPanel.addGui(v1Gui);
		v1.startThread();
		//mockVehicle Added
		//add a mockVehicle
				MockVehicleAgent v2 = new MockVehicleAgent(false);
				VehicleGui v2Gui = new VehicleGui( v2, layout, new AStarTraversal(layout.getRoadGrid()) );
				v2.agentGui = v2Gui;
				cityPanel.addGui(v2Gui);
				v2.startThread();
				//mockVehicle Added
				
				
		
		
		add(cityPanel);
		add(buildingsPanels);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
        gui.setTitle("SimCity201 V0.5  - Team 29");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
