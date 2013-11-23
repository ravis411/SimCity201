package gui;

import java.awt.Dimension;


/**
 * Holds Information where guis can enter and move to the building
 * 
 */
public class LocationInfo {
	public String name;
	
	/**The position a person would enter a building from
	 */
	public Dimension positionToEnterFromMainGrid = null;
	
	/**
	 * The position a person would go inside the building
	 * This position should be acquired by the building.
	 */
	public Dimension entranceFromMainGridPosition = null;
	
	/**
	 * The position a vehicle would enter building from.
	 * Generally this is the closest road position to the building.
	 */
	public Dimension positionToEnterFromRoadGrid = null;//<<-- a bus goes to a different place than a person.
	
	/**
	 * The location a vehicle would be once inside the location.
	 * This position should be acquired by the location.
	 */
	public Dimension entranceFromRoadGrid = null;
	
	/** This is the grid sector that the location is in. 
	 * This is used for instance to help a person cross the road.
	 * 
	 */
	public int sector = 0;
	
	

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
		this.sector = location.sector;
	}

}
