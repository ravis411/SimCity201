##Residence Class and Home/Apartment Manager Roles

###Residence - inherits from Building class (provided by Prof. Crowley)

####Data
	Person occupant
	Person owner

###Home - inherits from Residence

####Data
	boolean ownedByResident
	int mortgage/rent //depends on whether or not ownedByResident is true

###Apartment - inherits from Residence

####Data
	int rent
	int aptNumber

###Apartment Building - inherits from Building class

####Data
	List<apartments>
	Person landlord

###Home Role

####Data
	Person landlord
	int rentOwed = 0
	boolean tired
	Map <String item, int quantity> inventory
	List <HomeFeature> features //includes appliances, toilets, sinks, etc (anything that can break)
	
	inner class HomeFeature {
		String name
		boolean working
	}

####Messages
	msgRentDue (int amount) {
		rentOwed = amount
	}
	msgTired() { //called by timer
		tired == true
	}
	msgRestockItem (String item, int quantity) {
		increase item.quantity by quantity
	}
	msgFixedFeature (String name) {
		features.get(name).working == true
	}

####Scheduler
	if rentOwed > 0
		{ payRent() }
	if Person.stateOfNourishment == hungry
		{ eat() }
	if tired 
		{ goToSleep() }
	if any item.quantity in inventory < 2
		{ goToMarket(item) }
	for each HomeFeature f in features
		if !f.working
			{ fileWorkOrder (f) }

####Actions
	goToMarket (String item);
	fileWorkOrder (HomeFeature brokenFeature) {
		landlord.msgBrokenFeature(brokenFeature.name, this)
	}
	payRent () {
		landlord.msgRentPaid (this, rentOwed)
		rentOwed = 0
	}
	eat(); //chooses whether person cooks at home or goes out to a restaurant
	goToSleep();
	
###Apartment Manager Role

####Data
	List <Person> residents
	List <BrokenFeature> thingsToFix
	boolean collectRent
	
	inner class BrokenFeature {
		String name
		Person resident
	}

####Messages
	msgCollectRent () { //called by timer when it's time to collect rent from residents
		collectRent = true
	}
	msgRentPaid (Person, int amount)
	msgBrokenFeature(String name, Person p) {
		thingsToFix.add(new BrokenFeature(name, p)
	}

####Scheduler
	if collectRent == true
		for each Person p in residents
			{ chargeRent(p) }
	for each Person p in residents
		if p hasn't paid rent in 5 days
			{ demandRent(p) }
	if !thingsToFix.empty()
		{ fixFeature(thingsToFix.get(0)) }

####Actions
	chargeRent (Person p) {
		p.msgPayBackRent
	}
	demandRent (Person p) {
		p.msgPayBackRentUrgently
	}
	fixFeature(BrokenFeature bf) {
		bf.resident.msgFixedFeature(bf.name)
	}
