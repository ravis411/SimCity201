package byronRestaurant;


import Person.Role.Role;
import Person.Role.ShiftTime;
import byronRestaurant.gui.CookGui;
import interfaces.generic_interfaces.GenericCook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;



import byronRestaurant.RevolvingStand;
import trace.AlertLog;
import trace.AlertTag;


/**
 * Restaurant Cook Agent
 * make sure to change when cook makes an order to market (threshold point)
 * 
 * 
 */
public class CookRole extends GenericCook {
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public enum cookStatus {pending, cooking, done};
	private String name;
	private int foodThreshold = 3;
	private int foodMaximum = 50;
	private CookGui cookGui;
	private RevolvingStand revolvingStand;
	private Semaphore atPlatingArea = new Semaphore(0,true);
	private Semaphore atKitchen = new Semaphore(0,true);
	private Semaphore atDefault = new Semaphore(0,true);
	protected class Order{
		public WaiterRole waiter;
		public int table;
		public String choice;
		public cookStatus status;

		Order(WaiterRole waiterRole, int t, String c){
			waiter = waiterRole;
			table = t;
			choice = c;
			status = cookStatus.pending;
		}
	}
	private class Food{
		public String type;
		public double cookTime;
		public int amount;

		Food(String t, double c, int a){
			type = t;
			cookTime = c;
			amount = a;
		}
	}
	private Map<String, Food> inventory = Collections.synchronizedMap(new HashMap<String, Food>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("Steak", new Food("Steak", 6000,5));
		put("Chicken", new Food("Chicken", 5000, 5));
		put("Salad", new Food("Salad", 1000, 5));
		put("Pizza", new Food("Pizza", 8000, 5));
	}});



	//Initialize Cook
	public CookRole(String location) { 
		super(location);
		revolvingStand = new RevolvingStand();

		Timer checkRevolvingStand = new Timer(15000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				stateChanged();
			}

		});

		checkRevolvingStand.start();
	}


	//	public void AddMarket(MarketAgent m){
	//		market = m;
	//	}

	// messages
	public void msgHereIsAnOrder(WaiterRole waiterRole, int table, String choice){
		orders.add(new Order(waiterRole, table, choice));
		print("Receiving new order from " + waiterRole.getName());
		stateChanged();
	}
	public void msgFoodDone(Order o){
		o.status = cookStatus.done;
		stateChanged();
	}

	public void msgHereIsFood(String s, int a){
		int temp = inventory.get(s).amount + a;
		Food tempFood = new Food(s, inventory.get(s).cookTime,temp);
		inventory.put(s,tempFood);
		synchronized(orders){
			for (Order o : orders){
				o.waiter.msgRestockedItem(s);
			}
		}
		stateChanged();
	}

	public void msgNotEnoughFood(String s){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Market has run out, keep " + s + " off the menu.");
	}
	public void msgTakingFood(){
		cookGui.setOnPlate(false);
	}
	public void msgAtPlatingArea(){
		atPlatingArea.release();
		stateChanged();
	}
	public void msgAtKitchen(){
		atKitchen.release();
		stateChanged();
	}
	public void msgAtDefault(){
		atDefault.release();
		stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAction() {
		synchronized(orders){
			for (Order o : orders){
				if (o.status == cookStatus.done){
					placeOrder(o);
					return true;
				}
				if (o.status == cookStatus.pending){
					cookOrder(o);
					return true;
				}
			}
			if(!revolvingStand.isEmpty()){
				//get the order from the stand
				WaiterRole.Order order = revolvingStand.getLastOrder();
				//structure the order data to fit in with my old cooking routine
				Order newOrder = new Order(order.getWaiter(), order.getTable(), order.getChoice());
				orders.add(newOrder);
				//cook the order in the same way
				cookOrder(newOrder);
				return true;

			}
		}
		return false;
	}

	//Actions
	private void placeOrder(Order o){
		cookGui.doGoToPlatingArea();
		try {
			atPlatingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setOnPlate(true);
		o.waiter.msgOrderIsReady(o.table, o.choice);
		print("Order is ready");
		orders.remove(o);
		cookGui.doGoToDefault();
		try {
			atDefault.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	private void cookOrder(final Order o){
		if (inventory.get(o.choice).amount == foodThreshold){
			//				o.waiter.msgOutOfStock(o.choice, o.table);
			//				market.msgIWantFood(o.choice, (foodMaximum-inventory.get(o.choice).amount));
			//				orders.remove(o);
		}else if (inventory.get(o.choice).amount == 0){
			o.waiter.msgOutOfStock(o.choice, o.table);
			orders.remove(o);
		}
		else{	
			Food food = inventory.get(o.choice);
			cookGui.doGoToKitchen();
			try {
				atKitchen.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cookGui.setIsCooking(true);
			o.status = cookStatus.cooking;
			
			Timer timer2 = new Timer((int) food.cookTime, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					msgFoodDone(o);
					cookGui.setIsCooking(false);
				}
			});
			timer2.start();
			
			inventory.get(o.choice).amount --;
			print ("Cooking order of " + o.choice + ". " + inventory.get(o.choice).amount + " remaining.");
		}
	}

	//Utilities
	public String getName(){
		return name;
	}

	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}

	@Override
	public ShiftTime getShift() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return Role.RESTAURANT_BYRON_COOK_ROLE;
	}
	
	public RevolvingStand getRevolvingStand(){
		return revolvingStand;
	}

}
