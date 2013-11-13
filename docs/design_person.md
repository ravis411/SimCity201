#Person/Role Design Doc

##Basic information
Much of this design is based on that of Prof. Wilczynski because it outlines a standard way to interact between PeopleAgents and the mutliple roles they can take on throughout their time in the SimCity world. So essentially, the thread will exist within the PersonAgent, which will inherit from Agent for all of its thread-handling interface. The separate Roles, however, which will inherit from a superclass Role will contain the messages, schedulers, and actions common to most agents without having their own thread. 
> We don't need this thread because it is contained within the larger PersonAgent

More specifics about the interaction between PersonAgents and Role's are outlined below.

##Role class
The role is essentially an "unthreaded" agent, which uses the PersonAgent (which has-a list of Roles) thread to do its work. The role class will take the place of Agents in our previous RestaurantAgent's designs and will still implement messages, schedulers, and actions in the same fashion while maintaining the following interface:

```
abstract class Role {
  PersonAgent myPerson;
  boolean isActive;
}
```
Initially I think we will make an assumption that the Role will deactivate itself when it is no longer important. For example, when the Restaurant#CustomerRole leaves his restaurant, it can deactivate that role, because clearly it won't be of importance. In this fashion, the Person class won't have to keep track of everything.

###Intended Role Utilities
```
void setPerson(PersonAgent p);
PersonAgent getPerson();

void isActive(){ return isActive;}

//cues the Person scheduler
void stateChanged(){
  myPerson.stateChanged();
}

//handles whether or not this role is active
void activate(){ isActive = true;}
void deactivate(){ isActive = false;}
```
##Interaction Diagram
![interaction diagram](http://www-scf.usc.edu/~ciesielk/csci201/person_interaction.jpg)
> Preliminary Interaction diagram because we are not sure if the separate Roles will require more functionality of general people. Also, the message above called reportForWork will have a string parameter for the job that the person has to report for.

##Data
One side of the need for a PersonAgent class is the fact that our agents really are "people" outside of their roles as Cooks and Waiters and Customers in our SimCity establishments, and therefore have attributes we give to them independent of where they are at a given time.

###Data Given to Ordinary People
```
String name;
double money;
double moneyNeeded;
int age;
Residence home;
int SSN; //distinguishes individual people from all people

double loanAmount;
double rentAmount;

//states - somewhat implementation based - not complete list probably

////could very well end up being a normalized floating-point number
enum StateOfNourishment = {NotHungry, SlightlyHungry, Hungry, VeryHungry, Starving} 
enum stateOfLocation = {AtHome, AtBank, AtMarket, AtRestaurant#, InCar, InBus, ...}
enum stateOfEmployment = {Customer, Employee, Idle}

//maybe
enum stateOfBeing = {Asleep, Awake}

//some basic high-level person task states necessary in addition to the other states
enum PersonState state {Idle, NeedsMoney, PayRoleNow, PayRentNow, GettingMoney, NeedsFood, GettingFood};
```

###Utilities for Data
```
//getters
String getName();
double getMoney();
int getAge();
Residence getHome();
int getSSN();
double getLoan();

//setters

//used when we spend money or make money
void setMoney(double money);
void setLoan(double loan);
```

###Data Given to Mediate Between Roles
```
List<Role> roles;
```

###Utilities for Roles
```

//these methods will just call the corresponding function in the role List
void addRole(Role r);
void removeRole(Role r);
```

##Messages
```
/**
  * Message sent by a vehicle, like a bus, to tell its passengers
  * when it has arrived somewhere
  * @param currentLocation the location the bus has arrived at
  */
msgWeHaveArrived(String currentDestination){
  //unpause agent, which will be in transit (implementation not necessary here)
}

/**
  * Message probably sent by an outside GUI to force hunger on the 
  * person
  */
msgImHungry(){
  state = NeedsFood;
}

/**
  * Message sent by any role where the Person needs money but doesn't have enough
  * @param amountNeeded the amount of money the Person needs in addition to what he already has
  */
msgINeedMoney(double amountNeeded){
  state = NeedsMoney;
  moneyNeeded += amountNeeded;
}

/**
  * Message sent by a Bank employee to inform the person he has a loan
  //---------THIS MESSAGE COULD BE SENT TO BANKCUSTOMER WHO JUST CHANGES HIS PERSON DATA----------//
  * @param loan the amount of money in the loan
  */
msgYouHaveALoan(double loan){
  loanAmount = loan;
}

/**
  * Message sent by the TopAgent at a particular workplace 
  */
msgReportForWork(String role){
   if ∃ Role r in roles ∋ r.getRole() == role
     r.activate();
 
}

/**
  * Message called, probably by a timer, which increases the person's
  * amount of money by the amount he makes from work
  */
msgReceiveSalary(double amount){
  money += amount;
}

/**
  * Somehow if the person forgets to pay his loan, the Bank will remind him
  * around the time of the due date
  */
msgPayBackLoanUrgently(){
   state=PayLoanNow;
}

/**
  * Message sent by the Building TopAgent or some other mechanism like a timer
  * for providing a scheduled rent payment.
  * @param rent the amount due for rent
  */
msgYouHaveRentDue(double rent){
  rentAmount=rent;
}

/**
  * Message sent by the building (or other mechanism see above) when the rent
  * due date is close and needs paying.
  */
msgPayBackRentUrgently(){
   state=PayRentNow;
}
```

##Scheduler
Note that we use some message stubs here that will most likely be fleshed out later, but for now will just denote a state-like script that is named for its specific task.
```
//need this to keep track of how the Role scheduler's are functioning
boolean outcome = false;



//if we need to pay back our loan/rent urgently, then we have to do that even if it risks being late for work
if state = PayLoanNow
  PayBackLoan();

if state = PayRentNow
  PayBackRent();

//script-like approach

if stateOfWork == Employee && needsToBeAtWork()
  getWorkRole().activate();
  
if isHungry() && canGoGetFood()
  GoGetFood();
else if isHungry() && canGoOnBreak()
  GoOnBreak();
  GoGetFood();
  
if onPublicTransportation() && arrivedAtDestination()
  GetOffTransportation();

//the role schedulers are almost always going to take priority
if ∃ Role r in roles ∋ r.isActive()
  outcome = r.pickAndExecuteAction() || outcome;

//these states are somewhat have less importance, e.g. if we aren't doing anything and can pay back the loan, then we should do that -- these are also done in more of a traditional scheduler sense.
  
if state = NeedsFood
  GoGetFood();
  
if state = NeedsMoney
  GoGetMoney();
  
if loan != 0.0
  PayBackLoan();
if rent!=0.0
  PayBackRent();
  
return false || outcome;
```

##Actions
A short list of actions that we know will be necessary. Because the entire scheduler is subject to change as the Roles require more functionality, some of the above Action stubs may be needed/ may not be needed. We feel like this core will be necessary.
```
GoGetFood(){
  state = GettingMoney;
  Building b = PickFoodLocation();
  TransportationMode tm = pickTransportMode();
  DoGoToFoodLocation(b, tm);
  Role role;
  if(b instanceof Restaurant){
    role = getRestaurantCustomerRole();
  }else if(b instanceof Apartment || b instanceof Home){
    role = getHomeRole();
  }
  
  role.activate();
}

GoGetMoney(){
  state = GettingMoney;
  Bank b = pickBank();
  TransportationMode tm = pickTransportMode();
  DoGoToBank(b, tm);
  BankCustomerRole bcr = getBankCustomerRole();
  bcr.activate();
}

PayBackRent(){
  HomeRole hr= getHomeRole();
  hr.msgPayRent(rentAmount);
}

GetOffTransportation(){
  Transport t = myTransportation();
  t.msgWeHaveArrived();
}

PayBackLoan(){
  DoGoToBank();
  if(money >= loanAmount){
  
    //--------------------NEEDS MSG FOR ENTERING BANK WITH THE INTENT TO PAY LOAN-----------------------//
    BankCustomerRole bcr = getBankCustomerRole();
    bcr.msgPayLoan(loanAmount);
  else{
    //--------------------NEEDS MSG FOR WITHDRAWING FROM BANK------------------------------------------//
    BankCustomerRole bcr = getBankCustomerRole();
    bcr.msgWithdrawMoney();
    bcr.msgPayLoan(loanAmount);
  }
}
```
