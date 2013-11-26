package restaurant;

public class Menu
{
	private Dish[] dishes = new Dish[3];

	public Menu() {
		dishes[0] = new Dish("Steak", 7000, 15.99);
		dishes[1] = new Dish("Chicken", 6000, 10.99);
		dishes[2] = new Dish("Burger", 4000, 8.99);
	}
	
	public String getDishName(int choice){
		return dishes[choice].getName();
	}
	
	public double getDishPrice(int choice){
		return dishes[choice].getPrice();
	}
	
	public class Dish {
		private String name;
		private int cookTime;
		private double price;
		
		public Dish(String name, int cookTime, double price) {
			this.name = name;
			this.cookTime = cookTime;
			this.price = price;
		}
		
		public String getName(){
			return name;
		}
		public int cookTime(){
			return cookTime;
		}
		public double getPrice(){
			return price;
		}
	}
	
}