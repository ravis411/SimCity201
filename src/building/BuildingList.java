package building;

import gui.CityAnimationPanel;

import java.util.ArrayList;
import java.util.Calendar;

import trace.AlertLog;
import trace.AlertTag;
import util.MasterTime;
import util.TimeListener;

public class BuildingList extends ArrayList<Building> implements TimeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BuildingList instance = null;
	
	private static int DAY_SHIFT_HOUR = 9;
	private static int DAY_SHIFT_MINUTE = 0;
	
	private static int NIGHT_SHIFT_HOUR = 17;
	private static int NIGHT_SHIFT_MINUTE = 0;
	
	private static int END_SHIFT_HOUR = 0;
	private static int END_SHIFT_MINUTE = 0;
	
	protected BuildingList(){
		super();
		
		MasterTime.getInstance().registerTimeListener(DAY_SHIFT_HOUR, DAY_SHIFT_MINUTE, false, this);
		MasterTime.getInstance().registerTimeListener(NIGHT_SHIFT_HOUR, NIGHT_SHIFT_MINUTE, false, this);
		MasterTime.getInstance().registerTimeListener(END_SHIFT_HOUR, END_SHIFT_MINUTE, false, this);
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
	
	
	private void endNightShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Ending the Night Shift");
		for(Building b : this){
			if(b instanceof Workplace){
				Workplace w = (Workplace) b;
			}
		}
	}
	
	private void beginDayShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Starting the Day Shift");
	}
	
	private void endDayShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Ending the Day Shift");
	}
	
	private void beginNightShift(){
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "Starting the Night Shift");
	}

	@Override
	public void timeAction(int hour, int minute) {
		// TODO Auto-generated method stub
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, "Timing", "ACTION CALLED ---- "+hour+":"+minute);
		if(hour == DAY_SHIFT_HOUR && minute == DAY_SHIFT_MINUTE){
			beginDayShift();
		}else if(hour == NIGHT_SHIFT_HOUR && minute == NIGHT_SHIFT_MINUTE){
			endDayShift();
			beginNightShift();
		}else if(hour == END_SHIFT_HOUR && minute == END_SHIFT_MINUTE){
			endNightShift();
		}
	}
	
}
