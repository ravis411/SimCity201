package market.data;

import interfaces.MarketEmployee;
import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCook;

import java.util.ArrayList;
import java.util.List;

public class MarketData {
	String marketName;
	List<Inventory> marketInventory	= new ArrayList<Inventory>();
	List<Order> orders	= new ArrayList<Order>();
	MarketManager currentMarketManager;
	MarketEmployee currentMarketEmployee1=null;
	MarketEmployee currentMarketEmployee2=null;
	MarketEmployee currentMarketEmployee3=null;
	List<Integer> numberOfCustomersInALine	= new ArrayList<Integer>();
	double marketMoney=0;
	public MarketData(){
		marketInventory.add(new Inventory("Steak",97));
		marketInventory.add(new Inventory("Chicken",97));
		marketInventory.add(new Inventory("Burger",97));
		numberOfCustomersInALine.add(new Integer(0));//line 1
		numberOfCustomersInALine.add(new Integer(0));//line 2
		numberOfCustomersInALine.add(new Integer(0));//line 3
	}
	public int size(){
		return marketInventory.size();
	}
	/**
	 * returns Foodtype given number in Arraylist
	 *
	 */
	public String getFoodType(int inventoryListNumber){
		return marketInventory.get(inventoryListNumber).getFoodType();
	}
	/**
	 * returns Amount Ordered given number in Arraylist
	 *
	 */
	public int getAmount(int inventoryListNumber){
		return marketInventory.get(inventoryListNumber).getAmount();
	}
	public void giveMarketMoney(double moneyAmount){
		marketMoney +=moneyAmount;
	}
	public double getMarketMoney(){
		return marketMoney;
	}
	public void decrementFoodAmount(int inventoryListNumber, int amountToDecrement){
		marketInventory.get(inventoryListNumber).decrementFoodAmount(amountToDecrement);
	}
	public void restockFoodAmount(int inventoryListNumber, int amountToRestock){
		marketInventory.get(inventoryListNumber).restockFoodAmount(amountToRestock);
	}
	public void setMarketManager(MarketManager marketManager){
		currentMarketManager=marketManager;
	}
	public MarketManager getMarketManager(){
		return currentMarketManager;
	}
	public void setMarketEmployeeAtCounter1(MarketEmployee marketEmployee){
		currentMarketEmployee1=marketEmployee;
	}
	public MarketEmployee getMarketEmployeeAtCounter1(){
		return currentMarketEmployee1;
	}
	public void setMarketEmployeeAtCounter2(MarketEmployee marketEmployee){
		currentMarketEmployee2=marketEmployee;
	}
	public MarketEmployee getMarketEmployeeAtCounter2(){
		return currentMarketEmployee2;
	}
	public void setMarketEmployeeAtCounter3(MarketEmployee marketEmployee){
		currentMarketEmployee3=marketEmployee;
	}
	public MarketEmployee getMarketEmployeeAtCounter3(){
		return currentMarketEmployee3;
	}
	public int getNumberOfCustomersInALine(int linenumber){
		return numberOfCustomersInALine.get(linenumber).intValue();
	}
	public void incrementNumerOfCustomersInALine(int linenumber) {
		numberOfCustomersInALine.add(linenumber, numberOfCustomersInALine.get(linenumber).intValue()+1);
	}
	
	public String getName() {
		return marketName;
	}
	public void msgClosedMarketAFoodOrder(String choice, int howMuchFoodAgentAsksFromMarket, GenericCook CookRole) {
		
		orders.add(new Order(choice, howMuchFoodAgentAsksFromMarket, CookRole));
		
	}
	public synchronized Order getLastOrder(){
		if(orders.size() != 0){
			return orders.remove(0);
		}
		return null;
	}
	public boolean anyPendingOrders(){
		if (orders.size()!=0){
			return true;
		}
		else
			return false;
	}
private class Inventory {
String foodType;
int amount;
Inventory(String foodType, int amount){
	this.foodType=foodType;
	this.amount=amount;
}
private int getAmount() {
	return amount;
	
}
private String getFoodType() {
	return foodType;
}
private void decrementFoodAmount(int amountToDecrement) {
	amount -=amountToDecrement;
}
private void restockFoodAmount(int amountToRestock) {
	amount +=amountToRestock;
}
}

public class Order{
	String choice;
	int amount;
	GenericCook cookRole;
	Order(String choice,int howMuchFoodAgentAsksFromMarket, GenericCook lucaCookRole){
		this.choice=choice;
		this.amount=howMuchFoodAgentAsksFromMarket;
		cookRole=lucaCookRole;
				
	}
	public String getChoice(){
		return choice;
	}
	public int getAmount(){
		return amount;
	}
	public GenericCook getCook(){
		return cookRole;
	}
}





}