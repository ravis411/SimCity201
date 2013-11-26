package Transportation;

import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Person.Role.Role;
import interfaces.GuiPanel;

/**	An animation panel for bus stops.
 * 
 * @author Team29
 *
 */
public class BusStopAnimationPanel extends JPanel implements GuiPanel{

	private JScrollPane passengerPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	
	private List<JButton> passengerList = new ArrayList<>();
	
	public BusStopAnimationPanel() {
	//	this.add(new JLabel("BusStopAnimationPanel!!!!!!!!!!!!!"));
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		passengerPane.setViewportView(view);
		
		Dimension paneSize  = new Dimension(200, 350);
		passengerPane.setMaximumSize(paneSize);
		passengerPane.setMinimumSize(paneSize);
		passengerPane.setPreferredSize(paneSize);
		
		this.add(passengerPane);		
	}
	
	
	public void addWaitingPassenger(String name){
		JButton b = new JButton(name);
		view.add(b);
		passengerList.add(b);
		this.validate();
	}
	public void removeWaitingPassenger(String name){
		for(JButton b : passengerList){
			if(b.getText() == name){
				view.remove(b);
				passengerList.remove(b);
				this.validate();
				return;
			}
		}
	}
	
	
	
	@Override
	public void addGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGuiForRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
