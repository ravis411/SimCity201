#Residence Class

##Residence - inherits from Building class (provided by Prof. Crowley)

###Data
Person occupant
Person owner

##Home - inherits from Residence

###Data
boolean ownedByResident
int mortgage/rent //depends on whether or not ownedByResident is true

##Apartment - inherits from Residence

###Data
int rent
int aptNumber

##Apartment Building - inherits from Building class

###Data
List<apartments>
Person landlord

##Home Role

###Data
boolean hungry
boolean tired
Map <String item, int quantity> inventory
List <HomeFeature> features //includes appliances, toilets, sinks, etc (anything that can break)

###Messages
msgRentDue (int amount);
msgHungry ();
msgTired();
msgRestockItem (String item, int quantity) {
	increase item.quantity by quantity
}
msgFixedItem (HomeFeature fixedFeature) {
	HomeFeature.get(fixedFeature).working == true
}

###Scheduler
if hungry { eat() }
if tired { goToSleep() }
for each item i in inventory
	if i<2 { goToMarket(i) }
for each HomeFeature f in features
	if !f.working { fileWorkOrder (f) } 

###Actions
	goToMarket (String item);
	fileWorkOrder (HomeFeature brokenFeature);
	payRent (int amount);
	eat(); //chooses whether person cooks at home or goes out to a restaurant
	goToSleep();
	
inner class HomeFeature {
	String name;
	boolean working;
}
	
