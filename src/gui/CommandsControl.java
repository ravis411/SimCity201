package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Person.PersonAgent;
/**
 * GUI class for the command window that pops up 
 * from the control panel
 * @author JEFFREY
 *
 */
public class CommandsControl extends JFrame implements ActionListener {
	
	public JScrollPane pane1 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public JScrollPane pane2 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private JPanel Container = new JPanel();
	private JPanel friendsListPanel = new JPanel();
	private JPanel locationsListPanel = new JPanel();
	
	private List<JCheckBox> friendsList = new ArrayList<JCheckBox>();
	private List<JRadioButton> locationsList = new ArrayList<JRadioButton>();
	
	private JButton throwPartyB = new JButton("Throw a Party");
	private JButton getHungryB = new JButton("Make hungry");
	private JButton makeFriendB = new JButton("Make new friends");
	private JButton addMoneyB = new JButton("Add Funds");
	private JButton goToB = new JButton("Go!");

	private ButtonGroup locations = new ButtonGroup();
	
	private JTextField moneyTF = new JTextField("$$.$$");
	
	private CityControlPanel controller;
	
	CommandsControl(CityControlPanel parent) {
		controller = parent;
		setLayout(new GridLayout(1,1));
		Container.setLayout(new GridLayout(4,2));
		
		throwPartyB.addActionListener(this);
		Container.add(throwPartyB);
		
		getHungryB.addActionListener(this);
		Container.add(getHungryB);
		
		Container.add(moneyTF);
		addMoneyB.addActionListener(this);
		Container.add(addMoneyB);
		
		friendsListPanel.setLayout(new GridLayout(SetUpWorldFactory.agents.size(), 1));
		
		for(PersonAgent agent : SetUpWorldFactory.agents) {
			JCheckBox friendCB = new JCheckBox(agent.getName());
			friendsList.add(friendCB);
			friendsListPanel.add(friendCB);
		}
		
		pane1.setViewportView(friendsListPanel);
		Container.add(pane1);
		
		makeFriendB.addActionListener(this);
		Container.add(makeFriendB);
		
		locationsListPanel.setLayout(new GridLayout(SetUpWorldFactory.locationsList.size(), 1));
		
		for (String place : SetUpWorldFactory.locationsList) {
			JRadioButton placeRB = new JRadioButton(place);
			locations.add(placeRB);
			locationsList.add(placeRB);
			locationsListPanel.add(placeRB);
		}
		
		pane2.setViewportView(locationsListPanel);
		Container.add(pane2);
		
		goToB.addActionListener(this);
		Container.add(goToB);
		
		add(Container);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == throwPartyB) {
			throwParty();
		}
		else if (e.getSource() == getHungryB) {
			getHungry();
		}
		else if (e.getSource() == makeFriendB) {
			addFriends();
		}
		else if (e.getSource() == addMoneyB) {
			addMoney();
		}
		else if (e.getSource() == goToB) {
			//TODO
		}
		
	}
	
	private void addFriends() {
		try {
			List<String> people = new ArrayList<String>();
			for (JCheckBox person : friendsList) {
				if (person.isSelected()) {
					people.add(person.getText());
				}
			}
			controller.personAddFriends(people);
		} catch (NullPointerException n) {
			controller.errorPopUp("Select a person first!");
		}
	}
	private void getHungry() {
		try {
		controller.personGetHungry();
		} catch (NullPointerException n) {
			controller.errorPopUp("Select a person first!");
		}
	}
	private void throwParty() {
		try {
			controller.personThrowParty();
		} catch (NullPointerException n) {
			controller.errorPopUp("Select a person first!");
		}
	}
	private void addMoney() {
		try {
			Double funds = Double.parseDouble(moneyTF.getText());
			controller.personAddMoney(funds);
		} catch (NumberFormatException e) {
			controller.errorPopUp("Input a double for money!");
		}
	}
}
