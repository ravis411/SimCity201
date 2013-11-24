package Person.test.mock;

import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;

public class MockRole extends Role{

	AlertLog log = AlertLog.getInstance();
	
	@Override
	public boolean pickAndExecuteAction() {
		// TODO Auto-generated method stub
		log.logMessage(AlertTag.valueOf("ROLE"), getName(), "Scheduler Called");
		return false;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "mockrole";
	}

}
