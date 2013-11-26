package Transportation;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Person.Role.Role;
import interfaces.GuiPanel;

/**	An animation panel for bus stops.
 * 
 * @author Team29
 *
 */
public class BusStopAnimationPanel extends JPanel implements GuiPanel{

	
	public BusStopAnimationPanel() {
		this.add(new JLabel("BusStopAnimationPanel!!!!!!!!!!!!!"));
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
