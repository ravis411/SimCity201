package market.data;

import java.util.ArrayList;
import java.util.List;

public class Shelves {
	List<Inventory> marketInventory	= new ArrayList<Inventory>();
	public Shelves(){
		marketInventory.add(new Inventory("Steak",1));
		marketInventory.add(new Inventory("Chicken",1));
		marketInventory.add(new Inventory("Burger",1));
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
	
	public void decrementFoodAmount(int inventoryListNumber, int amountToDecrement){
		marketInventory.get(inventoryListNumber).decrementFoodAmount(amountToDecrement);
	}
	public void restockFoodAmount(int inventoryListNumber, int amountToRestock){
		marketInventory.get(inventoryListNumber).restockFoodAmount(amountToRestock);
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