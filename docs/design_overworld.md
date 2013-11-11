#OverWorld Design Doc

##Transportation (Base Class)

###Data
Interface: vehicle
Subclass: bus, privateVehicle

+ Map<Coordinate, String> cityMap; //For GUI
+ List<Person> passengers;
+ String nextDestination, currentLocation;
+ List<String> Destinations; //Ordered list of destinations

###Messages
+ msgArrivedAtDestination(Coordinate) { //From GUI
	currentLocation = cityMap.get(Coordinate);
}

###Scheduler
+ if (currentLocation != nextDestination) {
	goToDestination(nextDestination);
+ if (currentLocation = nextDestination) {
	arrived();
}

###Actions

+ arrived(String destination) {
	for (passenger in passengers) {
		msgWeHaveArrived(currentLocation); //Send to gui
		nextDestination = Destinations.next();
		wait(5);
		stateChanged();
	}
}
+ goToDestination(String nextDestination) {
	gui.DoGoToDestination(nextDestination);
}