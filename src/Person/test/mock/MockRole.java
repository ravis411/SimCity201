package Person.test.mock;

import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;

public class MockRole extends Role{

	public AlertLog log = AlertLog.getInstance();
	
	@Override
	public boolean pickAndExecuteAction() {
		// TODO Auto-generated method stub
		log.logMessage(AlertTag.PERSON, getNameOfRole(), "Role Scheduler Called");
		return true;
	}

	@Override
	public boolean canGoGetFood() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return "mockrole";
	}

}
