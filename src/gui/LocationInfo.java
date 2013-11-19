package gui;

import java.awt.Dimension;


/**
 * Holds Information where guis can enter and move to the building
 * 
 */
public class LocationInfo {
	String name;
	Dimension positionToEnterFromMainGrid;
	Dimension entranceFromMainGridPosition;
	Dimension positionToEnterFromRoadGrid;//<<-- a bus goes to a different place than a person.
}
