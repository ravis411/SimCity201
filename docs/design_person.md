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

enum PersonState state {Idle, NeedsMoney, PayRoleNow, PayRentNow, GettingMoney, NeedsFood, GettingFood};

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

msgImHungry(){
  state = NeedsFood;
}

msgINeedMoney(double amountNeeded){
  state = NeedsMoney;
  moneyNeeded += amountNeeded;
}

msgYouHaveALoan(double loan){
  loanAmount = loan;
}

msgReportForWork(String role){
   if ∃ Role r in roles ∋ r.getRole==role
     r.activate();
 
}
msgReceiveSalary(double amount){
  money += amount;
}
msgPayBackLoanUrgently(){
   state=PayLoanNow;
}
msgYouHaveRentDue(double rent){
  rentAmount=rent;
}
msgPayBackRentUrgently(){
   
   state=PayRentNow;
}
```

##Scheduler
```
if ∃ Role r in roles ∋ r.isActive()
  r.pickAndExecuteAction();
if state = PayLoanNow
  PayBackLoan();

if state = PayLoanNow
  PayBackRent();
  
if state = NeedsFood
  GoGetFood();
  
if state = NeedsMoney
  GoGetMoney();
  
if loan != 0.0
  PayBackLoan();
if rent!=0.0
  PayBackRent();
```

##Actions
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
