package Person;

import java.util.HashMap;
import java.util.Map;

public class Preferences {

	public enum KeyValue {VEHICLE_PREFERENCE}
	
	public final static String BUS = "BUS";
	public final static String CAR = "CAR";
	public final static String WALK = "WALK";
	
	private Map<KeyValue, String> map;
	
	public Preferences(){
		map = new HashMap<KeyValue, String>();
		//String vehicle_pref = Math.random() < 0.5 ? BUS : WALK;
		String vehicle_pref = BUS;
		//String vehicle_pref = BUS;
		map.put(KeyValue.VEHICLE_PREFERENCE, vehicle_pref);
	}
	
	public String get(KeyValue field){
		return map.get(field);
	}
	
}
