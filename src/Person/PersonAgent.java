/**
 * 
 */
package Person;

import interfaces.Employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import Person.Role.Role;
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
	
	private double loanAmount;
	
	private List<Role> roles;
	private List<PersonAgent> friends;
	
	private Calendar realTime;
	//private Preferences personalPreferences;
	
	public enum StateOfHunger {NotHungry, SlightlyHungry, Hungry, VeryHungry, Starving} 
	public enum StateOfLocation {AtHome,AtBank,AtMarket,AtRestaurant, InCar,InBus,Walking};
	public enum StateOfEmployment {Customer,Employee,Idle};
	public enum PersonState {Idle,NeedsMoney,PayRentNow, PayLoanNow,GettingMoney,NeedsFood,GettingFood }
	
	private PersonState state;
	private StateOfEmployment stateOfEmployment;
	
	//provides a hungerLevel on a normalized 0 to 100 scale
	private int hungerLevel;

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
		
		realTime = null;
		
		setUpPreferences();
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

	//------------------------------SCHEDULER---------------------------//
	
	/**
	 * Scheduler
	 * @return true if rule fulfilled, false otherwise
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		//cue the Role schedulers
		boolean outcome = false;
		for(Role r: roles){
			if(r.isActive()){
				outcome = r.pickAndExecuteAction() || outcome;
			}
		}
		
		return outcome;
	}
	
	//----------------------------ACTIONS--------------------------//
	
	private void GoGetFood(){
		  /*state = GettingMoney;
		  Building b = PickFoodLocation();
		  TransportationMode tm = pickTransportMode();
		  DoGoToFoodLocation(b, tm);
		  Role role;
		  if(b instanceof Restaurant){
		    role = getRestaurantCustomerRole();
		  }else if(b instanceof Apartment || b instanceof Home){
		    role = getHomeRole();
		  }

		  role.activate();*/
	}

	private void GoGetMoney(){
		  /*state = GettingMoney;
		  Bank b = pickBank();
		  TransportationMode tm = pickTransportMode();
		  DoGoToBank(b, tm);
		  BankCustomerRole bcr = getBankCustomerRole();
		  bcr.activate();*/
	}

	private void PayBackRent(){
		  /*HomeRole hr= getHomeRole();
		  hr.msgPayRent(rentAmount);*/
	}

	private void GetOffTransportation(){
		  /*Transport t = myTransportation();
		  t.msgWeHaveArrived();*/
	}

	private void PayBackLoan(){
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
	
	
	private void setUpPreferences(){
		
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
		stateChanged();
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

}
