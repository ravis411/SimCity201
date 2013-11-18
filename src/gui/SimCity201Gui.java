package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SimCity201Gui extends JFrame {

	private final int WINDOWX = 800;
	private final int WINDOWY = 800;
	
	SimCityLayout layout = new SimCityLayout(WINDOWX, WINDOWY/2);
	CityPanel cityPanel = new CityPanel(layout);
	BuildingsPanels buildingsPanels = new BuildingsPanels();
	
	
	
	/**
	 * Default Constructor Initializes gui
	*/
	public SimCity201Gui() {
		
		setBounds(50, 50, WINDOWX, WINDOWY);
		setLayout(new GridLayout(0, 1));
		
		
		
		/**
		 * This stuff prolly shouldn't be here but for testing purposes
		 */
		//down left
		if(!layout.addRoad(6, 5, 3, 20))
			System.out.println("addRoad unsuccessful");
		//across bottom
		if(!layout.addRoad(6, 25, 20, 3))
			System.out.println("addRoad unsuccessful");
		// across top
		if(!layout.addRoad(9, 5, 20, 3))
			System.out.println("addRoad unsuccessful");
		 //down right
		if(!layout.addRoad(26, 8, 3, 20))
			System.out.println("addRoad unsuccessful");
		//down middle
		if(!layout.addRoad(16, 8, 2, 17))
			System.out.println("addRoad unsuccessful");
		
		addBuildings();
		
		
		
		add(cityPanel);
		add(buildingsPanels);
	}
	
	
	/**Adds some buildings to the gui
	 * 
	 */
	public void addBuildings(){
		
		for(int i = 1; i < 10;i++) {
		Building b = layout.addBuilding(i * 2, i * 2, 2, 2 );
		if(b != null){
			BuildingPanel bp = new BuildingPanel(b, "Building " + i, buildingsPanels);
			b.setBuildingPanel(bp);
			cityPanel.addBuilding(b);
			buildingsPanels.addBuildingPanel(bp);
		}
		else
			System.out.println("FAILED ADDING BUILDING TO LAYOUT DURING SETTUP");
		}
	}
	
	
	
	public static void main(String[] args) {
		SimCity201Gui gui = new SimCity201Gui();
        gui.setTitle("SimCity201 V0.5  - Team 29");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
