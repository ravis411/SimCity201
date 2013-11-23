package bank.gui;

import bank.*;
import astar.*;

import java.util.concurrent.*; 

import javax.swing.*;

import java.awt.*;
import java.util.Vector;


/** Panel in frame that contains all the bank information,
 * including tellers and waiters */
public class bankPanel extends JPanel {
	//create animation
	static int gridX = 20;
	static int gridY = 15;

	//**Decide how many lines to have
	private int nLines = 5;

	//I'm going to address the grid using real coordinates, not 0-based
	//ones. i.e. grid(1,1) has x=1, y=1 and is the "first" square on
	//the layout. I'll waste a row and column
	Semaphore[][] grid = new Semaphore[gridX+1][gridY+1]; 
	//Line[] lines = new Line[nLines];
	Line[] lines = new Line[gridX * gridY];

	Bank bank =  new Bank("Welcome to csci201's Bank",
			gridX, gridY, grid, lines);

	//Clients and Tellers
	private Vector<bankClientRole> customers = new Vector<bankClientRole>();
	private Vector<bankTellerRole> waiters = new Vector<bankTellerRole>();

	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private ListPanel waiterPanel = new ListPanel(this, "Waiters");
	private JPanel group = new JPanel();

	private bankGui gui; //reference to main gui

	public bankPanel(bankGui gui){
		this.gui = gui;

		//intialize the semaphore grid
		for (int i=0; i<gridX+1 ; i++)
			for (int j = 0; j<gridY+1; j++)
				grid[i][j]=new Semaphore(1,true);
		//build the animation areas
		try {
			//make the 0-th row and column unavailable
			System.out.println("making row 0 and col 0 unavailable.");
			for (int i=0; i<gridY+1; i++) grid[0][0+i].acquire();
			for (int i=1; i<gridX+1; i++) grid[0+i][0].acquire();

			System.out.println("adding wait area");
			bank.addWaitArea(2, 2, 13);
			for (int i=0; i<13; i++) grid[2][2+i].acquire();

			System.out.println("adding counter area");
			bank.addCounter(17, 2, 13);
			for (int i=0; i<13; i++) grid[17][2+i].acquire();

			System.out.println("adding grill area");
			bank.addGrill(19, 3, 10);
			for (int i=0; i<10; i++) grid[19][3+i].acquire();
			//Let's just put the four static lines in for now

			System.out.println("adding line 1");
			lines[0] = new Line("T1", 5, 3, 3);//, bank);
			bank.addLine("T1", 5, 3, 3);
			for (int i=0; i<3; i++)
				for (int j=0; j<3; j++)
					grid[5+i][3+j].acquire();// because grid is 0-based

			System.out.println("adding line 2");
			lines[1] = new Line("T2", 5, 8, 3);//, bank);
			bank.addLine("T2", 5, 8, 3);
			for (int i=0; i<3; i++)
				for (int j=0; j<3; j++)
					grid[5+i][8+j].acquire();// because grid is 0-based

			System.out.println("adding line 3");
			lines[2] = new Line("T3", 10, 3, 3);//,bank);
			bank.addLine("T3", 10, 3, 3);
			for (int i=0; i<3; i++)
				for (int j=0; j<3; j++)
					grid[10+i][3+j].acquire();// because grid is 0-based

			System.out.println("adding line 4");
			lines[3] = new Line ("T4", 10, 8, 3);//,bank);
			bank.addLine("T4", 10, 8, 3);
			for (int i=0; i<3; i++)
				for (int j=0; j<3; j++)
					grid[10+i][8+j].acquire();// because grid is 0-based
		}catch (Exception e) {
			System.out.println("Unexpected exception caught in during setup:"+ e);
		}
		bank.setAnimDelay(500);
		bank.displayBank();

		setLayout(new GridLayout(1,2, 20,20));
		group.setLayout(new GridLayout(1,2, 10,10));

		group.add(waiterPanel);
		group.add(customerPanel);

		initRestLabel();
		add(restLabel);
		add(group);
	}

	
	public bankPanel(bankLayout layout) {
		// TODO Auto-generated constructor stub
	}



	private void initRestLabel(){
	JLabel label = new JLabel();
	//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
	restLabel.setLayout(new BorderLayout());
	restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
	restLabel.add(label, BorderLayout.CENTER);
	restLabel.add(new JLabel("               "), BorderLayout.EAST );
	restLabel.add(new JLabel("               "), BorderLayout.WEST );
    }


	/** When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information 
	 * will be shown
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person*/
	public void showInfo(String type, String name){

		if(type.equals("Customers")){

			for(int i=0; i < customers.size(); i++){
				bankClientRole temp = customers.get(i);
				if(temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}else if(type.equals("Waiters")){
			for(int i=0; i < waiters.size(); i++){
				bankTellerRole temp = waiters.get(i);
				if(temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
	}

	/** Adds a customer or waiter to the appropriate list
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person */
	public void addPerson(String type, String name){

		if(type.equals("Customers")){
			bankClientRole c = new bankClientRole(name, gui, bank);
			customers.add(c);
			c.startThread(); //Customer is fsm.
		} else if(type.equals("Waiters")){
			AStarTraversal aStarTraversal = new AStarTraversal(grid);
			bankTellerRole w = new bankTellerRole(name, aStarTraversal, bank, lines);
			waiters.add(w);
			w.startThread();
		}
	}	

	public void addLine() {
		int size = 3;
		System.out.println("adding line " + (nLines + 1));
		for(int i = 1; i <= gridX - size; i++) {
			for(int j = 1; j <= gridY - size; j++) {
				if(addLine(i, j, size)) {
					System.out.println("Added line " + nLines);
					return;
				}
			}
		}
		System.out.println("Cannot add line " + (nLines + 1));
	}

	public boolean addLine(int x, int y, int size)
	{
		try
		{
			int acqCnt = -1;
			int[][] acqList = new int[9][2];
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					boolean acquired = grid[x+i][y+j].tryAcquire();
					if(acquired) {
						acqCnt++;
						acqList[acqCnt][0] = x+i;
						acqList[acqCnt][1] = y+j;
					}
					if(!acquired) {
						for(int k=0; k<=acqCnt; k++) {
							grid[acqList[k][0]][acqList[k][1]].release();
						}
						return false;
					}
				}
			}
			lines[nLines] = new Line("T" + (nLines+1), x, y, size);//,bank);
			bank.addLine("T" + (nLines+1), x, y, size);
			nLines++;
		}
		catch (Exception e)
		{
			System.out.println("Unexpected exception caught in during setup:"+ e);
		}
		return true;
	}
}
