package Transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import gui.interfaces.Passenger;

public class MockPasssenger implements Passenger{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
		
	@Override
	public void msgBusIsHere() {
		log.add(new LoggedEvent("Received msgBusIsHere from bus stop"));
	}

	@Override
	public void msgArrivedAtDestination(String location) {
		log.add(new LoggedEvent("Received msgArrivedAtDestination from bus"));
		
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
