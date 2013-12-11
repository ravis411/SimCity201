package kushrestaurant;
import interfaces.generic_interfaces.GenericCook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.*;

import javax.swing.Timer;

import building.BuildingList;
import building.Market;
import kushrestaurant.HostRole.Table;
import kushrestaurant.gui.CookGui;
//import kushrestaurant.gui.RestaurantGui;
import kushrestaurant.interfaces.Cook;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Waiter;
import MarketEmployee.MarketManagerRole;

import Person.Role.RoleState;
import Person.Role.ShiftTime;

public class CookRole extends GenericCook implements Cook {
        List<Order> orders= new ArrayList<Order>();
        public String name;
        private CookGui cookGui=null;
        //private RestaurantGui gui;
        private int constant=1000;
        private List<MarketManagerRole> marketManagers= new ArrayList<MarketManagerRole>();
        private static Map<String, Food> foods = new HashMap<String, Food>();
        private java.util.Timer timer = new java.util.Timer();
        public List<Food> inventory = new ArrayList<Food>();
        private int count=0;
        private RevolvingStand revolvingStand;
        public static class Order{
                public Order(String choice,Waiter w2,Table t,Customer c){
                        this.choice=choice;
                        this.w=w2;
                        this.table=t;
                        s= orderstate.pending;
                        customer=c;
                        food=foods.get(choice);
                        
                }
                Food food;
                Customer customer;
                Waiter w;
                String choice;
                Table table;
                orderstate s;
        }

enum state {cooking,free,goinggroceryshopping};
state cookState;

enum orderstate {pending,cooking,done,served};
public int getCookTime(String choice){
        switch(choice){
        case "Chicken": return 4;
        case "Steak": return 6;
        case "Salad" : return 2;
        case "Pizza" : return 5;
        default: return 1;
        }
                
}
/*
public CookRole(String name) {
        super();
        //this.gui=gui;
        
        this.name = name;
        // make some tables
        //tables = new ArrayList<Table>(NTABLES);
        //for (int ix = 1; ix <= NTABLES; ix++) {
                //tables.add(new Table(ix));//how you add to a collections
        cookState= state.free;
        inventory.add(new Food("Steak",3));
        inventory.add(new Food("Chicken",0));
        inventory.add(new Food("Salad",0));
        inventory.add(new Food("Pizza",3));
        foods.put("Steak",inventory.get(0));
        foods.put("Chicken",inventory.get(1));
        foods.put("Pizza",inventory.get(3));
        foods.put("Salad",inventory.get(2));
        
        
}*/

public CookRole(String workLocation) {

        super(workLocation);
        //this.gui=gui;
        
        //this.name = name;
        // make some tables
        //tables = new ArrayList<Table>(NTABLES);
        //for (int ix = 1; ix <= NTABLES; ix++) {
                //tables.add(new Table(ix));//how you add to a collections
        cookState= state.free;
        inventory.add(new Food("Steak",3));
        inventory.add(new Food("Chicken",3));
        inventory.add(new Food("Salad",300));
        inventory.add(new Food("Pizza",300));
        foods.put("Steak",inventory.get(0));
        foods.put("Chicken",inventory.get(1));
        foods.put("Pizza",inventory.get(3));
        foods.put("Salad",inventory.get(2));
        revolvingStand= new RevolvingStand();

    javax.swing.Timer checkRevolvingStand = new javax.swing.Timer(15000, new ActionListener(){

          
            public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub        
                    stateChanged();
            }
            
    });
    
    checkRevolvingStand.start();
}
//map<String,Food> foods;
public class Food{
        String type;
        int cookingtime;
        public int amount;
        int capacity;
        public int threshold;
         Food (String food, int amount){
                this.type = food;
                //cookingtime=cookTime;
                this.amount=amount;
                threshold=1;
                capacity=10;
         }
        
        }

  
//List<WaiterAgent> waiters;
public void addMarket(MarketManagerRole mr){
	marketManagers.add(mr);
}
public void MsgHereisTheOrder(Waiter w, Customer c, Table t, String choice){
        print("Received order from " +w.getName());
    orders.add(new Order(choice,w,t,c));
        stateChanged();
        
}
public void setGui(CookGui c){this.cookGui=c;}
public void msgOrderFilled(int ingredientNum, int quantity){

    switch(ingredientNum) {      
    	case 0: foods.get("Steak").amount+= quantity;
    	print("Received complete delivery from market have "+ quantity + " steak");
    	case 1: foods.get("Chicken").amount+= quantity;
    	print("Received complete delivery from market have "+ quantity + " chicken");
        
}
    cookState=state.free;
    stateChanged();
}
public void msgOrderPartiallyFilled(int ingredientNum,int quantity, int quanityOfOrderMarketDoesntHave){
	switch(ingredientNum) {      
	case 0: foods.get("Steak").amount+= quantity;
	print("Received partial delivery from market have "+ quantity + " steak");
	case 1: foods.get("Chicken").amount+= quantity;
	print("Received partial delivery from market have "+ quantity + " chicken");
    
}
        cookState= state.goinggroceryshopping;
        stateChanged();
}
public void msgOrderNotFilled(int ingredientNum){
        cookState=state.goinggroceryshopping;
        stateChanged();
}
public void OrderFromMarket(){
		marketManagers.add((MarketManagerRole)((Market) BuildingList.findBuildingWithName("Market 1")).getManager());
        HashMap<String,Integer> order = new HashMap<String,Integer>();
        if(count==1){count=0;}
        print("ordering from "+ marketManagers.get(count).getNameOfRole());
        

        for(Food f:inventory ){
                if(f.threshold>=f.amount){
                
                        marketManagers.get(count).msgMarketManagerFoodOrder(f.type,f.capacity, this);
                        
                        
                }
                
        }
        //Order2 o= new Order2(order,this);
        
        count++;
        stateChanged();
        

}
public boolean pickAndExecuteAction() {
        /* Think of this next rule as:
        Does there exist a table and customer,
        so that table is unoccupied and customer is waiting.
        If so seat him at the table.
<<<<<<< HEAD
         */
	   if(this.workState==WorkState.ReadyToLeave && this.orders.size()==0){
		   this.cookState= state.free;
		   kill();
		   return true;
	   }
        if(cookState==state.goinggroceryshopping){
                if(count<marketManagers.size()){
                OrderFromMarket();
                cookState=state.free;
                return true;
                }
        }
        for(Order o: orders){
                if(o.s==orderstate.done){
                           OrderDone(o);
                           o.s=orderstate.served;
                            return true;
                            
                    }
                if(o.s== orderstate.pending){
                   CookIt(o);
                   return true;
            }
            

	if(cookState==state.goinggroceryshopping){
		if(count<marketManagers.size()){
		OrderFromMarket();
		cookState=state.free;
		return true;
		}
	}
	
   
        if(!revolvingStand.isEmpty()){
        Order order = revolvingStand.getLastOrder();
        Order newOrder = new Order(order.choice, order.w, order.table,order.customer);
        newOrder.s = orderstate.pending;
        orders.add(newOrder);
        CookIt(newOrder);
        return true;
}


                }
        return false;

}
public void CookIt(Order o){
        if(cookState==state.free)
                {
                if(o.food.amount>0)
                {
                        cookState=state.cooking;
                        o.s=orderstate.cooking;
                        print("cooking");
                        cookGui.cookFood(o.food.type);
                        doCooking(o);
                        
                }
                else
                {
                        print(o.food.type+" is out of stock");
                        o.w.msgDontHaveThis(o.choice,o.customer);
                        orders.remove(o);
                        count=0;
                        cookState=state.goinggroceryshopping;
                }
                }
        stateChanged();
}
public void doCooking(final Order o){

        int cookingtime=getCookTime(o.choice);
        
        timer.schedule(new TimerTask() {
                
                public void run() {
                        o.s=orderstate.done;
                        cookState=state.free;
                        cookGui.plateFood(o.choice);
                        stateChanged();
                }
        },
        constant*cookingtime);
        
        ;//getHungerLevel() * 1000);//how long to wait before running task

}

public void takenFood(String c){
        cookGui.doneFree(c);
}
public void OrderDone(final Order o){
        print(o.w.getName()+o.customer.getName()+"'s order done");
        
timer.schedule(new TimerTask() {
                
                public void run() {
                        o.w.msgGetFoodFromCook();
                        stateChanged();
                }
        },
        1000);
}

@Override
public boolean canGoGetFood() {
        // TODO Auto-generated method stub
        return false;
}
@Override
public String getNameOfRole() {
        // TODO Auto-generated method stub
        return "CookRole";
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

//@Override
//public void workplaceIsOpen() {
//        // TODO Auto-generated method stub
//        this.activate();
//        
//}

@Override
public RevolvingStand getRevolvingStand() {
        // TODO Auto-generated method stub
        return revolvingStand;
}

@Override
public void msgCantRestock() {

        // TODO Auto-generated method stub
        
}





}
