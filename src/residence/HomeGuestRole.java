package residence;

import residence.interfaces.HomeGuest;
import Person.Role.Role;

/**
 * Home Role
 */

public class HomeGuestRole extends Role implements HomeGuest {
	public enum AgentState
	{DoingNothing};
	private AgentState state = AgentState.DoingNothing;
	
	String name;
	
	public HomeGuestRole(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean canGoGetFood() {
		return false;
	}
	
	public boolean pickAndExecuteAction() {
		return false;
	}
}