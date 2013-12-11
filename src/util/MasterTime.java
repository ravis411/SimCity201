package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MasterTime {

        private static MasterTime masterTime = null;
        
        private final Calendar calendar = Calendar.getInstance();
        private List<MyDateListener> dateListeners;
        private List<MyTimeListener> timeListeners;
        private Semaphore dateListenersAvailable = new Semaphore(0,false);
        
        public static MasterTime getInstance(){
                if(masterTime == null)
                        masterTime = new MasterTime();
                return masterTime;
        }
        
        protected MasterTime(){
                dateListeners = Collections.synchronizedList(new ArrayList<MyDateListener>());
                timeListeners = Collections.synchronizedList(new ArrayList<MyTimeListener>());
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        }
        
        /**
         * Helper function to access a particular field of the calendar
         * @param field Calendar class constants for unit of interest
         * @return the number of units of interest
         */
        public int get(int field){
                return calendar.get(field);
        }
        
        /**
         * Helper function that adds a certain amount of time to the 
         * calendar time
         * @param field the unit of time defined by Calendar constants
         * @param amt the number of units to add
         */
        public synchronized void add(int field, int amt){
                boolean breakBool=false;
                calendar.add(field, amt);
                
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                
                boolean weekend = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
                                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

                synchronized (timeListeners) {
                        for(MyTimeListener tl : timeListeners){
                                if(tl.hour == hour && tl.minute == minute && tl.weekend == weekend){
                                        tl.listener.timeAction(hour, minute);
                                }
                        }
                }
                
                List<MyDateListener> toRemove = new ArrayList<>();//<--to hold the dl to remove
                synchronized (dateListeners) {
                        for(MyDateListener dl : dateListeners){
                                if(dl.month == month && dl.day == day && dl.hour == hour && dl.minute == minute){
                                                dl.listener.dateAction(month, day, hour, minute);
                                        toRemove.add(dl);
                                        breakBool=true;
                                       // break;
                                        
                                }
                                //else
                                      //  break;
                        }
                }

                //now remove dl to prevent ConcurrentModificationExceptions.
                for (MyDateListener dListener1: toRemove){
                        for (Iterator<MyDateListener> dLst = dateListeners.iterator(); dLst.hasNext();){
                                MyDateListener dListener2 = dLst.next();
                                        if (dListener2 == dListener1){
                                                dLst.remove();
                                        }
                        }
                }

        }
        
        /**
         * Register a Time Listener to receive time updates, each day 
         * @param hour the hour to respond to
         * @param minute the minute to respond to
         * @param weekend true if he should receive events during the weekend, false otherwise
         * @param sender the actual person or role registering for updates
         * @return true if the registration worked, false if such a registration already exists
         */
        public boolean registerTimeListener(int hour, int minute, boolean weekend, TimeListener sender){
                synchronized (timeListeners) {
                for(MyTimeListener tl : timeListeners){
                        if(tl.listener == sender && tl.hour == hour && tl.minute == minute && tl.weekend == weekend){
                                return false;
                        }
                }
                }
                timeListeners.add(new MyTimeListener(hour, minute, weekend, sender));
                return true;
        }
        
        /**
         * Register once for a Time Listener to receive updates for a specific date
         * @param month the month to respond to
         * @param day the day to respond to
         * @param hour the hour to respond to
         * @param minute the minute to respond to
         * @param sender the actual person or role registering for updates
         * @return true if the registration worked, false if such a registration already exists
         */
        public boolean registerDateListener(int month, int day, int hour, int minute, DateListener sender){
                /*if (semaphoreNeeded){
                        try {
                                dateListenersAvailable.acquire();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        }*/
                synchronized (dateListeners) {
                        
                for(MyDateListener dl : dateListeners){
                        if(dl.listener == sender && dl.month == month && dl.day == day && 
                                        dl.hour == hour && dl.minute == minute){
                                return false;
                        }
                }
                }
                
                dateListeners.add(new MyDateListener(month, day, hour, minute, sender));
                return true;
        }
        
        /**
         * Creates a string representation of the date via a helper function
         * @return
         */
        public String calendarString(){
                return new java.text.SimpleDateFormat("EEEE, MM/dd/yyyy hh:mm a").format(calendar.getTime());
        }
        
        /**
         * Private struct for holding TimeListener data
         *
         */
        private class MyTimeListener{
                
                TimeListener listener;
                int hour;
                int minute;
                boolean weekend;
                
                public MyTimeListener(int hour, int minute, boolean weekend, TimeListener listener){
                        this.listener = listener;
                        this.hour = hour;
                        this.minute = minute;
                        this.weekend = weekend;
                }
                
        }
        
        /**
         * Private struct for holding DateListener
         *
         */
        private class MyDateListener{
                
                DateListener listener;
                int hour;
                int minute;
                int day;
                int month;
                
                public MyDateListener(int month, int day, int hour, int minute, DateListener listener){
                        this.listener = listener;
                        this.month = month;
                        this.day = day;
                        this.hour = hour;
                        this.minute = minute;
                }
        }
        
        
        
}