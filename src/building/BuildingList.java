package building;

import gui.CityAnimationPanel;

import java.util.ArrayList;
import java.util.Calendar;

import trace.AlertLog;
import trace.AlertTag;
import util.DateListener;
import util.MasterTime;
import util.TimeListener;

public class BuildingList extends ArrayList<Building> implements TimeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BuildingList instance = null;
	
	public static String RESTAURANT = "Restaurant";
	public static String MARKET = "Market";
	public static String BANK = "Bank";
	
	
	protected BuildingList(){
		super();
		
		MasterTime.getInstance().registerTimeListener(Workplace.DAY_SHIFT_HOUR, Workplace.DAY_SHIFT_MIN, false, this);
		MasterTime.getInstance().registerTimeListener(Workplace.NIGHT_SHIFT_HOUR, Workplace.NIGHT_SHIFT_MIN, false, this);
		MasterTime.getInstance().registerTimeListener(Workplace.END_SHIFT_HOUR, Workplace.END_SHIFT_MIN, false, this);
	}
	
	@Override
	public synchronized boolean add(Building e) {
		// TODO Auto-generated method stub
		return super.add(e);
	}

	public static BuildingList getInstance(){
		if(instance == null){
			instance = new BuildingList();
		}
		
		return instance;
	}
	
	public synchronized static Building findBuildingWithName(String name){
		for(Building b : getInstance()){
			if(b.getName().equals(name)){
				return b;
			}
		}
		
		return null;
	}
	
	public synchronized static ArrayList<Building> findBuildingsWithType(String name){
        ArrayList<Building> buildings = new ArrayList<Building>();
        
            for(Building b : getInstance()){
                    if(name.equals(BANK)){
                            if(b instanceof Bank){
                                    buildings.add(b);
                            }
                            }
                    if(name.equals(RESTAURANT)){
                            if(b instanceof Restaurant){
                                    buildings.add(b);
                            }
                    }
                    if(name.equals(MARKET)){
                            if(b instanceof Market){
                                    buildings.add(b);
                            }
                    }
            }
            
            return buildings;
    }
	
	private void endNightShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Ending the Night Shift");
		for(Building b : this){
			if(b instanceof Workplace){
				Workplace w = (Workplace) b;
				w.notifyEmployeesTheyCanLeave();
			}
		}
	}
	
	private void beginDayShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Starting the Day Shift");
	}
	
	private void endDayShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Ending the Day Shift");
		for(Building b : this){
			if(b instanceof Workplace){
				Workplace w = (Workplace) b;
				w.notifyEmployeesTheyCanLeave();
			}
		}
	}
	
	private void beginNightShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Starting the Night Shift");
	}

	@Override
	public void timeAction(int hour, int minute) {
		// TODO Auto-generated method stub
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "ACTION CALLED ---- "+hour+":"+minute);
		if(hour == Workplace.DAY_SHIFT_HOUR && minute == Workplace.DAY_SHIFT_MIN){
			beginDayShift();
		}else if(hour == Workplace.NIGHT_SHIFT_HOUR && minute == Workplace.NIGHT_SHIFT_MIN){
			endDayShift();
			beginNightShift();
		}else if(hour == Workplace.END_SHIFT_HOUR && minute == Workplace.END_SHIFT_MIN){
			endNightShift();
		}
	}
	
}
