package kushrestaurant;
import interfaces.generic_interfaces.GenericCashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


import Person.Role.RoleState;

import restaurant.interfaces.luca.LucaMarket;

import Person.Role.ShiftTime;
import kushrestaurant.interfaces.Cashier;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Market;
import kushrestaurant.interfaces.Waiter;

public class CashierRole extends GenericCashier implements Cashier{
	
	public String name;
	private int money=100;
	public double bill=0;
	private Map<String, Double> foods = new HashMap<String, Double>();
	public ArrayList<customer> customers= new ArrayList<customer>();
	private Timer timer = new Timer();
	public HashMap <Integer,Integer> givenGiftCards= new HashMap<Integer,Integer>();
	public enum CState {checkNotReady,checkReady,paid,ChangeReady,Punish,Unpaid,MadeGood};
	private ArrayList<Bill> bills= new ArrayList<Bill>();
	int i=1;
	//public List<Food> inventory = new ArrayList<Food>();
	public class customer{
		Customer c;
		String choice;
		public double check;
		double cash;
		public double change;
		Waiter w;
		public CState state= CState.checkNotReady;
		customer(Customer c,String choice,Waiter w){
			this.w=w;
			this.c=c;
			this.choice=choice;
			//check=foods.get(choice);
		}
		public void setCheck(){
			check=foods.get(choice);
			//bill=check;
		}
	}
	public class Bill{
		int bill;
		LucaMarket m;
		public CState state=CState.Unpaid;
		Bill(int b, LucaMarket m){
			bill=b;
			this.m=m;
		}
	}
enum CashierState {busy,free};
CashierState cashierState= CashierState.free;
public double icheck=10.99;

public CashierRole(String workLocation){
super(workLocation);
    
	
	// make some tables
	//tables = new ArrayList<Table>(NTABLES);
	//for (int ix = 1; ix <= NTABLES; ix++) {
		//tables.add(new Table(ix));//how you add to a collections
	cashierState= CashierState.free;
	foods.put("Steak",15.99);
	foods.put("Chicken",10.99);
	foods.put("Salad",5.99);
	foods.put("Pizza",8.99);
}
/*
public CashierRole(String name) {
	//super();
    
	this.name = name;
	// make some tables
	//tables = new ArrayList<Table>(NTABLES);
	//for (int ix = 1; ix <= NTABLES; ix++) {
		//tables.add(new Table(ix));//how you add to a collections
	cashierState= CashierState.free;
	foods.put("Steak",15.99);
	foods.put("Chicken",10.99);
	foods.put("Salad",5.99);
	foods.put("Pizza",8.99);
	
}*/

//map<String,Food> foods;
public void msgProduceCheck(Customer customer,String choice,Waiter waiter){
	print("Producing check");
	customers.add(new customer(customer,choice,waiter));
	stateChanged();
}
public double getMoney(){return money;}
public void msgHereIsPayment(Customer cust,double check, double cash){
	for(customer c:customers){
		if(cash>=check)
			{if(c.c==cust){
			 c.change=cash-check;
			 c.state=CState.ChangeReady;
			 }
			}
		else 
		{
				if(c.c==cust){
					 //c.change=cash-check;
					 c.state=CState.Punish;
			}
			}
			stateChanged();
		}
	}
public void msgCashierHereIsMarketBill( int bill, LucaMarket market){
	bills.add(new Bill(bill,market));
	stateChanged();
}
//List<WaiterAgent> waiters;


public double getBill(){
	return bill;
}
public ArrayList<Bill> getListOfBills(){return bills;}
public boolean pickAndExecuteAction() {
	/* Think of this next rule as:
        Does there exist a table and customer,
        so that table is unoccupied and customer is waiting.
        If so seat him at the table.
	 */if(this.workState==WorkState.ReadyToLeave){
		 kill();
		 return true;
	 }
	for (Bill b:bills){
		if(b.state==CState.Unpaid){
			
			PayMarketBill(b);
			//bills.remove(b);
			return true;
			
		}
		
	}
	for(customer c:customers){
		if(c.state==CState.checkNotReady){
			c.setCheck();
			c.state=CState.checkReady;
			HereIsCheck(c);
			return true;
		}
	}
		for(customer c:customers){
			if(c.state==CState.ChangeReady){
				
				ChangeReady(c.c,c.change);
				c.state=CState.paid;
				return true;
			}
		}
			for(customer c:customers){
				if(c.state==CState.Punish){
					
					PunishCustomer(c.c);
					c.state=CState.paid;
					return true;
				}
			}
	
	return false;
}
public void PayMarketBill(Bill b){
	
	print("Paying bill of "+b.bill+" to market "+ b.m.getName()+" left with " + (money-b.bill));
	    b.m.msgMarketHereIsPayment(b.bill);
	    money=money-b.bill;
	    b.state=CState.paid;
}
public void ChangeReady(Customer c, double change){
   print("Heres your change "+change);
	c.msgHereIsYourChange(change);
}
public void PunishCustomer(Customer c){
	print("Punishing this guy, he has to wash dishes "+c.getName());
	c.msgWashDishes();
}
public void HereIsCheck(customer c){
	print("Give this check of"+c.check+"to "+c.c.getName());
	c.w.msgHereIsCheck(c.c,c.check);
	
}

@Override
public boolean canGoGetFood() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public String getNameOfRole() {
	// TODO Auto-generated method stub
	return "CashierRole";
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
public void msgHereIsBill(Market m, int bill) {
	// TODO Auto-generated method stub
	
}

//@Override
//public void workplaceIsOpen() {
//	// TODO Auto-generated method stub
//	this.activate();
//	
//}



}
