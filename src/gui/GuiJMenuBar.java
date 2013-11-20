package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GuiJMenuBar extends JMenuBar implements ActionListener {

	SimCity201Gui gui = null;
	JMenu fileMenu, loadSubmenu, viewMenu;
	//FileMenu Items
	JMenuItem exit, loadDefault, loadGuiTest1, loadGuiTest2;
	//View Menu Items
	JMenuItem viewDefault;
	
	
	public GuiJMenuBar(SimCity201Gui gui) {
		this.gui = gui;
		
		fileMenu = new JMenu("File");
		add(fileMenu);
		
		
		//Set up the "Load" submenu
		loadSubmenu = new JMenu("Load");
		fileMenu.add(loadSubmenu);
		loadDefault = new JMenuItem("Load Default");
		loadDefault.addActionListener(this);
		loadSubmenu.add(loadDefault);
		loadGuiTest1 = new JMenuItem("Load Gui Test1 A* Works Larger Grid");
		loadGuiTest1.addActionListener(this);
		loadSubmenu.add(loadGuiTest1);
		loadGuiTest2 = new JMenuItem("Load Gui Test2 A* FREEEZES");
		loadGuiTest2.addActionListener(this);
		loadSubmenu.add(loadGuiTest2);
		
		fileMenu.addSeparator();
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		fileMenu.add(exit);
		
		
		
		//Set up the "View" menu
		viewMenu = new JMenu("View");
		add(viewMenu);
		viewDefault = new JMenuItem("View Default");
		viewMenu.add(viewDefault);
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
		else if(e.getSource() == exit) {
			System.exit(0);
		}

		
	}

	
	
}
