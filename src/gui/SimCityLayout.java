package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.*;
import java.util.concurrent.Semaphore;

public class SimCityLayout {
	int numxGrids = 0;
	int numyGrids = 0;
	int GRID_SIZEX = 25;
	int GRID_SIZEY = 25;
	int WINDOWX = 900;
	int WINDOWY = 900;


	//A map of grid positions to Java coordinates
	public static final Map<Dimension, Dimension> positionMap = new HashMap<Dimension, Dimension>();

	//A list of the roads
	private List<Road> roads = new ArrayList<>();

	//The main Semaphore Grid
	private Semaphore[][] mainGrid = null;
	/**
	 * A Grid For Roads
	 * A Position will be acquired everywhere there is not an empty road.
	 */
	private Semaphore[][] roadGrid = null;


	public SimCityLayout(int WindowSizeX, int WindowSizeY) {
		WINDOWX = WindowSizeX;
		WINDOWY = WindowSizeY;

		numxGrids = WINDOWX / GRID_SIZEX;
		numyGrids = WINDOWY / GRID_SIZEY;

		//initialize the positionMap
		for(int x = 0; x < numxGrids; x++)
		{
			for(int y = 0; y < numyGrids; y++) {
				positionMap.put(new Dimension(x + 1, y + 1), new Dimension((x * GRID_SIZEX), y * GRID_SIZEY));
			}
		}

		//initialize grids
		mainGrid = addAndInitializeMainGrid(mainGrid);
		roadGrid = addAndInitializeRoadGrid(roadGrid);

	}








	private Semaphore[][] addAndInitializeRoadGrid( Semaphore[][] grid) {
		grid = new Semaphore[numxGrids + 1][numyGrids + 1];

		//Initialize the semaphore grid with 0 permits
		for (int i=0; i<numxGrids+1 ; i++)
			for (int j = 0; j<numyGrids+1; j++)
				grid[i][j]=new Semaphore(0,true);

		return grid;
	}//end initialize grid
	private Semaphore[][] addAndInitializeMainGrid( Semaphore[][] grid) {
		grid = new Semaphore[numxGrids + 1][numyGrids + 1];

		//Initialize the semaphore grid
		for (int i=0; i<numxGrids+1 ; i++)
			for (int j = 0; j<numyGrids+1; j++)
				grid[i][j]=new Semaphore(1,true);

		//build the animation areas
		try {
			//make the 0-th row and column unavailable
			for (int i=0; i<numyGrids+1; i++) grid[0][0+i].acquire();
			for (int i=1; i<numxGrids+1; i++) grid[0+i][0].acquire();

		}catch (Exception e) {
			System.out.println("Unexpected exception caught during setup:"+ e);
		}

		return grid;
	}//end initialize grid


	/**Adds a Road to the city.
	 * 
	 * @param xPos 	The starting x grid Position of the road
	 * @param yPos     The starting y grid Position of the road
	 * @param width	The number of grid positions across
	 * @param height	The number of grid positions high
	 * @return True if successful False if at least one of the Roads was unsuccessful
	 */
	public boolean addRoad(int xPos, int yPos, int width, int height){
		Dimension d = new Dimension(xPos, yPos);
		List<Road> rds = new ArrayList<>();
		Road rd;
		boolean successfull = true;

		//This loop will create a list of Roads that we want to add
		for(int x = 0; x < width;x++) {
			for(int y = 0; y < height; y++ ) {
				rd = new Road();
				rd.position = new Dimension(d);
				rds.add(rd);
				d.height++;
			}
			d.width++;
			d.height = yPos;
		}

		//This loop trys to add the Roads to the list of roads
		for(Road r: rds) {
			//Make sure the mainGrid is free
			if(mainGrid[r.position.width][r.position.height].tryAcquire()){
				//check to make sure there aren't permits for this road position...if there are something is wrong
				if(roadGrid[r.position.width][r.position.height].availablePermits() == 0)
				{
					roadGrid[r.position.width][r.position.height].release();//Release the roadGrid position
					roads.add(r);//add the road
				}
				else
					successfull = false;
			}
			else
				successfull = false;
		}
		return successfull;
	}//end addRoad



	/** Will draw components that have been added to the layout
	 * 
	 * @param g2
	 */
	public void draw(Graphics2D g) {

		//Paint the grid
		for(Dimension pos : positionMap.keySet()) {
			Dimension dim = positionMap.get(pos);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(dim.width, dim.height, 24, 24);
		} //Grid painted


		//Paint the roads
		for(Road r : roads) {
			Dimension d = positionMap.get(r.position);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(d.width, d.height, GRID_SIZEX, GRID_SIZEY);
		}//Roads painted

	}



	
	
	
	
	
	class Road{
		String direction;//positive if cars can only travel in positive directions, Negative otherwise
		Dimension position;

		Road() {
			position = new Dimension(0, 0);
			direction=null;
		}
	}
}
