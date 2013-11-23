package gui;

import gui.Building.Building;
import gui.Building.BuildingPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Team29Panel extends BuildingPanel{
	JLabel title = new JLabel("SimCity V0.5     Team 29");
	static Building b = new Building(0, 0, 0, 0);
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
}


