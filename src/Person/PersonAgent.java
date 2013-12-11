package Person;

import gui.BuildingsPanels;
import gui.Building.ResidenceBuildingPanel;
import gui.agentGuis.PersonGui;
import interfaces.BusStop;
import interfaces.Person;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import residence.HomeGuestRole;
import residence.HomeRole;
import trace.AlertLog;
import trace.AlertTag;
import util.DateListener;
import util.MasterTime;
import util.TimeListener;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.RoleFactory;
import Person.Role.RoleState;
import Person.Role.ShiftTime;
import Transportation.BusStopConstruct;
import Transportation.CarAgent;
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
public class PersonAgent extends Agent implements Person, TimeListener, DateListener{
        
        private final double STARTING_MONEY = 100.00;
        private final int HUNGER_THRESHOLD = 50;
        
        private static double SALARY = 50.00;

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
	
	private Queue<Employee> pendingJobs = new ArrayDeque<Employee>();
	
	private double loanAmount;
	
	public List<MyRole> roles;
	public List<Person> friends;
	
	private CarAgent myCar;
	
	private Queue<Item> itemsNeeded;
	
	public enum StateOfHunger {NotHungry, SlightlyHungry, Hungry, VeryHungry, Starving} 
	public enum StateOfLocation {AtHome,AtBank,AtMarket,AtRestaurant, InCar,InBus,Walking};
	public enum StateOfEmployment {Customer,WaitingAtWork, Employee,Idle};
	public enum PersonState {Idle,NeedsMoney,PayRentNow, Working, PayLoanNow,GettingMoney,NeedsFood,GettingFood,HostParty,GoingToParty,HostingParty,Partying, GoHome}
	public enum WorkState {None, GoToWork, GoingToWork, AtWork}

	private static int GO_HOME_HOUR = 1;
	private static int GO_HOME_MINUTE = 0;
	
	private List<Item> backpack;
	private boolean plsRob=false;
	private ShiftTime currentShift;
	public PersonState state;
	public StateOfLocation stateOfLocation;
	public WorkState workState;
	private StateOfEmployment stateOfEmployment;
	private Preferences prefs;
	private int hungerLevel;
	private Calendar rentDueDate = null;
	
	private PersonGui gui;
	
	public ResidenceBuildingPanel home;
	
	public static class MyRole{
		Role role;
		public MyRole(Role r){
			this.role = r;
		}
	}
	
	public static class MyJob extends MyRole{

		ShiftTime shift;
		public MyJob(Role r, ShiftTime shift) {
			super(r);
			this.shift = shift;
			// TODO Auto-generated constructor stub
		}
		
	}
	
//	public PersonAgent(String name, ResidenceBuildingPanel home, String roleLocation){
//		SSN = counter++;
//		this.name = name;
//		//initializations
//		money = STARTING_MONEY;
//		moneyNeeded = 0;
//		loanAmount = 0;
//		friends = new ArrayList<PersonAgent>();
//		roles = new ArrayList<Role>();
//		hungerLevel = 0;
//		state=PersonState.GettingFood;
//		parties = new ArrayList<Party>();
//		prefs = new Preferences();
//		this.home = home;
//		
//		this.myCar = new CarAgent(this, name+" car");
//		
//		backpack = new ArrayList<Item>();
//		itemsNeeded = new ArrayDeque<Item>();
//		
//		MasterTime.getInstance().registerTimeListener(Workplace.DAY_SHIFT_HOUR, Workplace.DAY_SHIFT_MIN, false, this);
//		MasterTime.getInstance().registerTimeListener(Workplace.NIGHT_SHIFT_HOUR, Workplace.NIGHT_SHIFT_MIN, false, this);
//		MasterTime.getInstance().registerTimeListener(Workplace.END_SHIFT_HOUR, Workplace.END_SHIFT_MIN, false, this);
//	
//		//Add the gui
//		setGui(new PersonGui(this));
//	}
	
	private Semaphore waitingAtWork = new Semaphore(0, true);
	
	public void workIsOpen(){
		waitingAtWork.release();
	}
	
	/**
	 * @precondition must be called after setGui
	 * @param r
	 * @param roleLocation
	 */
	public void setInitialRole(Role r, String roleLocation, ShiftTime shift){
		//if(r instanceof HomeRole || r == null){
		
			//always start them at the house
			HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;
			//if(name.equals("Person 1"))
			//hr.msgMakeFood();
				
			gui.setStartingStates(home.getName());
			BuildingList.findBuildingWithName(home.getName()).addRole(hr);
			hr.activate();
//		}
			
//			if( !(r == null) || shift == ShiftTime.NIGHT_SHIFT) {
//				Random rand = new Random();
//				switch(Math.abs(rand.nextInt() % 6)){
//				case 0:
//					this.msgGoToMarket("Chicken");
//					System.err.println("MARKET");
//					break;
//				case 1:
//					this.msgINeedMoney(40.00);
//					System.err.println("NEEDS MONEY");
//					break;
//				case 2:
//				case 3:
//				case 4:
//				case 5:
//					System.err.println("HUNGRY");
//					this.msgImHungry();
//					break;
//				}
//			}
			

//			if(r instanceof MarketManagerRole ){
//				 MarketManagerRole role = (MarketManagerRole) findRole(Role.MARKET_MANAGER_ROLE);
//				 return;
//			}
//			if(r instanceof MarketEmployeeRole ){
//				MarketEmployeeRole role = (MarketEmployeeRole) findRole(Role.MARKET_EMPLOYEE_ROLE);
//				return;
//			}
			
			//but if the role we passed in was an employee, we have to make sure we add it to the role list
			if(r instanceof Employee){
				Employee e = (Employee) r;
				//if the role is a shared role, make sure we are adding the same one and not a repeat
				System.out.println(e.getWorkLocation()+" "+getName());
				if(r instanceof GenericHost){
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					//if the hostrole exists, add that role
					if(rest.getHostRole() != null){
						MyRole a = new MyJob(rest.getHostRole(), shift);
						addRole(a);
					}else //otherwise add the role that was passed in -- a new one
						addRole(new MyJob(r, shift));
				 }else if(r instanceof GenericCook){
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					if(rest.getCookRole() != null){
						MyRole a = new MyJob(rest.getCookRole(), shift);
						addRole(a);
					}else
						addRole(new MyJob(r, shift));
				 }else if(r instanceof GenericCashier){
					Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(e.getWorkLocation());
					if(rest.getCashierRole() != null){
						MyRole a = new MyJob(rest.getCashierRole(), shift);
						addRole(a);
					}else
						addRole(new MyJob(r, shift));
				 }else{
					 //if it isn't a shared role just add it
					 addRole(new MyJob(r, shift));
				}

		}
	}
	
	
	public void waitForWork(Role r){
//		BuildingList.findBuildingWithName(em.getWorkLocation()).addRole(em);
		try {
			waitingAtWork.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workState = WorkState.AtWork;
		r.activate();
	}

	public PersonAgent(String name, ResidenceBuildingPanel home){
		SSN = counter++;
		this.name = name;
		//initializations
		money = STARTING_MONEY;
		moneyNeeded = 0;
		loanAmount = 0;
		friends = new ArrayList<Person>();
		roles = new ArrayList<MyRole>();
		hungerLevel = 0;
		state=PersonState.Idle;
		parties = new ArrayList<Party>();
		prefs = new Preferences();
		this.home = home;
		
		if(home != null) {
			this.myCar = Math.random() > 0.5 ? new CarAgent(this, name+" car", home.getName()) : null;
		}
		
		backpack = new ArrayList<Item>();
		itemsNeeded = new ArrayDeque<Item>();
		stateOfLocation = StateOfLocation.Walking;
		roles.add(new MyRole(new HomeRole(this)));
		
		this.workState = WorkState.None;

                rentDueDate = Calendar.getInstance();
                rentDueDate.set(MasterTime.getInstance().get(Calendar.YEAR), MasterTime.getInstance().get(Calendar.MONTH), MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+1, 0, MasterTime.getInstance().get(Calendar.MINUTE), MasterTime.getInstance().get(Calendar.SECOND));
                MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+1), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);
                MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+8), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);
                MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+15), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);
                MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+22), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);
                MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+29), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);

//                if(name.equals("Person 1") || name.equals("Person 2") )
//                        this.msgImHungry();
//                
//                if())
//                
//                if(name.equals("Person 10") || name.equals("Person 11") || name.equals("Person 12"))
//                        this.msgINeedMoney(30.00);
//                if(name.equals("Person 13")){
//                        this.msgGoToMarket("Steak");
//                }
                
                MasterTime.getInstance().registerTimeListener(Workplace.DAY_SHIFT_HOUR, Workplace.DAY_SHIFT_MIN, false, this);
                MasterTime.getInstance().registerTimeListener(Workplace.NIGHT_SHIFT_HOUR, Workplace.NIGHT_SHIFT_MIN, false, this);
                MasterTime.getInstance().registerTimeListener(Workplace.END_SHIFT_HOUR, Workplace.END_SHIFT_MIN, false, this);
                //MasterTime.getInstance().registerTimeListener(GO_HOME_HOUR, GO_HOME_MINUTE, true, this);
                //Add the gui
                //setGui(new PersonGui(this));
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
                //------------------ILLEGAL CHANGE OR DELETE---------------------------------//
                if(stateOfLocation == StateOfLocation.InCar){
                        myCar.msgLeavingCar();
                        stateOfLocation = StateOfLocation.Walking;
                }
                AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "Arrived at Destination!!");
        }
        
        public void msgGoHome(){
        	state = PersonState.GoHome;
        	stateChanged();
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
                else{
                		state = PersonState.Working;
                        workState = WorkState.GoToWork;
                        deactivateCurrentRole();
                }
                
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
		AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "Received a party invitation! Party's at " + partyTime.get(Calendar.HOUR_OF_DAY) + ":" + partyTime.get(Calendar.MINUTE) + " tomorrow.");
		Party party = new Party(p, rsvpDeadline, partyTime);
		party.partyState = PartyState.ReceivedInvite;
		parties.add(party);
		stateChanged();
	}
	
	/**
	  * RSVP message sent by the home role
	  */
	public void msgIAmComing(Person p){
		AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), p.getName() + " is coming to the party.");
		HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;

		if(hr != null) {
			hr.partyAttendees.add((PersonAgent) p);
			//hr.rsvp.get(p)=true;
			hr.partyInvitees.remove((PersonAgent) p);
		}
		
	}
	public void msgIAmNotComing(Person p){
		AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), p.getName() + " is not coming to the party.");
		//findRole("HOME_ROLE").rsvp.get(p)=true;
		HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;
		if(hr != null) {
			hr.partyInvitees.remove((PersonAgent) p);
		}
		if(hr.partyInvitees.size()==0 && hr.partyAttendees.size()==0){
			AlertLog.getInstance().logMessage(AlertTag.PERSON, getName(), "Party is cancelled since no one is attending.");
		}
	}
	public void msgRespondToInviteUrgently(Person host){
		for(Party p : parties) {
			if(p.host.getName() == host.getName()) {
				p.partyState = PartyState.NeedsResponseUrgently;
			}
		}
		stateChanged();
	}
	public void msgPartyOver(Person host) {
		for(Party p : parties) {
			if(p.host.getName() == host.getName()) {
				//parties.remove(p);
			}
		}
		MyRole role = findRole(Role.HOME_GUEST_ROLE);
		HomeGuestRole hgr = (HomeGuestRole) role.role;
		hgr.msgPartyOver();
	}

	
	//------------------------------SCHEDULER---------------------------//
	
	/**
	 * Scheduler
	 * @return true if rule fulfilled, false otherwise
	 */
	@Override
	public boolean pickAndExecuteAnAction() {

		if(this.name.equals("robber") && !plsRob){
			GoRobBank();
			plsRob=true;
			return true;
		}

		//cue the Role schedulers
		boolean outcome = false;
			for(MyRole r: roles){
				boolean somethingIsActive = false;
				if(r.role.roleState == RoleState.Deactivating){
					somethingIsActive = true;
					
					outcome = r.role.pickAndExecuteAction() || outcome;
					if(outcome)
						return outcome;
				}
				
				if(somethingIsActive)
					return false;
		}
		
		if(stateOfLocation != StateOfLocation.AtHome && state == PersonState.GoHome){
			GoHome();
			return true;
		}
		
		if(!pendingJobs.isEmpty()){
			waitForWork(pendingJobs.poll());
			return true;
		}
		
		if(parties.size()!=0){
			for(Party p:parties){
				if(p.partyState==PartyState.NeedsResponseUrgently){
					for(Person pa:friends){
						if(pa==p.getHost()){
							rsvpYes(pa,p);	
						}
						else{
							rsvpNo(pa,p);
						}   
					}
				}
			}
		}
		
		if(parties.size()!=0){
			for(Party p:parties){
				if(p.partyState==PartyState.ReceivedInvite){
					for(Person pa :friends){
						if(pa==p.getHost()){
							int i= new Random().nextInt(40);
							if(i%2==0){
								rsvpYes(pa,p);
								return true;
							}
							else{ 
								p.partyState=PartyState.notRSVPed;
							}
						}
					}
					int i= new Random().nextInt(40);
					if(p.partyState==PartyState.ReceivedInvite){
						if(i%2==0){
							rsvpNo((PersonAgent) p.host,p);
						}
						else{
							p.partyState=PartyState.notRSVPed;
						}
					}
				}
			}
		}
		
		if(state == PersonState.GoingToParty) {
			GoToParty(parties.get(0).getHost().getHome().getName());
			return true;
		}
		if(state == PersonState.HostParty) {
			boolean atHome = false;
			for(MyRole r : roles){
				if(r.role.getNameOfRole().equals(Role.HOME_ROLE) && r.role.isActive()){
					atHome = true;
					break;
				}
			}
			if(!atHome){
				GoHome();
			}
			HostParty();
			return true;
		}
		
		if(workState == WorkState.GoToWork){
			GoToWork();
			return true;
		}

		//cue the Role schedulers
		outcome = false;
		for(MyRole r: roles){
			if(r.role.getPerson() != this)
				continue;
			
			boolean somethingIsActive = false;
			if(r.role.isActive()){
				somethingIsActive = true;
				
//				if(workState == WorkState.AtWork){
//					Employee e = (Employee) r.role;
//					if(getCurrentJob().role != e)
//						continue;
//				}
				
				
				outcome = r.role.pickAndExecuteAction() || outcome;
				if(outcome)
					return outcome;
			}
			
			if(somethingIsActive)
				return false;
		}
		
		if(state == PersonState.NeedsFood){
			GoGetFood();
			return true;
		}
		
		if(!itemsNeeded.isEmpty()){
			GoToMarketForItems();
			return true;
		}
		
		if(state == PersonState.NeedsMoney && moneyNeeded > 100000){
			GoRobBank();
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
		  String transport = getTransportPreference();
		  this.state = PersonState.GettingFood;
		  String location = PickFoodLocation();
		  //String location= "Kush's Restaurant";

		  GoToLocation(location, transport);
		  if(location.equals(home.getName())){
			  //go home for food
			  GoHome();
			  HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;
			  hr.msgMakeFood();
		  }else{
			  String roleString = Restaurant.getStringForCustomer(location);
			  MyRole role = findRole(roleString);
			  if(role == null){
				  role = new MyRole(RoleFactory.roleFromString(roleString));
				  addRole(role);
			  }
			  GenericCustomer cust = (GenericCustomer) role.role;
			  AlertLog.getInstance().logMessage(AlertTag.PERSON, "Person", "Customer Role = "+role);


			  Restaurant resta =  (Restaurant) BuildingList.findBuildingWithName(location);
			  BuildingList.findBuildingWithName(location).addRole(role.role);
			  Building bdg =  BuildingList.findBuildingWithName(location);


			  if(bdg instanceof Restaurant){
				  Restaurant rest = (Restaurant) bdg;
				  try {
					waitingAtWork.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				  cust.setupCustomer(location);



				  cust.gotHungry();
				  cust.activate();
			  }
		  }
		  hungerLevel = 0;
		  this.msgGoToMarket("item");
	}
	
	private String PickFoodLocation(){
		List<Building> buildings = BuildingList.findBuildingsWithType(BuildingList.RESTAURANT);
		buildings.add(BuildingList.findBuildingWithName(home.getName()));
		Random r = new Random();
		//return "Mike's Restaurant";
		return buildings.get(Math.abs(r.nextInt()) % buildings.size()).getName();
		
        //return Math.random() > 0.5 ? "Food Court" : this.home.getName();
    }
	
	private String PickBankLocation(){
		List<Building> buildings = BuildingList.findBuildingsWithType(BuildingList.BANK);
		Random r = new Random();
		//return "Mike's Restaurant";
		return buildings.get(Math.abs(r.nextInt()) % buildings.size()).getName();
	}
	
	private String PickMarketLocation(){
		List<Building> buildings = BuildingList.findBuildingsWithType(BuildingList.MARKET);
		Random r = new Random();
		//return "Mike's Restaurant";
		return buildings.get(Math.abs(r.nextInt()) % buildings.size()).getName();
	}

	private void GoToParty(String location){
		  state = PersonState.Partying;
		  
		  GoToLocation(location, getTransportPreference());
		  
		  MyRole role = findRole(Role.HOME_GUEST_ROLE);
		  if(role == null){
			  role = new MyRole( RoleFactory.roleFromString(Role.HOME_GUEST_ROLE));
			  addRole(role);
		  }
		  HomeGuestRole hgr = (HomeGuestRole) role.role;
		  for(MyRole r : roles) {
			  r.role.deactivate();
		  }

		  AlertLog.getInstance().logMessage(AlertTag.PERSON, "Person", "Home Guest Role = "+role);
		  hgr.msgComeIn();
		  BuildingList.findBuildingWithName(location).addRole(role.role);
		  hgr.activate();
	}
	
	private void rsvpYes(Person pa, Party p){
		p.partyState=PartyState.GoingToParty;
		pa.msgIAmComing(this);
		MasterTime.getInstance().registerDateListener(p.dateOfParty.get(Calendar.MONTH), p.dateOfParty.get(Calendar.DAY_OF_MONTH), p.dateOfParty.get(Calendar.HOUR_OF_DAY), p.dateOfParty.get(Calendar.MINUTE), this);
		
	}
	
	private void rsvpNo(Person pa, Party p){
		pa.msgIAmNotComing(this);
		p.partyState=PartyState.NotGoingToParty;
	}
	
	public void deactivateCurrentRole(){
		for(MyRole r : roles) {
			  if(r.role.isActive())
				  r.role.deactivate();
		  }
	}
	
	private void GoToWork(){
		if(getCurrentJob() == null)
			return;
		this.workState = WorkState.GoingToWork;
		Employee r = (Employee) getCurrentJob().role;
		Building bdg = BuildingList.findBuildingWithName(r.getWorkLocation());
		if(BuildingList.findBuildingWithName(r.getWorkLocation()) instanceof Workplace ){
			Workplace w = (Workplace) bdg;
			
				GoToLocation(r.getWorkLocation(), getTransportPreference());

			
			if(w instanceof Restaurant){
				Restaurant rest = (Restaurant) bdg;
				if(r instanceof GenericHost){
					if(rest.getHostRole() != null)
						rest.getHostRole().setPerson(this);
					else if(r.getPerson() != this){
						r.setPerson(this);
					}
				}else if(r instanceof GenericCashier){
					if(rest.getCashierRole() != null)
						rest.getCashierRole().setPerson(this);
					else if(r.getPerson() != this){
						r.setPerson(this);
					}
				}else if(r instanceof GenericCook){
					if(rest.getCookRole() != null)
						rest.getCookRole().setPerson(this);
					else if(r.getPerson() != this){
						r.setPerson(this);
					}
				}
			}

			w.addRole(r);
			if(!w.isOpen()){
				pendingJobs.add(r);
				stateChanged();
				return;
			}
			r.activate();
			return;
//			if(!roles.contains(r)){
//					addRole(r);
//					if(!w.isOpen()){
//						pendingJobs.add((Role) r);
//						stateChanged();
//						return;
//					}
//					r.activate();
//					return;
//			 }else{
			}
		}
		
//		String workLocation = getCurrentJob().getWorkLocation();
//		GoToLocation(workLocation, getTransportPreference());
//		Role r = getCurrentJob();
//		if(r instanceof GenericHost){
//			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
//			addRole(rest.getHostRole());
//		}else if(r instanceof GenericCook){
//			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
//			addRole(rest.getCookRole());
//		}else if(r instanceof GenericCashier){
//			Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
//			addRole(rest.getCashierRole());
//		}else{
//			BuildingList.findBuildingWithName(workLocation).addRole(r);
//			addRole(getCurrentJob());
//		}
//		
//		if(!r.isActive())
//			r.activate();
	
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
    	String location = PickBankLocation();
        GoToLocation(PickBankLocation(), getTransportPreference());
        MyRole r = findRole(Role.BANK_CLIENT_ROLE);
        if(r == null){
                r = new MyRole(RoleFactory.roleFromString(Role.BANK_CLIENT_ROLE));
                addRole(r);
        }
        
        BankClientRole role = (BankClientRole) r.role;
        role.setIntent(BankClientRole.withdraw);
        BuildingList.findBuildingWithName(location).addRole(role);
        role.activate();

	}
	
	private void GoRobBank(){
	
		//needs a way to find a bank quite yet
		String location = PickBankLocation();
		GoToLocation(location, getTransportPreference());
		MyRole r = findRole(Role.BANK_CLIENT_ROLE);
		if(r == null){
			r = new MyRole(RoleFactory.roleFromString(Role.BANK_CLIENT_ROLE));
			addRole(r);
		}
		
		BankClientRole role = (BankClientRole) r.role;
		role.setIntent(BankClientRole.steal);
		BuildingList.findBuildingWithName(location).addRole(role);
		role.activate();
	}
	
	private void GoToMarketForItems(){
		String location = PickMarketLocation();
	    AlertLog.getInstance().logMessage(AlertTag.PERSON, "Person", "GOING TO MARKET FOR ITEMS");
	    String transport = getTransportPreference();
	    
	    //needs a way to find a bank quite yet
	     GoToLocation(location, transport);
	     MyRole r = findRole(Role.MARKET_CUSTOMER_ROLE);
	    if(r == null){
	            r = new MyRole(RoleFactory.employeeFromString(Role.MARKET_CUSTOMER_ROLE, location));
	            addRole(r);
	            r.role.activate();
	    }else{
	            r.role.activate();
	    }
	    
	    BuildingList.findBuildingWithName(location).addRole(r.role);
	}
	
        /**
         * @pre Assume that if we are paying back a loan we have a bank role
         */
        private void PayBackLoan(){
                String location = PickBankLocation();
                String transport = getTransportPreference();
                
                //needs a way to find a bank quite yet
                GoToLocation(location, transport);
                
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

<<<<<<< HEAD
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
		//if the Person has a Car use it
		if(myCar != null){
			modeOfTransportation = Preferences.CAR;
		}
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
				
				myCar.msgEnteringCar();
				myCar.msgNewDestination(location);
				stateOfLocation = StateOfLocation.InCar;
				try {
					onBus.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Preferences.WALK:
				System.err.println("Trying to walk to "+location);
				gui.DoGoTo(location);
				break;
		}
	}
	
	private void GoHome(){
	  stateOfLocation = StateOfLocation.AtHome;
	  state = PersonState.Idle;
	  String transport = getTransportPreference();
	  deactivateCurrentRole();
	  GoToLocation(home.getName(), transport);
	  HomeRole role = (HomeRole) findRole(Role.HOME_ROLE).role;
	  BuildingList.findBuildingWithName(home.getName()).addRole(role);
	  role.activate();
	  
	  //role.msgMakeFood();
	  role.msgEnterBuilding();

	}
	
	private void HostParty(){
		state = PersonState.HostingParty;
		HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;
		hr.msgHostParty();
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
		MyRole r = findMyJob();
		if(r == null)
			return true;
		
		return true;
	}
	
	private boolean needsToBeAtWork(){
		MyRole r = findMyJob();
		if(r != null){
			Employee e = (Employee) r.role;
			return true;
			//return stateOfEmployment == StateOfEmployment.Employee && e.getShift().intersectsWithTime(realTime);
		}
		
		return false;
	}
	
	private boolean needsTransportation(){
		return true;
	}
	
	//--------------------------UTILITIES---------------------------//
	
	
	private MyRole findRole(String role){
		if(role.equals(Role.HOME_ROLE)){
			int i = 0;
		}
		for(MyRole r : roles){
			if(r.role.getNameOfRole().equals(role)){
				return r;
			}
		}
		
		return null;
	}
	
	private MyRole findMyJob(){
		for(MyRole r : roles){
			if(r.role.isActive() && r.role instanceof Employee){
				return r;
			}
		}
		
		return null;
	}
	
	public void homeThrowParty() {
		MyRole role = findRole(Role.HOME_ROLE);
		HomeRole hr = (HomeRole) role.role;
		hr.msgThrowParty();
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
	public void addRole(MyRole r){
		if(r.role instanceof Employee){
			state = PersonState.Working;
		}
		
		if(roles.contains(r)){
			r.role.setPerson(this);
			return;
		}
		
		r.role.setPerson(this);
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
	public class Friend {
		PersonAgent person;
		boolean goodFriend;
		
		public Friend(PersonAgent person, boolean goodFriend){
			this.person = person;
			this.goodFriend = goodFriend;
		}
		public PersonAgent getPerson(){
			return person;
		}
	}
	
	enum PartyState {Host, ReceivedInvite, NeedsResponseUrgently, RSVPed, GoingToParty, NotGoingToParty, notRSVPed}
	
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
		public Person getHost(){
			return host;
		}
	}
	
	public void setGui(PersonGui gui){
		this.gui = gui;
		stateChanged();
		Timer hungerTimer = new Timer(5000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				hungerLevel += 1;
				if(hungerLevel % 5 == 0){
					stateChanged();
				}
			}
			
		});
		
		hungerTimer.start();
	}
	public PersonGui getPersonGui(){
		return this.gui;
	}
	
	public String toString(){
		return getName();
	}

	public List<Person> getFriends() {
		return friends;
	}
	public ResidenceBuildingPanel getHome(){
		return home;
	}
	
	private MyJob getCurrentJob(){
		for(MyRole r : roles){
			if(r instanceof MyJob){
				MyJob e = (MyJob) r;
				if(e.shift == currentShift){
					return e;
				}
			}
		}
		
		return null;
	}

	@Override
	public void timeAction(int hour, int minute) {
		// TODO Auto-generated method stub
		if(hour == Workplace.DAY_SHIFT_HOUR && minute == Workplace.DAY_SHIFT_MIN){
			currentShift = ShiftTime.DAY_SHIFT;
			if(getCurrentJob() != null && getCurrentJob().shift == ShiftTime.DAY_SHIFT){
				if(workState == WorkState.None)
					msgReportForWork();
			}
		}else if(hour == Workplace.NIGHT_SHIFT_HOUR && minute == Workplace.NIGHT_SHIFT_MIN){
			currentShift = ShiftTime.NIGHT_SHIFT;
			if(getCurrentJob() != null && getCurrentJob().shift == ShiftTime.NIGHT_SHIFT){
				if(workState == WorkState.None)
					msgReportForWork();
			}
		}else if(hour == Workplace.END_SHIFT_HOUR && minute == Workplace.END_SHIFT_MIN){
			currentShift = ShiftTime.NONE;
			money += SALARY;
		}
	}


public void dateAction(int month, int day, int hour, int minute) {
    HomeRole hr = (HomeRole) findRole(Role.HOME_ROLE).role;
    if(hr.rsvpDate.get(Calendar.MONTH) == month && hr.rsvpDate.get(Calendar.DAY_OF_MONTH) == day && hr.rsvpDate.get(Calendar.HOUR_OF_DAY) == hour && hr.rsvpDate.get(Calendar.MINUTE) == minute) {
            hr.msgResendInvites();
    }
    if(hr.partyDate.get(Calendar.MONTH) == month && hr.partyDate.get(Calendar.DAY_OF_MONTH) == day && hr.partyDate.get(Calendar.HOUR_OF_DAY) == hour && hr.partyDate.get(Calendar.MINUTE) == minute) {
            if(hr.partyAttendees.size()!=0){
                    state = PersonState.HostParty;
                    stateChanged();
            }
    }
    if(rentDueDate != null && home != null && home.isApartment == true) {
            if(rentDueDate.get(Calendar.DAY_OF_MONTH) == day && hour == 0) {
                    hr.msgRentDue(5.00,rentDueDate.get(Calendar.DAY_OF_MONTH));
                    rentDueDate.add(Calendar.DAY_OF_MONTH, 7);
                    //MasterTime.getInstance().registerDateListener(MasterTime.getInstance().get(Calendar.MONTH), (MasterTime.getInstance().get(Calendar.DAY_OF_MONTH)+1), 0, MasterTime.getInstance().get(Calendar.MINUTE), this);
            }

    }
    if(hr.featureRepairDate.get(Calendar.MONTH) == month && hr.featureRepairDate.get(Calendar.DAY_OF_MONTH) == day && hr.featureRepairDate.get(Calendar.HOUR_OF_DAY) == hour && hr.featureRepairDate.get(Calendar.MINUTE) == minute) {
            hr.msgFixedFeature();
    }
    for(Party p: parties){
            if(p.dateOfParty.get(Calendar.MONTH) == month && p.dateOfParty.get(Calendar.DAY_OF_MONTH) == day && p.dateOfParty.get(Calendar.HOUR_OF_DAY) == hour && p.dateOfParty.get(Calendar.MINUTE) == minute) {
                    state = PersonState.GoingToParty;
                    stateChanged();
            }        
    }
}

	//Control Panel Information Access Functions
		//Only include what hasn't already been done
	public int getHungerLevel() {
		return hungerLevel;
	}
	public String getCurrentJobString() {
		return findMyJob().role.getNameOfRole();
	}
	
	public String getCurrentLocation() {
		String location  = "N/A";
		switch (stateOfLocation) {
		case AtHome:
			location = "Home";
			break;
			
		case AtBank:
			location = "Bank";
			break;
		
		case AtMarket:
			location = "Market";
			break;
			
		case AtRestaurant:
			location = "Restaurant";
			break;
			
		case InCar: 
			location = "City";
			break;
			
		case InBus:
			location = "City";
			break;
			
		case Walking:
			location = "City";
			break;
		}
		
		return location;
	}
	
	public void addFriend(PersonAgent agent){
		if(!friends.contains(agent))
			friends.add(agent);
	}
	
	public String getCurrentRole(){
		for(MyRole r : roles){
			if(r.role.isActive())
				return r.role.getNameOfRole();
		}
		
		return null;
	}
	
	public ShiftTime getCurrentShift(){
		return currentShift;
	}
	
	public boolean hasCar() {
		if (myCar != null) {
			return true;
		}
		else
			return false;
	}
}
