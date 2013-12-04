package kushrestaurant;
import agent.Agent;
//import restaurant.Order2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import kushrestaurant.CookAgent.Food;
import kushrestaurant.CookAgent.Order;
import kushrestaurant.CookAgent.orderstate;
import kushrestaurant.CookAgent.state;
import kushrestaurant.HostAgent.Table;
import kushrestaurant.interfaces.*;
public class MarketAgent extends Agent implements Market{
	private Cook cook;
	public String name;
	enum MarketStatus {received,delivering,incomplete,hasnothing,delivered,incompletedelivery,nil};
	private Map<String, Integer> foods = new HashMap<String, Integer>();
	private Timer timer = new Timer();
    private Order2 order;
    public CashierAgent cashier;
    
public MarketAgent(String name,CashierAgent c) {
	super();
	this.name = name;
	this.cashier=c;
	if(name.equals("M1")){
	foods.put("Steak",new Integer(0));
	foods.put("Chicken",new Integer(0));
	foods.put("Pizza",new Integer(0));
	foods.put("Salad",new Integer(0));
	}
	
	if(name.equals("M2")){
		foods.put("Steak",new Integer(1));
		foods.put("Chicken",new Integer(10));
		foods.put("Pizza",new Integer(2));
		foods.put("Salad",new Integer(0));
		}
	if(name.equals("M3")){
		foods.put("Steak",new Integer(10));
		foods.put("Chicken",new Integer(10));
		foods.put("Pizza",new Integer(10));
		foods.put("Salad",new Integer(10));
		}
	order=new Order2();
	
	
	
}
//map<String,Food> foods;



  
//List<WaiterAgent> waiters;

public void msgReceivedOrderFromCook(HashMap<String,Integer> hm,Cook c){
	print("Received order from cook");
	Order2 o=new Order2(hm,c);
	this.cook=c;
	this.order=o;
	
	order.status= MarketStatus.received;
	stateChanged();
	
}
public void msgHereIsPayment(int bill){
	print("Received payment of " + bill+ " from cashier");
}
public void msgNoPayment(){
	print("Didnt receive a payment as didnt supply anything");
}
public void msgHereIsGiftCard(double val,double payment){
	print("Received gift card worth "+ val+ " which was the difference as cashier paid "+ payment);
}
public void ReStocking()
{
	
	for(Entry<String, Integer> orderlist: order.orders.entrySet()){
		
		if(foods.get(orderlist.getKey()) < order.orders.get(orderlist.getKey()))
		{
			order.restockings.put(orderlist.getKey(), foods.get(orderlist.getKey()));
			foods.put(orderlist.getKey(), new Integer(0));
			order.status = MarketStatus.incomplete;
		}
		else
		{
			Integer newValue = new Integer(foods.get(orderlist.getKey()) - orderlist.getValue());
			order.restockings.put(orderlist.getKey(), orderlist.getValue());
			foods.put(orderlist.getKey(), newValue);
			
		}
	}
	
	//if cannot complete
	int count = 0;
	for(Entry<String, Integer> item : order.restockings.entrySet())
	{	
		if(item.getValue().intValue() == 0){count++;}
	}
	if(count == order.restockings.size()){ order.status = MarketStatus.hasnothing;}
	stateChanged();
}
public void ReStock(){
	print("Restocking now, giving bill worth " + order.getBill()+ " to cashier");
	cashier.msgHereIsBill(this,order.getBill());
	timer.schedule(new TimerTask(){
			public void run(){
		cook.msgMarketReStock(order);
		
		stateChanged();
	}
	},5000);
	
	
}
public void cantReStock()
{
	print("couldnt fulfill order, giving bill to cashier");
	cook.msgCantRestock();
	cashier.msgHereIsBill(this,order.getBill());
	stateChanged();
}
protected boolean pickAndExecuteAnAction() {
	/* Think of this next rule as:
        Does there exist a table and customer,
        so that table is unoccupied and customer is waiting.
        If so seat him at the table.
	 */
	if(order.status== MarketStatus.received){
		
		order.status=MarketStatus.delivering;
		ReStocking();
		return true;
		
	}
	if(order.status==MarketStatus.hasnothing){
		cantReStock();
		order.status=MarketStatus.nil;
		return true;
	}
	if(order.status==MarketStatus.delivering || order.status==MarketStatus.incomplete ){
		if(order.status==MarketStatus.incomplete){
			order.status=MarketStatus.incompletedelivery;
		
		}
		else order.status=MarketStatus.delivered;
		ReStock();
		return true;
	}

	return false;
}
public String getName(){
	return name;
}

public class Order2{
    Order2(){
    	this.status= MarketStatus.nil;
    }
	public HashMap <String,Integer> orders;
	public HashMap<String,Integer> restockings= new HashMap<String,Integer>();
	
	public int getBill(){
		int b=0;
		for(Entry<String, Integer> orderlist: restockings.entrySet()){
			String c=orderlist.getKey();
			switch(c){
			case "Chicken":b=b+(orderlist.getValue()*2);
			case "Salad":b=b+(orderlist.getValue()*1);
			case "Steak":b=b+(orderlist.getValue()*4);
			case "Pizza":b=b+(orderlist.getValue()*3);
			default: b=b;
			}
		}
		return b;
		
	}
	MarketStatus status;
	private Cook cook;
	Order2(HashMap<String, Integer> o,Cook c){
		orders=o;
		status=MarketStatus.nil;
	}
	
	
	}




}
