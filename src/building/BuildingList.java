package building;

import java.util.ArrayList;

public class BuildingList extends ArrayList<Building>{

	private static BuildingList instance = null;
	
	protected BuildingList(){
		super();
	}
	
	public static BuildingList getInstance(){
		if(instance == null){
			instance = new BuildingList();
		}
		
		return instance;
	}
	
	public static Building findBuildingWithName(String name){
		for(Building b : getInstance()){
			if(b.getName().equals(name)){
				return b;
			}
		}
		
		return null;
	}
	
}
