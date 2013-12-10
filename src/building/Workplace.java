package building;

import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanTellerRole;
import interfaces.BankClient;
import interfaces.BankTeller;
import interfaces.LoanTeller;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;
import ryansRestaurant.RyansCookRole;
import util.MasterTime;
import util.TimeListener;
import gui.Building.BuildingPanel;
import Person.Role.Employee;
import Person.Role.Role;

public abstract class Workplace extends Building implements TimeListener{

	public Workplace(BuildingPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
		MasterTime.getInstance().registerTimeListener(NIGHT_SHIFT_HOUR, NIGHT_SHIFT_MIN, false, this);
	}

	@Override
	public void timeAction(int hour, int minute) {
		// TODO Auto-generated method stub

		this.notifyEmployeesTheyCanLeave();
	}

	private boolean ready = false;

	public static int DAY_SHIFT_HOUR = 9;
	public static int DAY_SHIFT_MIN = 0;

	public static int NIGHT_SHIFT_HOUR = 17;
	public static int NIGHT_SHIFT_MIN = 0;

	public static int END_SHIFT_HOUR = 0;
	public static int END_SHIFT_MIN = 0;

	/**
	 * Message sent to the workplace that it is time to open
	 */
	public abstract void open();

	/**
	 * Message sent to the workplace that it is time to close
	 */
	public abstract void close();

	/**
	 * Find out who is in the store and determine if the store is open or not
	 * @return
	 */
	public abstract boolean isOpen();

	public abstract void notifyEmployeesTheyCanLeave();

	public void getReadyForWork(){
		synchronized(inhabitants){
			for(Role role : inhabitants){
				if(role instanceof Employee){
					Employee e = (Employee) role;

					if(e instanceof GenericWaiter){
						GenericWaiter gw = (GenericWaiter) e;
						//System.out.println(e.getWorkLocation());
						Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
						gw.setHost(rest.getHostRole());
						rest.getHostRole().addWaiter(gw);
						gw.setCashier(rest.getCashierRole());
						gw.setCook(rest.getCookRole());
						//return gw;
					}else if(e instanceof GenericHost){
						GenericHost gh = (GenericHost) e;
						Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
						//return gh;
					}else if(e instanceof GenericCook){
						GenericCook gc = (GenericCook) e;
						if(gc instanceof RyansCookRole){
							RyansCookRole rcr = (RyansCookRole) gc;
							Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(rcr.getWorkLocation());
							rcr.setCashier(rest.getCashierRole());
						}
						//return gc;
					}else if(e instanceof GenericCashier){
						GenericCashier gc = (GenericCashier) e;
						//return gc;
					}else if(e instanceof BankTeller){
						BankTellerRole btr = (BankTellerRole) e;
						Bank bank = (Bank) BuildingList.findBuildingWithName(e.getWorkLocation());
						btr.setAnnouncer(bank.getAnnouncer());

					}else if(e instanceof LoanTeller){
						LoanTellerRole ltr = (LoanTellerRole) e;
						Bank bank = (Bank) BuildingList.findBuildingWithName(e.getWorkLocation());
						ltr.setAnnouncer(bank.getLoanAnnouncer());

					}else{
						//return e;
					}
				}
			}

			synchronized(inhabitants){
				for(Role r : inhabitants){
					System.err.println("Activating: " + r.getNameOfRole() );
					r.getPerson().workIsOpen();
				}
			}
		}
	}

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		super.addRole(r);	
		System.err.println("Added "+r.getNameOfRole()+" to "+this.getName());
		if(isOpen() && !ready){
			ready = true;
			getReadyForWork();
		}
	}



}
