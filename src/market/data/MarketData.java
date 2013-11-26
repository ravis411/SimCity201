package market.data;

import java.util.ArrayList;
import java.util.List;

import market.interfaces.MarketManager;

public class MarketData {
	List<Inventory> marketInventory	= new ArrayList<Inventory>();
	MarketManager currentMarketManager;
	List<Integer> numberOfCustomersInALine	= new ArrayList<Integer>();
	double marketMoney=0;
	public MarketData(){
		marketInventory.add(new Inventory("Steak",1));
		marketInventory.add(new Inventory("Chicken",1));
		marketInventory.add(new Inventory("Burger",1));
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
	public int getNumberOfCustomersInALine(int linenumber){
		return numberOfCustomersInALine.get(linenumber).intValue();
	}
	public void incrementNumerOfCustomersInALine(int linenumber) {
		numberOfCustomersInALine.add(linenumber, numberOfCustomersInALine.get(linenumber).intValue()+1);
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



}