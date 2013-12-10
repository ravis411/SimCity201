package building;

import Person.Role.Employee;
import Person.Role.Role;
import gui.Building.BuildingPanel;

public class Market extends  Workplace {

	public Market(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void notifyEmployeesTheyCanLeave() {
		// TODO Auto-generated method stub
		for(Role r : inhabitants){
			if(r instanceof Employee){
				r.deactivate();
			}
		}
	}

}
