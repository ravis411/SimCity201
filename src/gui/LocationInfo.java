package gui;

import java.awt.Dimension;


/**
 * Holds Information where guis can enter and move to the building
 * 
 */
public class LocationInfo {
	String name;
	Dimension positionToEnterFromMainGrid = null;
	Dimension entranceFromMainGridPosition = null;
	Dimension positionToEnterFromRoadGrid = null;//<<-- a bus goes to a different place than a person.
	Dimension entranceFromRoadGrid = null;

	public LocationInfo(){
		
	}
	public LocationInfo(LocationInfo location){
		this.name = location.name;
		if(location.positionToEnterFromMainGrid != null)
			this.positionToEnterFromMainGrid = new Dimension(location.positionToEnterFromMainGrid);
		if(location.entranceFromMainGridPosition != null)
			this.entranceFromMainGridPosition = new Dimension(location.entranceFromMainGridPosition);
		if(location.positionToEnterFromRoadGrid != null)
			this.positionToEnterFromRoadGrid = new Dimension(location.positionToEnterFromRoadGrid);
		if(location.entranceFromRoadGrid != null)
			this.entranceFromRoadGrid = new Dimension(location.entranceFromRoadGrid);
	}

}
