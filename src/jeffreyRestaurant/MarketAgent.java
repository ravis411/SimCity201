package jeffreyRestaurant;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.util.HashMap;

import jeffreyRestaurant.Gui.HostGui;
import jeffreyRestaurant.interfaces.Market;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MarketAgent extends Agent implements Market{
	//Data
	public MarketAgent(String Name, CookAgent cook, CashierAgent cashier, int I1, int I2, int I3, int I4) {
		name = Name;
		ck = cook;
		this.cashier = cashier;
		print("New market created");
		//Foods
		foods.put("Steak", new food("Steak", I1, 10.00));
		foods.put("Chicken", new food ("Chicken", I2, 5.00));
		foods.put("Salad", new food("Salad", I3, 2.00));
		foods.put("Pizza", new food("Pizza", I4, 6.00));
	}
	private class food {
		food(String name, int q, Double price) {
			type = name;
			quantity = q;
			unitPrice = price;
		}
		String type;
		int quantity;
		Double unitPrice;
	}
	private class foodOrder {
		foodOrder(String f, int q, orderState s) {
			food = f;
			quantity = q;
			state = s;
			price = foods.get(food).quantity*foods.get(food).unitPrice;
		}
		String food;
		int quantity;
		orderState state;
		Double price;
	}
	private String name;
	private enum orderState {received, pending, fulfilled, payRequested, paidFor};
	
	private CookAgent ck;
	private CashierAgent cashier;
	
	private Map<String, food> foods = new HashMap<String, food>();
	private List<foodOrder> orders = Collections.synchronizedList(new ArrayList<foodOrder>());
	//Fill map in class constructor
	
	
	public String getName() {
		return name;
	}
	
	public int getFoodNum(String food) {
		return foods.get(food).quantity;
	}
	
	//Messages
	
	public void msgNeedFood(String food, int quantity) {
		print("Cook needs food");
		orders.add(new foodOrder(food,quantity, orderState.received));
		stateChanged();
	}
	
	public void msgOrderPayment(String food, Double price) {
		print("Payment received from cashier");
		synchronized(orders) {
			for (foodOrder o : orders) {
				if (o.food.equals(food) && o.price == price) {
					o.state = orderState.paidFor;
				}
			}
		}
		stateChanged();
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		synchronized(orders) {
			for (foodOrder o : orders) {
				if (o.state == orderState.received) {
					o.state = orderState.pending;
					tryFillOrder(o);
				}
			}
			for (foodOrder o : orders) {
				if (o.state == orderState.fulfilled) {
					askForPayment(o);
				}
			}
		}
		
		
		
		return false;
	}
	
	//Actions
	private void tryFillOrder(foodOrder o) {
		if (foods.get(o.food).quantity > 0) {
			//Case 2: market can partially fulfill order
			if (foods.get(o.food).quantity - o.quantity < 0) {
				//ck.msgPartialMarketOrder(o.food, foods.get(o.food).quantity, this);
				foods.get(o.food).quantity = 0;
				o.state = orderState.fulfilled;
			}
			//Case 1: market can completely fulfill order
			else if (foods.get(o.food).quantity - o.quantity >= 0) {
				//ck.msgMarketOrder(o.food, o.quantity, this);
				foods.get(o.food).quantity -= o.quantity;
				o.state = orderState.fulfilled;
			}
		}
		//Case 3: market cannot fulfill order at all
		else if (foods.get(o.food).quantity == 0) {
			//ck.msgPartialMarketOrder(o.food, 0, this);
		}
	}
	
	private void askForPayment (foodOrder o) {
		o.state = orderState.payRequested;
		//cashier.msgPayForOrder(this, o.price, o.food);
	}
	
	
}

