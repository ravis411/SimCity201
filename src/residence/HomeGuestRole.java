package residence;

import java.util.concurrent.Semaphore;

import residence.HomeRole.AgentEvent;
import residence.gui.HomeGuestGui;
import residence.gui.HomeRoleGui;
import residence.interfaces.HomeGuest;
import Person.PersonAgent;
import Person.Role.Role;

/**
 * Home Role
 */

public class HomeGuestRole extends Role implements HomeGuest {
	
	HomeGuestGui gui = null;
	PersonAgent myPerson;
	private Semaphore atFrontDoor = new Semaphore(0, true);
	private Semaphore atCenter = new Semaphore(0, true);
	
	public enum AgentState
	{DoingNothing, WalkingIn, Leaving};
	private AgentState state = AgentState.DoingNothing;
	
	public HomeGuestRole(PersonAgent myPerson) {
		this.myPerson = myPerson;
	}
	
	public void setGui(HomeGuestGui gui){
		this.gui = gui;
	} 
	
	public String getNameOfRole() {
		return "HomeGuestRole";
	}
	
	public boolean canGoGetFood() {
		return false;
	}
	
	//messages
	
	public void msgComeIn() {
		state = AgentState.WalkingIn;
		stateChanged();
	}
	public void msgPartyOver() {
		state = AgentState.Leaving;
		stateChanged();
	}

	public void msgAtFrontDoor() {
		deactivate();
		atFrontDoor.release();
	}
	public void msgAtCenter() {
		atCenter.release();
	}
	
	//scheduler
	
	public boolean pickAndExecuteAction() {
		if(state == AgentState.WalkingIn) {
			enterHome();
			return true;
		}
		if(state == AgentState.Leaving) {
			leaveHome();
			return true;
		}
		return false;
	}
	
	//actions
	
	private void enterHome() {
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void leaveHome() {
		gui.DoGoToFrontDoor();
		try {
			atFrontDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}