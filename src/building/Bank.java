package building;

import java.util.ConcurrentModificationException;

import bank.gui.BankAnimationPanel;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;
import gui.Building.BuildingPanel;
import Person.Role.Employee;
import Person.Role.Role;
import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanNumberAnnouncer;
import bank.LoanTellerRole;
import bank.NumberAnnouncer;

public class Bank extends Workplace {

	public boolean isOpenSetter = false;
	public Bank(BuildingPanel panel) {
		super(panel);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isOpen() {
		boolean hasTeller = false, hasLoanTeller = false;
		synchronized(inhabitants){
			for(Role r : inhabitants){
				if(r instanceof BankTellerRole){
					hasTeller = true;
				}else if(r instanceof LoanTellerRole){
					hasLoanTeller = true;
				}
			}
		}
		return hasTeller || hasLoanTeller;
	}

	public void notifyEmployeesTheyCanLeave() {

		synchronized(inhabitants){
			//List<Role> removalList = new ArrayList<Role>();
			try{
				for(Role r : inhabitants){
					if(r instanceof BankClientRole){
						BankClientRole bcr = (BankClientRole) r;
						bcr.bankClosing();
					}else if(r instanceof LoanTellerRole){
						LoanTellerRole ltr = (LoanTellerRole) r;
						ltr.bankClosing();
					}else{
						BankTellerRole btr = (BankTellerRole) r;
						btr.bankClosing();
					}

					if(r instanceof Employee){
						Employee e = (Employee) r;
						e.getPerson().msgYouCanLeave();
						e.deactivate();
						//removeRole(r);
					}
				}
				ready = false;
				//this.removeInhabitants();
			} catch (ConcurrentModificationException e){
				
			}
		}	
	}	

public NumberAnnouncer getAnnouncer() {
	return ((BankAnimationPanel) this.panel.getPanel()).getAnnouncer();
}

public LoanNumberAnnouncer getLoanAnnouncer(){
	return ((BankAnimationPanel) this.panel.getPanel()).getLoanAnnouncer();
}


}
