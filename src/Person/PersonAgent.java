package Person;

import gui.Building.ResidenceBuildingPanel;
import gui.agentGuis.PersonGui;
import interfaces.BusStop;
import interfaces.Person;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import kushrestaurant.CashierRole;
import kushrestaurant.interfaces.Waiter;
import residence.HomeRole;
import trace.AlertLog;
import trace.AlertTag;
import util.MasterTime;
import util.TimeListener;
import MarketEmployee.MarketEmployeeRole;
import MarketEmployee.MarketManagerRole;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.RoleFactory;
import Person.Role.ShiftTime;
import Transportation.BusStopConstruct;
import agent.Agent;
import bank.BankClientRole;
import building.Building;
import building.BuildingList;
import building.Restaurant;
import building.Workplace;
//import restaurant.NewWaiterRole;

/**
 * @author MSILKJR
 *
 */
public class PersonAgent extends Agent implements Person, TimeListener{
	
	private final double STARTING_MONEY = 100.00;
	private final int HUNGER_THRESHOLD = 50;

	private String name;
	private double money;
	private double moneyNeeded;
	
	private Semaphore onBus = new Semaphore(0, true);
	
	private int age = 20; //edited by Byron for testing purposes
	
	private int SSN;
	private int atRestaurant;
	//private Residence home;
	private static int counter = 0;
	
	//party variables
	private List<Party> parties;
	
	private double loanAmount;
	
	public List<Role> roles;
	public List<PersonAgent> friends;
	
	private Queue<Item> itemsNeeded;
	
	public enum StateOfHunger {NotHungry, SlightlyHungry, Hungry, VeryHungry, Starving} 
	public enum StateOfLocation {AtHome,AtBank,AtMarket,AtRestaurant, InCar,InBus,Walking};
	public enum StateOfEmployment {Customer,Employee,Idle};
	public enum PersonState {Idle,NeedsMoney,PayRentNow, Working, PayLoanNow,GettingMoney,NeedsFood,GettingFood }
	public enum WorkState {None, GoToWork, GoingToWork, AtWork}
	
	private List<Item> backpack;
	
	private ShiftTime currentShift;
	public PersonState state;
	public WorkState workState;
	private StateOfEmployment stateOfEmployment;
	private Preferences prefs;
	private int hungerLevel;
	
	private PersonGui gui;
	
	public ResidenceBuildingPanel home;
	
	public PersonAgent(String name, ResidenceBuildingPanel home, Role initialRole, String roleLocation){
		SSN = counter++;
		this.name = name;
		//initializations
		money = STARTING_MONEY;
		moneyNeeded = 0;
		loanAmount = 0;
		friends = new ArrayList<PersonAgent>();
		roles = new ArrayList<Role>();
		hungerLevel = 0;
		state=PersonState.GettingFood;
		parties = new ArrayList<Party>();
		prefs = new Preferences();
		this.home = home;
		
		backpack = new ArrayList<Item>();
		itemsNeeded = new ArrayDeque<Item>();
		
		MasterTime.getInstance().registerTimeListener(Workplace.DAY_SHIFT_HOUR, Workplace.DAY_SHIFT_MIN, false, this);
		MasterTime.getInstance().registerTimeListener(Workplace.NIGHT_SHIFT_HOUR, Workplace.NIGHT_SHIFT_MIN, false, this);
		MasterTime.getInstance().registerTimeListener(Workplace.END_SHIFT_HOUR, Workplace.END_SHIFT_MIN, false, this);
	}
	
	/**
	 * @precondition must be called after setGui
	 * @param r
	 * @param roleLocation
	 */
	public void setInitialRole(Role r, String roleLocation){
		if(r instanceof HomeRole){
			HomeRole hr = (HomeRole) findRole("HomeRole");
			//if(name.equals("Person 1"))
				hr.msgMakeFood();

			gui.setStartingStates(home.getName());
			BuildingList.findBuildingWithName(home.getName()).addRole(hr);
			hr.activate();
		}else{
			addRole(r);
				//System.err.println("AAAAAAAAAAAAAAAAAA");
			/*
			if(r instanceof GenericWaiter ){
				GenericWaiter w = (GenericWaiter) r;
				Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(roleLocation);
				GenericHost role = (GenericHost) rest.getHostRole();
				GenericCook cook = (GenericCook) rest.getCookRole();
				GenericCashier cashier = (GenericCashier) rest.getCashierRole();
				role.addWaiter(w);
				w.setHost(role);
				w.setCook(cook);
				w.setCashier(cashier);
			}*/
			if(r instanceof MarketManagerRole ){
				 MarketManagerRole role = (MarketManagerRole) findRole(Role.MARKET_MANAGER_ROLE);
			
			}
			if(r instanceof MarketEmployeeRole ){
				MarketEmployeeRole role = (MarketEmployeeRole) findRole(Role.MARKET_EMPLOYEE_ROLE);
			}
			
			gui.setStartingStates(roleLocation);
			BuildingList.findBuildingWithName(roleLocation).addRole(r);
			r.activate();
		}
	}

	public PersonAgent(String name, ResidenceBuildingPanel home){
		SSN = counter++;
		this.name = name;
		//initializations
		money = STARTING_MONEY;
		moneyNeeded = 0;
		loanAmount = 0;
		friends = new ArrayList<PersonAgent>();
		roles = new ArrayList<Role>();
		hungerLevel = 0;
		state=PersonState.GettingFood;
		parties = new ArrayList<Party>();
		prefs = new Preferences();
		this.home = home;
		
		backpack = new ArrayList<Item>();
		itemsNeeded = new ArrayDeque<Item>();
		
		roles.add(new HomeRole(this));
		
		if(name.equals("Person 1") || name.equals("Person 2") || name.equals("Person 12"))
			this.msgImHungry();
		if(name.equals("Person 10") || name.equals("Person 11"))
			this.msgINeedMoney(30.00);
		if(name.equals("Person 13")){
			this.msgGoToMarket("Steak");
		}
	}
	
//-------------------------------MESSAGES----------------------------------------//
	
	/**
	  * Message sent by a vehicle, like a bus, to tell its passengers
	  * when it has arrived somewhere
	  * @param currentLocation the location the bus has arrived at
	  */
	public void msgWeHaveArrived(String currentDestination){
	  //unpause agent, which will be in transit (implementation not necessary here)
		onBus.release();
		AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "Arrived at Destination!!");
	}

	/**
	  * Message probably sent by an outside GUI to force hunger on the 
	  * person
	  */
	public void msgImHungry(){
	  state = PersonState.NeedsFood;
	  stateChanged();
	}

	/**
	  * Message sent by any role where the Person needs money but doesn't have enough
	  * @param amountNeeded the amount of money the Person needs in addition to what he already has
	  */
	public void msgINeedMoney(double amountNeeded){
	  state = PersonState.NeedsMoney;
	  moneyNeeded += amountNeeded;
	  stateChanged();
	}

	/**
	  * Message sent by a Bank employee to inform the person he has a loan
	  //---------THIS MESSAGE COULD BE SENT TO BANKCUSTOMER WHO JUST CHANGES HIS PERSON DATA----------//
	  * @param loan the amount of money in the loan
	  */
	public void msgYouHaveALoan(double loan){
	  loanAmount = loan;
	  stateChanged();
	}

	/**
	  * Message sent to the person by a timer listener to report for
	  * work. Works sort of like an alarm clock.
	  */
	public void msgReportForWork(){
		if(getCurrentJob() == null)
			return;
		else
			workState = WorkState.GoToWork;
		
		stateChanged();
	}
	
	/**
	 * Message sent to the Person from a Workplace to leave
	 */
	public void msgYouCanLeave(){
		workState = WorkState.None;
		stateChanged();
	}
	
	/**
	 * Message sent by the HomeRole for the person to go to the market
	 * @param item the name of the item needed from the market
	 */
	public void msgGoToMarket(String item){
		itemsNeeded.add(new Item(item, 1));
		stateChanged();
	}

	/**
	  * Message called, probably by a timer, which increases the person's
	  * amount of money by the amount he makes from work
	  */
	public void msgReceiveSalary(double amount){
	  money += amount;
	  stateChanged();
	}

	/**
	  * Somehow if the person forgets to pay his loan, the Bank will remind him
	  * around the time of the due date
	  */
	public void msgPayBackLoanUrgently(){
	   state=PersonState.PayLoanNow;
	   stateChanged();
	}

	/**
	  * Message sent by the building (or other mechanism see above) when the rent
	  * due date is close and needs paying.
	  */
	public void msgPayBackRentUrgently(){
	   state=PersonState.PayRentNow;
	   stateChanged();
	}
	
	
	public void msgAddObjectToBackpack(String object, int quantity){
		boolean added = false;
		for(Item bo : backpack){
			if(bo.name.equals(object)){
				bo.quantity += quantity;
				added = true;
				print("Added "+ quantity +" "+ object+ " to backpack. Quantity now: "+bo.quantity);
				break;
			}
		}
		
		if(!added){
			backpack.add(new Item(object, quantity));
			print("Added "+ quantity +" "+ object+ " to backpack. Quantity now: "+quantity);

		}
		
		stateChanged();
	}
	
	/**
	  * Message sent by the home role to invite the person to a party
	  */
	public void msgPartyInvitation(Person p,Calendar rsvpDeadline,Calendar partyTime){
		Party party = new Party(p, rsvpDeadline, partyTime);
		party.partyState = PartyState.ReceivedInvite;
		parties.add(party);
		stateChanged();
	}
	
	/**
	  * RSVP message sent by the home role
	  */
	public void msgIAmComing(Person p){
		//findRole("HOME_ROLE").partyAttendees.add(p);
		//findRole("HOME_ROLE").rsvp.get(p)=true;
		
		
	}
	public void msgIAmNotComing(Person p){
		//findRole("HOME_ROLE").rsvp.get(p)=true;
	}

	//------------------------------SCHEDULER---------------------------//
	
	/**
	 * Scheduler
	 * @return true if rule fulfilled, false otherwise
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stu

		//cue the Role schedulers
		boolean outcome = false;
		for(Role r: roles){
			boolean somethingIsActive = false;
			if(r.isActive()){
				somethingIsActive = true;
				outcome = r.pickAndExecuteAction() || outcome;
				if(outcome)
					return outcome;
			}
			
			if(somethingIsActive)
				return false;
		}
		
		if(workState == WorkState.GoToWork){
			GoToWork();
			return true;
		}
		
		if(state == PersonState.NeedsFood){
			GoGetFood();
			return true;
		}
		
		if(!itemsNeeded.isEmpty()){
			GoToMarketForItems();
			return true;
		}
		
		if(state == PersonState.NeedsMoney && moneyNeeded > 10){
			GoGetMoney();
			return true;
		}
		

		if(state != PersonState.Idle){
			AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "////////////IDLE//////////");
			GoHome();
		}
		
		return false;
	}
	
	//----------------------------ACTIONS--------------------------//
	
	private void GoGetFood(){
		  state = PersonState.GettingFood;
		  //Building b = PickFoodLocation();
		  String transport;
		  switch(prefs.get(Preferences.KeyValue.VEHICLE_PREFERENCE)){
		  	case Preferences.BUS:
		  		transport = Preferences.BUS;
		  		break;
		  	case Preferences.CAR:
		  		transport = Preferences.CAR;
		  		break;
		  	case Preferences.WALK:
		  		transport = Preferences.WALK;
		  		break;
		  		
		  	default:
		  		transport = "ERROR";
		  }
		  
		  String location = PickFoodLocation();
		  GoToLocation(location, transport);
		  
		  GenericCustomer role = (GenericCustomer) findRole(Role.RESTAURANT_KUSH_CUSTOMER_ROLE);
		  if(role == null){
			  role = (GenericCustomer) RoleFactory.roleFromString(Role.RESTAURANT_KUSH_CUSTOMER_ROLE);
			  addRole(role);
		  }

		  AlertLog.getInstance().logMessage(AlertTag.PERSON, "Person", "Customer Role = "+role);
		  BuildingList.findBuildingWithName("Kush's Restaurant").addRole(role);
		  Building bdg =  BuildingList.findBuildingWithName("Kush's Restaurant");
		  if(bdg instanceof Restaurant){
			  Restaurant rest = (Restaurant) bdg;
			  role.setupCustomer("Kush's Restaurant");
//			  role.setCashier(rest.getCashierRole());
//			  role.setHost(rest.getHostRole());
			  
			  role.gotHungry();
			  role.activate();
		  }
	}
	
	private String PickFoodLocation(){
		return home.getName();
	}
	
	private void GoToWork(){
		if(getCurrentJob() == null)
			return;
		
		String workLocation = getCurrentJob().getWorkLocation();
		GoToLocation(workLocation, getTransportPreference());
		Role r = getCurrentJob();
		if(r instanceof GenericHost){
			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
			addRole(rest.getHostRole());
		}else if(r instanceof GenericCook){
			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
			addRole(rest.getCookRole());
		}else if(r instanceof GenericCashier){
			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
			addRole(rest.getCashierRole());
		}else{
			BuildingList.findBuildingWithName(workLocation).addRole(r);
			addRole(getCurrentJob());
		}
		
		if(!r.isActive())
			r.activate();
	}
	
	private String getTransportPreference(){
		String transport;
		switch(prefs.get(Preferences.KeyValue.VEHICLE_PREFERENCE)){
		  	case Preferences.BUS:
		  		transport = Preferences.BUS;
		  		break;
		  	case Preferences.CAR:
		  		transport = Preferences.CAR;
		  		break;
		  	case Preferences.WALK:
		  		transport = Preferences.WALK;
		  		break;
		  		
		  	default:
		  		transport = "ERROR";
		}
		
		return transport;
	}

	private void GoGetMoney(){
		
		
		
		//needs a way to find a bank quite yet
		GoToLocation("Bank", getTransportPreference());
		Role r = findRole(Role.BANK_CLIENT_ROLE);
		if(r == null){
			r = RoleFactory.roleFromString(Role.BANK_CLIENT_ROLE);
			addRole(r);
		}
		
		BankClientRole role = (BankClientRole) r;
		role.setIntent(BankClientRole.withdraw);
		BuildingList.findBuildingWithName("Bank").addRole(role);
		role.activate();
	}
	
	private void GoToMarketForItems(){
		AlertLog.getInstance().logMessage(AlertTag.PERSON, "Person", "GOING TO MARKET FOR ITEMS");
		String transport;
		switch(prefs.get(Preferences.KeyValue.VEHICLE_PREFERENCE)){
		  	case Preferences.BUS:
		  		transport = Preferences.BUS;
		  		break;
		  	case Preferences.CAR:
		  		transport = Preferences.CAR;
		  		break;
		  	case Preferences.WALK:
		  		transport = Preferences.WALK;
		  		break;
		  		
		  	default:
		  		transport = "ERROR";
		}
		
		//needs a way to find a bank quite yet
		 GoToLocation("Market 1", transport);
		 Role r = findRole(Role.MARKET_CUSTOMER_ROLE);
		if(r == null){
			r = RoleFactory.roleFromString(Role.MARKET_CUSTOMER_ROLE);
			addRole(r);
			r.activate();
		}else{
			r.activate();
		}
		
		BuildingList.findBuildingWithName("Market 1").addRole(r);
	}

	/**
	 * @pre Assume that if we are paying back a loan we have a bank role
	 */
	private void PayBackLoan(){
			
		String transport;
		switch(prefs.get(Preferences.KeyValue.VEHICLE_PREFERENCE)){
		  	case Preferences.BUS:
		  		transport = Preferences.BUS;
		  		break;
		  	case Preferences.CAR:
		  		transport = Preferences.CAR;
		  		break;
		  	case Preferences.WALK:
		  		transport = Preferences.WALK;
		  		break;
		  		
		  	default:
		  		transport = "ERROR";
		}
		
		//needs a way to find a bank quite yet
		GoToLocation("Bank", transport);
		
		if(money >= loanAmount){

		    //--------------------NEEDS MSG FOR ENTERING BANK WITH THE INTENT TO PAY LOAN-----------------------//
		   /* BankCustomerRole bcr = (BankCustomeRole) findRole(Role.BANK_CUSTOMER_ROLE);
		    bcr.msgPayLoan(loanAmount);*/
		}else{
		    //--------------------NEEDS MSG FOR WITHDRAWING FROM BANK------------------------------------------//
		   /* BankCustomerRole bcr = (BankCustomerRole) findRole(Role.BANK_CUSTOMER_ROLE);
		    bcr.msgWithdrawMoney();
		    bcr.msgPayLoan(loanAmount);*/
		}
		
		  /*DoGoToBank();
		  if(money >= loanAmount){

		    //--------------------NEEDS MSG FOR ENTERING BANK WITH THE INTENT TO PAY LOAN-----------------------//
		    BankCustomerRole bcr = getBankCustomerRole();
		    bcr.msgPayLoan(loanAmount);
		  else{
		    //--------------------NEEDS MSG FOR WITHDRAWING FROM BANK------------------------------------------//
		    BankCustomerRole bcr = getBankCustomerRole();
		    bcr.msgWithdrawMoney();
		    bcr.msgPayLoan(loanAmount);
		  }*/
	}
	
	private void GoToLocation(String location, String modeOfTransportation){
		AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "Going to "+location+" via + "+modeOfTransportation);
		switch(modeOfTransportation){
			case Preferences.BUS:
				String startStop = gui.DoGoToClosestBusStop();
				String destStop = gui.DoRideBusTo(location);
				BusStop startAgent = BusStopConstruct.stops.get(startStop);
				startAgent.msgAtBusStop(this, destStop);
				try{
					onBus.acquire();
				}catch(Exception e){
					e.printStackTrace();
				}
				GoToLocation(location, "WALK");
				break;
			case Preferences.CAR:
				break;
			case Preferences.WALK:
				System.err.println("Trying to walk to "+location);
				gui.DoGoTo(location);
				break;
		}
	}
	
	private void GoHome(){
		String transport;
	  state = PersonState.Idle;
	  switch(prefs.get(Preferences.KeyValue.VEHICLE_PREFERENCE)){
	  	case Preferences.BUS:
	  		transport = Preferences.BUS;
	  		break;
	  	case Preferences.CAR:
	  		transport = Preferences.CAR;
	  		break;
	  	case Preferences.WALK:
	  		transport = Preferences.WALK;
	  		break;
	  		
	  	default:
	  		transport = "ERROR";
	  }
 
	  GoToLocation(home.getName(), transport);
	  HomeRole role = (HomeRole) findRole("HomeRole");
	  BuildingList.findBuildingWithName(home.getName()).addRole(role);
	  role.activate();
	  
	  role.msgMakeFood();

	}
		  
	//------------------------DO XYZ FUNCTIONS----------------------//
		  
	//------------------------SCRIPTING STUBS-----------------------//
	
	private StateOfHunger howHungry(){
		
		if(hungerLevel < 20)
			return StateOfHunger.NotHungry;
		
		if(hungerLevel < 40)
			return StateOfHunger.SlightlyHungry;
		
		if(hungerLevel < 60)
			return StateOfHunger.Hungry;
		
		if(hungerLevel < 80)
			return StateOfHunger.VeryHungry;
		
		return StateOfHunger.Starving;
	}
	
	private boolean canGoGetFood(){
		//--------------COME UP WITH SPECIFIC STATES WHERE WE CANNOT GO GET A ROLE-------------------//
		if(state != PersonState.Idle)
			return true;
		else
			return false;
	}
	
	private boolean canGoOnBreak(){
		Role r = findMyJob();
		if(r == null)
			return true;
		
		return true;
	}
	
	private boolean needsToBeAtWork(){
		Role r = findMyJob();
		if(r != null){
			Employee e = (Employee) r;
			return true;
			//return stateOfEmployment == StateOfEmployment.Employee && e.getShift().intersectsWithTime(realTime);
		}
		
		return false;
	}
	
	private boolean needsTransportation(){
		return true;
	}
	
	//--------------------------UTILITIES---------------------------//
	
	
	private Role findRole(String role){
		for(Role r : roles){
			if(r.getNameOfRole().equals(role)){
				return r;
			}
		}
		
		return null;
	}
	
	private Role findMyJob(){
		for(Role r : roles){
			if(r.isActive() && r instanceof Employee){
				return r;
			}
		}
		
		return null;
	}
	
	/**
	 * Getter function for name
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter function for money
	 * @return money
	 */
	public double getMoney(){
		return money;
	}
	
	/**
	 * Getter function for age
	 * @return age
	 */
	public int getAge(){
		return age;
	}
	
	/**
	 * Getter function for SSN
	 * @return SSN
	 */
	public int getSSN(){
		return SSN;
	}
	/**
	 * Getter function for moneyNeeded
	 * @return moneyNeeded
	 */
	public double getMoneyNeeded(){
		return moneyNeeded;
	}
	
	/**
	 * Getter function for debts of person
	 * @return debts
	 */
	public double getLoan(){
		return loanAmount;
	}
	/**
	 * Setter for the money
	 */
	public void setMoney(double money){
		this.money=money;
	}
	
	public void setMoneyNeeded(double money){
		this.moneyNeeded = money;
	}
	
	public int getNumParties(){
		return parties.size();
	}
	
	/**
	 * Adds a loan/debt to the list of debts
	 */
	public void setLoan(double loan){
		this.loanAmount = loan;
	}

	/**
	 * A public stateChanged method to allow the Roles to call stateChanged
	 */
	public void stateChanged(){
		super.stateChanged();
	}
	
	/**
	 * Adds a role to the Person
	 * @param r the role to add
	 */
	public void addRole(Role r){
		if(r instanceof Employee){
			state = PersonState.Working;
		}
		
		if(roles.contains(r)){
			r.setPerson(this);
			return;
		}
		
		r.setPerson(this);
		roles.add(r);
	}
	
	/**
	 * Removes a specified role from the Person
	 * @param r the role to remove
	 */
	public void removeRole(Role r){
		roles.remove(r);
	}
	
	public Queue<Item> getItemsNeeded(){
		return itemsNeeded;
	}
	
	/**
	 * Class meant to simulate an Item the person either needs or has on hand.
	 * (Essentially a struct for name and quantity)
	 */
	private class Item {
		String name;
		int quantity;
		
		public Item(String name, int quantity){
			this.name = name;
			this.quantity = quantity;
		}
	}
	
	/**
	 * A class meant to simulate a friend.
	 * (Essentially a struct which links a person to how good of a friend one is)
	 */
	private class Friend {
		PersonAgent person;
		boolean goodFriend;
		
		public Friend(PersonAgent person, boolean goodFriend){
			this.person = person;
			this.goodFriend = goodFriend;
		}
	}
	
	enum PartyState {Host, ReceivedInvite, NeedsResponseUrgently, RSVPed, GoingToParty, NotGoingToParty}
	
	/**
	 * A class meant to simulate a Party
	 *
	 */
	private class Party{
		
		Person host;
		Calendar rsvpDeadline;
		Calendar dateOfParty;
		
		PartyState partyState;
		
		/**
		 * Party state should be set upon initialization accordingly whether or not 
		 * the person has received an invite or is the host
		 * @param p the personAgent hosting the party
		 * @param rsvpDeadline the RSVP deadline
		 * @param partyTime the date of the party
		 */
		public Party(Person p, Calendar rsvpDeadline, Calendar partyTime){
			this.host=p;
			this.rsvpDeadline = rsvpDeadline;
			this.dateOfParty = partyTime;
		}
	}
	
	public void setGui(PersonGui gui){
		this.gui = gui;
	}
	public PersonGui getPersonGui(){
		return this.gui;
	}
	
	public String toString(){
		return getName();
	}

	public List<PersonAgent> getFriends() {
		return friends;
	}
	public ResidenceBuildingPanel getHome(){
		return home;
	}
	
	private Employee getCurrentJob(){
		for(Role r : roles){
			if(r instanceof Employee){
				Employee e = (Employee) r;
				if(e.getShift() == currentShift){
					return (Employee) r;
				}
			}
		}
		
		return null;
	}

	@Override
	public void timeAction(int hour, int minute) {
		// TODO Auto-generated method stub
		/*if(hour == Workplace.DAY_SHIFT_HOUR && minute == Workplace.DAY_SHIFT_MIN){
			currentShift = ShiftTime.DAY_SHIFT;
			if(getCurrentJob().getShift() == ShiftTime.DAY_SHIFT){
				msgReportForWork();
			}
		}else if(hour == Workplace.NIGHT_SHIFT_HOUR && minute == Workplace.NIGHT_SHIFT_MIN){
			currentShift = ShiftTime.NIGHT_SHIFT;
		}else if(hour == Workplace.END_SHIFT_HOUR && minute == Workplace.END_SHIFT_MIN){
			currentShift = ShiftTime.NONE;
		}*/
	}
}
