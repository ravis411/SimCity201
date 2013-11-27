package gui;

import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import Person.Role.Role;

public class Team29Panel extends BuildingPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel title = new JLabel("SimCity V 1.0     Team 29");
	static BuildingGui b = new BuildingGui(0, 0, 0, 0);
	public Team29Panel(BuildingsPanels panels) {
		super(b, "Team29", panels ); 
		
		this.setBackground(Color.white);
		
		this.setLayout(new GridLayout(0, 1));
		
		this.removeAll();
		
		add(title);
		
		add(new JLabel("Kushaan Kumar"));
		add(new JLabel("Michael Ciesielka"));
		add(new JLabel("Dylan Resnik"));
		add(new JLabel("Ryan Davis"));
		add(new JLabel("Jeffrey Chau"));
		add(new JLabel("Luca Spinazzola"));
		add(new JLabel("Byron Choy"));
		
	}
	
	
	public String getName() {
		return myName;
	}
	
	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public GuiPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}


