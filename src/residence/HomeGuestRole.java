package residence;

import interfaces.HomeGuest;

import java.util.concurrent.Semaphore;

import residence.gui.HomeGuestGui;
import Person.Role.Role;

/**
 * Home Guest Role
 */

public class HomeGuestRole extends Role implements HomeGuest {
	
	HomeGuestGui gui = null;
	private Semaphore atFrontDoor = new Semaphore(0, true);
	private Semaphore atCenter = new Semaphore(0, true);
	
	public enum AgentState
	{DoingNothing, WalkingIn, Leaving};
	public AgentState state = AgentState.DoingNothing;
	
	public HomeGuestRole() {
		super();
	}
	
	public void setGui(HomeGuestGui gui){
		this.gui = gui;
	} 
	
	public String getNameOfRole() {
		return "residence.HomeGuestRole";
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
		gui.leaveParty = false;
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
		gui.leaveParty = true;
		try {
			atFrontDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}