package mikeRestaurant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import mikeRestaurant.interfaces.Cashier;
import mikeRestaurant.interfaces.Cook;
import mikeRestaurant.interfaces.Market;
import agent.Agent;

public class MarketRole extends Agent implements Market{
	
	//arbitrary inventory value
	private final static int MAX_FOOD_INVENTORY = 10;
	private final static int NO_FOOD_LEFT = 0;
	
	//simple ID to tell the markets apart
	private static int ID_COUNTER = 0;
	private int ID;
	
	private Semaphore marketBusy = new Semaphore(0, true);
	private Semaphore waitingForCashier = new Semaphore(0, true);
	
	private Map<String, Food> inventory;
	private CookRole cookRequestingFood;
	private CashierRole cashier;
	private Queue<Food> requests;
	
	private Map<String, Double> pricesForGoods;
	
	/**
	 * Constructor for the MarketAgent
	 * @param cook the cook to whom the Market responds (only one)
	 */
	public MarketRole(Cook cook, Cashier cashier) {
		inventory = new HashMap<String, Food>();
		requests = new ArrayDeque<Food>();
		
		//randomly add to the inventory of food
		for(String choice : WaiterRole.MENU().keySet()){
			inventory.put(choice, new Food(choice, (int)(Math.random()*MAX_FOOD_INVENTORY)) );
		}
		
		cookRequestingFood = (CookRole)cook;
		this.cashier = (CashierRole)cashier;	
		
		//how we distinguish between the separate Markets (they don't have names)
		ID_COUNTER++;  //iterate the ID so the next market will have a different value
		ID=ID_COUNTER;
		
		pricesForGoods = initPricesForGoods();
	}
	
	/**
	 * Standard accessor method
	 * @return the id of the MarketAgent, starts at #1
	 */
	public int getID(){
		return ID;
	}
	
	private Map<String, Double> initPricesForGoods(){
		Map<String,Double> temp = new HashMap<String, Double>();
		for(String s : WaiterRole.MENU().keySet()){
			switch(s){
				case "Steak":
					temp.put(s,Double.valueOf(10.00));
					break;
				case "Chicken":
					temp.put(s,Double.valueOf(6.00));
					break;
				case "Salad":
					temp.put(s,Double.valueOf(1.00));
					break;
				case "Pizza":
					temp.put(s,Double.valueOf(4.00));
					break;
			}
		}
		
		return temp;
	}

	//----------------------MESSAGES------------------------//

	/**
	 * Message sent by the CookAgent if he is ordering food
	 * @param choice
	 * @param quantity
	 */
	public void msgINeedFood(String choice, int quantity){
		requests.add(new Food(choice, quantity));
		stateChanged();
	}
	
	/**
	 * Message sent by the CashierAgent in response to a request for money
	 * @param name the item ordered
	 * @param quantity the quantity ordered
	 * @param bill the amount of money owed
	 * @param money the amount of money paid
	 */
	public void msgPaymentResponse(String name, int quantity, double bill, double money){
		/*for(Food f : requests){
			if(f.state == FoodState.WaitingForPayment && f.name.equals(name) && f.quantity == quantity){
				f.state = FoodState.Paid;
				waitingForCashier.release();
				break;
			}
		}*/
		waitingForCashier.release();
		
	}
	
	//----------------------SCHEDULER-----------------------//
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		//if there are requests, distribute the food appropriately
		
		try {
			if(!requests.isEmpty()){
				distributeFood(requests.poll());
				return true;
			}
			
			return false;
		} catch (ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		
	}
	
	//-----------------------ACTIONS-----------------------//
	
	/**
	 * Action called by the scheduler when the Market should be giving out its food to the cook
	 * @param request the request of the food to distribute 
	 */
	private void distributeFood(Food request){
		//if we are out of this type of food
		if(inventory.get(request.name) == null || inventory.get(request.name).quantity == 0){
			DoDistributeFood(new Food(request.name, NO_FOOD_LEFT));
			cookRequestingFood.msgOrderWillNotBeFulfilled(request.name, this);
			cookRequestingFood.msgHereIsFoodFromMarket(request.name, NO_FOOD_LEFT, inventory.isEmpty(), this);
			return;
		}
		
		request.state = FoodState.Preparing;
		//if we have enough to supply the cook fully
		DeliveryTimerListener listener;
		if(inventory.get(request.name).quantity > request.quantity){
			listener = new DeliveryTimerListener(request, true, this);
			//cookRequestingFood.msgHereIsFoodFromMarket(request.name, request.quantity, true, this);
			inventory.get(request.name).quantity -= request.quantity;
			
			DoAskForFoodPayment(request, request.quantity);
			cashier.msgAskForPayment(request.name, request.quantity, request.quantity*pricesForGoods.get(request.name), this);
		}else{//if we have to give him the rest of our food, but can give him some food
			//initially alert the cook that the order won't be filled completely
			//so faulty orders don't happen in the delay between order and delivery
			cookRequestingFood.msgOrderWillNotBeFulfilled(request.name, this);
			int amountAbleToProvide = inventory.get(request.name).quantity;
			DoAskForFoodPayment(request, amountAbleToProvide);
			cashier.msgAskForPayment(request.name, amountAbleToProvide, amountAbleToProvide*pricesForGoods.get(request.name), this);
			listener = new DeliveryTimerListener(new Food(request.name, amountAbleToProvide), inventory.isEmpty(), this);
			inventory.remove(request.name); //since we are out of this type of food, remove it from the list
			//if we are completely out of food, let the cook know
			//cookRequestingFood.msgHereIsFoodFromMarket(request.name, request.quantity, inventory.isEmpty(), this);
		}
		
		//wait for the Cashier to pay
		try {
			waitingForCashier.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Timer timer = new Timer(5000+(int)Math.random()*5000, listener);
		timer.start();
		
		//sleep the thread while the market is busy
		try {
			marketBusy.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//---------------------DO XYZ----------------------//
	
	private void DoDistributeFood(Food request){
		print("Delivering "+request.quantity+"x"+request.name+" to the cook");
	}
	
	private void DoAskForFoodPayment(Food request, int quantityProvided){
		print("Requesting Cashier for Payment for "+quantityProvided+"x"+request.name);
	}
	
	public enum FoodState {NewOrder, WaitingForPayment, Paid, Preparing};
	
	/**
	 * Class meant to couple data for requests, containing the name
	 * of the food and how many are desired
	 */
	private static class Food {
		String name;
		int quantity;
		FoodState state;
		
		/**
		 * Standard constructor for the food class
		 * @param name name of the food
		 * @param quantity how many are requested/on-hand
		 */
		public Food(String name, int quantity){
			this.name = name;
			this.quantity = quantity;
			state = FoodState.NewOrder;
		}
	}
	
	//---------------------UTILITIES--------------------//
	
	public String getName(){
		return "Market #"+ID;
	}
	
	/**
	 * Hack that clears the inventory of this MarketAgent
	 */
	public void clearInventory(){
		for(String s : inventory.keySet()){
			inventory.get(s).quantity = 0;
		}
	}
	
	/**
	 * Class that handles the delivery of food to the cook, which
	 * happens on a timer
	 * @author MSILKJR
	 */
	private class DeliveryTimerListener implements ActionListener {
		
		private Food request;
		private boolean isEmpty;
		private MarketRole mkt;
		
		/**
		 * Standard constructor for a DeliveryTimeListener
		 * @param request the Food object containing the request
		 * @param isEmpty whether or not the market is out of food altogether
		 * @param mkt this MarketAgent that is sending the food
		 */
		public DeliveryTimerListener(Food request, boolean isEmpty, MarketRole mkt){
			this.request = request;
			this.isEmpty = isEmpty;
			this.mkt = mkt;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			((Timer) e.getSource()).stop();
			DoDistributeFood(request);
			cookRequestingFood.msgHereIsFoodFromMarket(request.name, request.quantity, isEmpty, mkt);
			marketBusy.release();
		}
	}

}
