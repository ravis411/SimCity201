package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GuiJMenuBar extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SimCity201Gui gui = null;
	JMenu fileMenu, loadSubmenu, viewMenu, viewSettingsSubmenu;
	//FileMenu Items
	JMenuItem exit, loadDefault, loadGuiTest1, loadGuiTest2;
	//View Menu Items
	JMenuItem viewDefault, viewTesting, showTeam29Panel, showControlPanel;
	
	
	public GuiJMenuBar(SimCity201Gui gui) {
		this.gui = gui;
		
		fileMenu = new JMenu("File");
		add(fileMenu);
		
		
		//Set up the "Load" submenu
		loadSubmenu = new JMenu("Load");
		fileMenu.add(loadSubmenu);
		//loadDefault = new JMenuItem("Load Default");
		//loadDefault.addActionListener(this);
		//loadSubmenu.add(loadDefault);
		loadGuiTest1 = new JMenuItem("Load Gui Test 1");
		loadGuiTest1.addActionListener(this);
		loadSubmenu.add(loadGuiTest1);
		//loadGuiTest2 = new JMenuItem("Load Gui Test 2");
	//	loadGuiTest2.addActionListener(this);
	//	loadSubmenu.add(loadGuiTest2);
		
		fileMenu.addSeparator();
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		fileMenu.add(exit);
		
		
		
		//Set up the "View" menu
		viewMenu = new JMenu("View");
		add(viewMenu);
		viewSettingsSubmenu = new JMenu("Settings");
		viewMenu.add(viewSettingsSubmenu);
		viewDefault = new JMenuItem("Default View");
		viewDefault.addActionListener(this);
		viewTesting = new JMenuItem("Test View");
		viewTesting.addActionListener(this);
		viewSettingsSubmenu.add(viewDefault);
		viewSettingsSubmenu.add(viewTesting);
		showTeam29Panel = new JMenuItem("Team 29 Information");
		showTeam29Panel.addActionListener(this);
		showControlPanel = new JMenuItem("Sim City Controls");
		showControlPanel.addActionListener(this);
		viewMenu.addSeparator();
		viewMenu.add(showTeam29Panel);
		viewMenu.add(showControlPanel);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loadDefault) {
			System.out.println("Loading Default Configuration");
			gui.loadConfig("Default");;
		}
		else if(e.getSource() == loadGuiTest1) {
			gui.loadConfig("GUI Test 1");
		}
		else if(e.getSource() == loadGuiTest2) {
			gui.loadConfig("GUI Test 2");
		}
		
		
		
		//View menu
		else if(e.getSource() == viewDefault){
			gui.cityPanel.setTestView(false);
		}
		else if(e.getSource() == viewTesting){
			gui.cityPanel.setTestView(true);
		}
		else if(e.getSource() == showTeam29Panel){
			gui.buildingsPanels.displayBuildingPanel("Team29");
		}
		else if (e.getSource() == showControlPanel){
			gui.buildingsPanels.displayBuildingPanel("Controls");
		}
				
		
		else if(e.getSource() == exit) {
			System.exit(0);
		}

		
	}

	
	
}
