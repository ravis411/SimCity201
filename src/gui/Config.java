package gui;

public interface Config {
	//arranged in hierarchy
	
	String PEOPLE_NODE = "people";
		String PERSON_NODE = "person";
			String NAME_ATTRIBUTE = "name";
			String HOME_ATTRIBUTE = "home";
			String JOB_ATTRIBUTE = "job";
			String LOCATION_ATTRIBUTE = "location";
			String CAR_ATTRIBUTE = "hasCar";
			String SHIFT_ATTRIBUTE = "shift";
			
			String FRIENDS_NODE = "friends";
				String FRIEND_NODE = "friend";
	
	String BUILDING_CONFIG_NODE = "building-config";
		String BUILDING_NODE = "building";
			String BUILDING_NAME_ATTRIBUTE = "name";
			String BUILDING_ID_ATTRIBUTE = "id";
			String BUILDING_TYPE_ATTRIBUTE = "type";
	
	
}
