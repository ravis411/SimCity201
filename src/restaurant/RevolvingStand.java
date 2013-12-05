package restaurant;

import interfaces.Customer;
import interfaces.Waiter;

import java.util.ArrayDeque;
import java.util.Queue;

public class RevolvingStand {

	private static RevolvingStand instance = null;
	private final int CAPACITY = 5;
	
	private Queue<Order> incomingOrders;
	
	protected RevolvingStand(){
		incomingOrders = new ArrayDeque<Order>();
	}
	
	public static RevolvingStand getInstance(){
		if(instance == null){
			instance = new RevolvingStand();
		}
		
		return instance;
	}
	
	public synchronized void addIncomingOrder(Waiter w, int tableNumber, int item, Customer c){
		if(incomingOrders.size() == CAPACITY){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		incomingOrders.add(new Order(w, tableNumber, item, c));
		notify();
	}
	
	public synchronized boolean isEmpty(){
		return incomingOrders.isEmpty();
	}
	
	public synchronized boolean isFull(){
		return incomingOrders.size() == CAPACITY;
	}
	
	public synchronized Order popOrder(){
		if(incomingOrders.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Order order = incomingOrders.poll();
		notify();
		return order;
	}
	

	
}
