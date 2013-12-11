package building;

import gui.Building.BuildingPanel;
import interfaces.MarketEmployee;
import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCook;
import Person.Role.Employee;
import Person.Role.Role;

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
		boolean hasManager = false, hasThreeEmployee = false;
		int numEmployee=0;
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof MarketManager){
					hasManager = true;
				}else if(r instanceof MarketEmployee){
					numEmployee++;
					if(numEmployee==3)
						hasThreeEmployee = true;
				}
			}
		}
		
		return hasManager && hasThreeEmployee;
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
