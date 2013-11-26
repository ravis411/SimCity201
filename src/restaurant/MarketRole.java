package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.CookAgent.AgentState;
import restaurant.CustomerAgent.AgentEvent;
import agent.Agent;
import java.util.Random;
import restaurant.interfaces.*;

/**
 * Restaurant Market Agent
 */

public class MarketAgent extends Agent implements Market {
	public Ingredient[] ingredients = new Ingredient[4];
	
	String name;
	CookAgent cook;
	Cashier cashier;
	private int currentRequest;
	private int currentRequestQuantity;
	Timer timer = new Timer();
	
	public enum AgentState
	{DoingNothing, RespondingToCook, SellingFood};
	private AgentState state = AgentState.DoingNothing;//The start state
	
	public MarketAgent(String name, CookAgent cook, Cashier cashier) {
		super();
		
		Random randNum = new Random();
		
		this.name = name;
		this.cook = cook;
		this.cashier = cashier;
		
		ingredients[0] = new Ingredient("Steak", randNum.nextInt(3), 6.99);
		ingredients[1] = new Ingredient("Chicken", randNum.nextInt(3), 4.99);
		ingredients[2] = new Ingredient("Salad", randNum.nextInt(3), 2.99);
		ingredients[3] = new Ingredient("Pizza", randNum.nextInt(3), 2.99);
	}
	
	public String getName() {
		return name;
	}
	
	//Messages
	public void msgNeedFood(int ingredientNum, int quantity) {
		print("Cook is requesting " + quantity + " " + ingredients[ingredientNum].getName() + "s.");
		currentRequest = ingredientNum;
		currentRequestQuantity = quantity;
		state = AgentState.RespondingToCook;
		stateChanged();
	}
	public void msgReceivePayment(double amount) {
		print("Recieved $" + amount + " from restaurant's cashier.");
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	protected boolean pickAndExecuteAnAction() {
		if(state == AgentState.RespondingToCook && ingredients[currentRequest].getQuantity() >= currentRequestQuantity) {
			fulfillOrder();
			return true;
		}
		else if(state == AgentState.RespondingToCook && ingredients[currentRequest].getQuantity() < currentRequestQuantity && ingredients[currentRequest].getQuantity() > 0) {
			fulfillPartialOrder();
			return true;
		}
		else if(state == AgentState.RespondingToCook && ingredients[currentRequest].getQuantity() == 0) {
			cannotFulfillOrder();
			return true;
		}
		return false;
	}
	
	private void fulfillOrder() {
		final MarketAgent temp = this;
		print("I have " + ingredients[currentRequest].quantity + " " + ingredients[currentRequest].getName() + "s. Enough to fill the cook's order.");
		timer.schedule(new TimerTask() {
			public void run() {
				cook.msgOrderFilled(currentRequest, ingredients[currentRequest].quantity);
				cashier.msgMarketBill(temp, ingredients[currentRequest].quantity*ingredients[currentRequest].price);
				ingredients[currentRequest].quantity = ingredients[currentRequest].quantity-currentRequestQuantity;
				state = AgentState.DoingNothing;
				stateChanged();
			}
		},
		9000);
		state = AgentState.SellingFood;
	}
	
	private void fulfillPartialOrder() {
		final MarketAgent temp = this;
		print("I have " + ingredients[currentRequest].quantity + " " + ingredients[currentRequest].getName() + "s. Enough to fill only part of the cook's order.");
		timer.schedule(new TimerTask() {
			public void run() {
				cook.msgOrderPartiallyFilled(currentRequest, ingredients[currentRequest].quantity);
				cashier.msgMarketBill(temp, ingredients[currentRequest].quantity*ingredients[currentRequest].price);
				ingredients[currentRequest].quantity = ingredients[currentRequest].quantity-currentRequestQuantity;
				state = AgentState.DoingNothing;
				stateChanged();
			}
		},
		9000);
		state = AgentState.SellingFood;
	}
	
	private void cannotFulfillOrder() {
		print("I am out of " + ingredients[currentRequest].getName() + ". You'll have to go to a different market.");
		cook.msgOrderNotFilled(currentRequest);
		state = AgentState.DoingNothing;
	}
	
	private class Ingredient {
		private String name;
		private int quantity;
		private double price;
		
		Ingredient(String name, int quantity, double price) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
		}
		
		public String getName() {
			return name;
		}
		public int getQuantity() {
			return quantity;
		}
	}
}