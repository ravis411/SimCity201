package gui;

import gui.Building.Building;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.*;
import java.util.concurrent.Semaphore;



/** This class holds the city layout information. Use this class to add roads and other static gui objects.
 * 	This is also where the grids are initialized. 
 * 
 *
 */
public class SimCityLayout {
	private final int numxGrids;
	private final int numyGrids;
	private final int GRID_SIZEX;
	private final int GRID_SIZEY;
	private final int WINDOWX;
	private final int WINDOWY;


	//A map of grid positions to Java coordinates
	public final Map<Dimension, Dimension> positionMap = new HashMap<Dimension, Dimension>();

	//A list of the roads
	private List<Road> roads = new ArrayList<>();
	//A List of crosswalks
	private List<Dimension> crosswalks = new ArrayList<>();
	

	//The main Semaphore Grid
	private Semaphore[][] mainGrid = null;
	/**
	 * A Grid For Roads
	 * A Position will be acquired everywhere there is not an empty road.
	 */
	private Semaphore[][] roadGrid = null;
	
	//A grid for the crosswalks //acquired everywhere there is not an empty crosswalk
	private Semaphore[][] crossWalkGrid = null;


	public SimCityLayout(int WindowSizeX, int WindowSizeY, int GridSizeX, int GridSizeY) {
		WINDOWX = WindowSizeX;
		WINDOWY = WindowSizeY;
		
		this.GRID_SIZEX = GridSizeX;
		this.GRID_SIZEY = GridSizeY;

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
		crossWalkGrid = addAndInitializeRoadGrid(crossWalkGrid);
		

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


	
	
	
	
	
	
	
	
	/**Adds a CrossWalk to the city.
	 * A crosswalk can only be added where there is an empty road.
	 * 
	 * @param xPos 	The starting x grid Position of the crosswalk
	 * @param yPos     The starting y grid Position of the crosswalk
	 * @param width	The number of grid positions across
	 * @param height	The number of grid positions high
	 * @return True if successful False if at least one of the crosswalk positions was unsuccessful
	 */
	public boolean addCrossWalk(int xPos, int yPos, int width, int height){
		Dimension d = new Dimension(xPos, yPos);
		List<Dimension> crosses = new ArrayList<>();
		boolean successfull = true;
		
		//Dimension cross;

		//This loop will create a list of positions that we want to add
		for(int x = 0; x < width;x++) {
			for(int y = 0; y < height; y++ ) {
				crosses.add(new Dimension(d));
				d.height++;
			}
			d.width++;
			d.height = yPos;
		}
		
		//This loop trys to add the crosses to the grid
		for(Dimension dd: crosses) {
			try {
				//Make sure the roadGrid is free
				if(roadGrid[dd.width][dd.height].availablePermits() != 0){
					//check to make sure there aren't permits for this road position...if there are something is wrong
					crossWalkGrid[dd.width][dd.height].release();
						crosswalks.add(new Dimension(dd));
				}
				else
					successfull = false;
			} catch (Exception e) {
				successfull = false;
			}
		}
		return successfull;
	}//end addCrossWalk
	
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
			try {
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
			} catch (Exception e) {
				successfull = false;
			}
		}
		return successfull;
	}//end addRoad
	
	
	
	/** Will try to add a building to the city
	 * 
	 * @param xPos
	 * @param yPos
	 * @param width
	 * @param height
	 * @return A pointer to a new Building object if successful. null otherwise
	 */
	public Building addBuilding(int xPos, int yPos, int width, int height) {
		Dimension startPos = new Dimension(xPos, yPos);
		List<Dimension> needed = new ArrayList<>();
		List<Dimension> acquired = new ArrayList<>();
		boolean successfull = true;
		Dimension d = new Dimension(startPos);

		//This loop will create a list of positions that we want to add
		for(int x = 0; x < width;x++) {
			for(int y = 0; y < height; y++ ){
				needed.add(new Dimension(d));
				d.height++;
			}
			d.width++;
			d.height = yPos;
		}
		
		try {
			//Now check if all are available
			for(Dimension dd : needed) {
				if(mainGrid[dd.width][dd.height].tryAcquire()) {
					acquired.add(dd);
				}
				else{
					successfull = false;
					break;
				}
			}//if all were acquired successful = true
		} catch (Exception e) {
			System.out.println("Error during setup...grid out of bounds");
			successfull = false;
		}
		
		
		
		if(successfull) {
			d = new Dimension(positionMap.get(startPos));
			Building b = new Building(d.width, d.height, width * GRID_SIZEX, GRID_SIZEY * height);
			return b;
		}
		//we need to release the spaces that were acquired
		else{
			for(Dimension dd : acquired) {
				mainGrid[dd.width][dd.height].release();
			}
		}
		
		return null;
	}



	/** Will draw components that have been added to the layout
	 * 
	 * @param g2
	 */
	public void draw(Graphics2D g) {

		//Paint the grid
		for(Dimension pos : positionMap.keySet()) {
			Dimension dim = positionMap.get(pos);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(dim.width, dim.height, GRID_SIZEX - 1, GRID_SIZEY-1);
		} //Grid painted


		//Paint the roads
		for(Road r : roads) {
			Dimension d = positionMap.get(r.position);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(d.width, d.height, GRID_SIZEX, GRID_SIZEY);
		}//Roads painted
		
		//Paint the crosswalks
		for(Dimension c : crosswalks){
			Dimension d = positionMap.get(c);
			g.setColor(Color.white);
			for(int i = 0; i < GRID_SIZEX; i+=5)
				g.drawLine(d.width + i, d.height, d.width + i, d.height + GRID_SIZEY);
			//g.drawRect(d.width, d.height, getGRID_SIZEX(), GRID_SIZEY);
		}

	}


	public Semaphore[][] getMainGrid() {
		return mainGrid;
	}

	public Semaphore[][] getRoadGrid() {
		return roadGrid;
	}
	public Semaphore[][] getCrossWalkGrid(){
		return crossWalkGrid;
	}
	
	
	
	
	
	class Road{
		String direction;//positive if cars can only travel in positive directions, Negative otherwise
		Dimension position;

		Road() {
			position = new Dimension(0, 0);
			direction=null;
		}
	}
	
	
	
	public void clear(){
		roads.clear();
		positionMap.clear();
		mainGrid = null;
		roadGrid = null;
	}


	//Public Accesses /////////////////////////////////////////////
	
	public int getWINDOWX() {
		return WINDOWX;
	}

	public int getGRID_SIZEX() {
		return GRID_SIZEX;
	}

	public int getGRID_SIZEY() {
		return GRID_SIZEY;
	}

	public int getWINDOWY() {
		return WINDOWY;
	}
	
	
}
