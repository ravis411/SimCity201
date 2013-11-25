package Person;

import gui.agentGuis.PersonGui;
import interfaces.Employee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import Person.Role.RoleFactory;
import agent.Agent;

/**
 * @author MSILKJR
 *
 */
public class PersonAgent extends Agent {
	
	private final double STARTING_MONEY = 100.00;
	private final int HUNGER_THRESHOLD = 50;

	private String name;
	private double money;
	private double moneyNeeded;
	
	private int age;
	
	private int SSN;
	private int atRestaurant;
	//private Residence home;
	private static int counter = 0;
	
	//party variables
	private List<Party> parties;
	
	
	private double loanAmount;
	
	public List<Role> roles;
	private List<PersonAgent> friends;
	private Calendar realTime;
	
	private Queue<Item> itemsNeeded;
	
	public enum StateOfHunger {NotHungry, SlightlyHungry, Hungry, VeryHungry, Starving} 
	public enum StateOfLocation {AtHome,AtBank,AtMarket,AtRestaurant, InCar,InBus,Walking};
	public enum StateOfEmployment {Customer,Employee,Idle};
	public enum PersonState {Idle,NeedsMoney,PayRentNow, PayLoanNow,GettingMoney,NeedsFood,GettingFood }
	
	private List<Item> backpack;
	
	public PersonState state;
	private StateOfEmployment stateOfEmployment;
	private Preferences prefs;
	//provides a hungerLevel on a normalized 0 to 100 scale
	private int hungerLevel;
	
	private PersonGui gui;

	public PersonAgent(String name){
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
		realTime = null;
		parties = new ArrayList<Party>();
		prefs = new Preferences();
		
		backpack = new ArrayList<Item>();
		itemsNeeded = new ArrayDeque<Item>();
	}
	
//-------------------------------MESSAGES----------------------------------------//
	
	/**
	  * Message sent by a vehicle, like a bus, to tell its passengers
	  * when it has arrived somewhere
	  * @param currentLocation the location the bus has arrived at
	  */
	public void msgWeHaveArrived(String currentDestination){
	  //unpause agent, which will be in transit (implementation not necessary here)
	}

	/**
	  * Message probably sent by an outside GUI to force hunger on the 
	  * person
	  */
	public void msgImHungry(){
	  state = PersonState.NeedsFood;
	}

	/**
	  * Message sent by any role where the Person needs money but doesn't have enough
	  * @param amountNeeded the amount of money the Person needs in addition to what he already has
	  */
	public void msgINeedMoney(double amountNeeded){
	  state = PersonState.NeedsMoney;
	  moneyNeeded += amountNeeded;
	}

	/**
	  * Message sent by a Bank employee to inform the person he has a loan
	  //---------THIS MESSAGE COULD BE SENT TO BANKCUSTOMER WHO JUST CHANGES HIS PERSON DATA----------//
	  * @param loan the amount of money in the loan
	  */
	public void msgYouHaveALoan(double loan){
	  loanAmount = loan;
	}

	/**
	  * Message sent by the TopAgent at a particular workplace 
	  */
	public void msgReportForWork(String role){
		for(Role r: roles){
			if(r.role==role){
				r.activate();
			}
		}
	}
	
	/**
	 * Message sent by the HomeRole for the person to go to the market
	 * @param item the name of the item needed from the market
	 */
	public void msgGoToMarket(String item){
		itemsNeeded.add(new Item(item, 1));
	}

	/**
	  * Message called, probably by a timer, which increases the person's
	  * amount of money by the amount he makes from work
	  */
	public void msgReceiveSalary(double amount){
	  money += amount;
	}

	/**
	  * Somehow if the person forgets to pay his loan, the Bank will remind him
	  * around the time of the due date
	  */
	public void msgPayBackLoanUrgently(){
	   state=PersonState.PayLoanNow;
	}

	/**
	  * Message sent by the building (or other mechanism see above) when the rent
	  * due date is close and needs paying.
	  */
	public void msgPayBackRentUrgently(){
	   state=PersonState.PayRentNow;
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
	public void msgPartyInvitation(PersonAgent p,Calendar rsvpDeadline,Calendar partyTime){
		Party party = new Party(p, rsvpDeadline, partyTime);
		party.partyState = PartyState.ReceivedInvite;
		parties.add(party);
		stateChanged();
	}
	
	/**
	  * RSVP message sent by the home role
	  */
	public void msgIAmComing(PersonAgent p){
		//findRole("HOME_ROLE").partyAttendees.add(p);
		//findRole("HOME_ROLE").rsvp.get(p)=true;
		
		
	}
	public void msgIAmNotComing(PersonAgent p){
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
			if(r.isActive()){
				outcome = r.pickAndExecuteAction() || outcome;
				
				if(outcome)
					return outcome;
			}
		}
		
		if(!itemsNeeded.isEmpty()){
			GoToMarketForItems();
			return true;
		}
		
		if(state != PersonState.Idle){
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
		  
		  Role role;
		  /*if(b instanceof Restaurant){
			  if(findRole(Role.RESTAURANT_CUSTOMER_ROLE) != null){
				  role = findRole(Role.RESTAURANT_CUSTOMER_ROLE);
		  	  }else{
				  role = RoleFactory.roleFromString(Role.RESTAURANT_CUSTOMER_ROLE);
				  addRole(role);
			  }
		  }else if(b instanceof Apartment || b instanceof Home){
		    role = findRole(Role.HOME_ROLE);
		  }

		  role.activate();*/
	}
	
	private String PickFoodLocation(){
		return "HOME";
	}

	private void GoGetMoney(){
		
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
		if(findRole(Role.BANK_CUSTOMER_ROLE) == null){
			Role r = RoleFactory.roleFromString(Role.BANK_CUSTOMER_ROLE);
			r.activate();
			addRole(r);
		}else{
			findRole(Role.BANK_CUSTOMER_ROLE).activate();
		}
		  /*state = GettingMoney;
		  Bank b = pickBank();
		  TransportationMode tm = pickTransportMode();
		  DoGoToBank(b, tm);
		  BankCustomerRole bcr = getBankCustomerRole();
		  bcr.activate();*/
	}
	
	private void GoToMarketForItems(){
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
		GoToLocation("Market", transport);
		if(findRole(Role.MARKET_CUSTOMER_ROLE) == null){
			Role r = RoleFactory.roleFromString(Role.MARKET_CUSTOMER_ROLE);
			r.activate();
			addRole(r);
		}else{
			findRole(Role.MARKET_CUSTOMER_ROLE).activate();
		}
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
				if(findRole(Role.PASSENGER_ROLE) == null){
					Role role = RoleFactory.roleFromString(Role.PASSENGER_ROLE);
					addRole(role);
					role.activate();
				}else{
					findRole(Role.PASSENGER_ROLE).activate();
				}
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
	  //state = PersonState.Idle;
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
		  
		  GoToLocation("Building 2", transport);
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
			return stateOfEmployment == StateOfEmployment.Employee && e.getShift().intersectsWithTime(realTime);
		}
		
		return false;
	}
	
	private boolean needsTransportation(){
		return true;
	}
	
	//--------------------------UTILITIES---------------------------//
	
	
	private Role findRole(String role){
		for(Role r : roles){
			if(r.getName().equals(role)){
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
		
		PersonAgent host;
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
		public Party(PersonAgent p, Calendar rsvpDeadline, Calendar partyTime){
			this.host=p;
			this.rsvpDeadline = rsvpDeadline;
			this.dateOfParty = partyTime;
		}
	}
	
	public void setGui(PersonGui gui){
		this.gui = gui;
	}

}
