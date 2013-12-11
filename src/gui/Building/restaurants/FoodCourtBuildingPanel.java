package gui.Building.restaurants;

import gui.BuildingsPanels;
import gui.LocationInfo;
import gui.SetUpWorldFactory;
import gui.Building.BuildingGui;
import gui.Building.BuildingPanel;
import interfaces.GuiPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import Person.Role.Role;


/**
 * class for various gui building panels.
 * BuildingPanel is added to the buildingPanels and is displayed in the zoomed in view of a building.
 * 
 *
 */
@SuppressWarnings("serial")
public class FoodCourtBuildingPanel extends BuildingPanel{
	
	FoodCourtAnimationPanel animPanel = null;
	LocationInfo locationInfo = null;
	SetUpWorldFactory factory = null;
	
	private List<String> nameList = new ArrayList<String>();
	
	public FoodCourtBuildingPanel(Rectangle2D r, String name, BuildingsPanels buildingPanels, SetUpWorldFactory factory, LocationInfo info) {
		super(r, name, buildingPanels);
		this.removeAll();
		setBackground( Color.gray );
		animPanel = new FoodCourtAnimationPanel();
		setLayout(new BorderLayout());
		JLabel j = new JLabel( myName );
		add(j, BorderLayout.NORTH);
		add( animPanel, BorderLayout.CENTER );
		
		this.factory = factory;
		locationInfo = new LocationInfo(info);
		
		
		//Add Ryan's Restaurant
		RyansRestaurantBuilding rrb = new RyansRestaurantBuilding(new BuildingGui(50, 50, 50, 50));
		RyansRestaurantBuildingPanel rrbp = new RyansRestaurantBuildingPanel(rrb, "Ryan's Restaurant", buildingPanels);
		rrb.setBuildingPanel(rrbp);
		buildingPanels.addBuildingPanel(rrbp);
		locationInfo.name = "Ryan's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Ryan's Restaurant");
		animPanel.addBuildingGui(rrb);
		
		//Adds Dylan's Restaurant
		RestaurantBuilding drb = new RestaurantBuilding(new BuildingGui(375, 250, 50, 50));
		RestaurantBuildingPanel drbp = new RestaurantBuildingPanel(drb, "Dylan's Restaurant", buildingPanels);
		drb.setBuildingPanel(drbp);
		buildingPanels.addBuildingPanel(drbp);
		locationInfo.name = "Dylan's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Dylan's Restaurant");
		animPanel.addBuildingGui(drb);
		
		//Adds Kush's Restaurant
		KushRestaurantBuilding krb = new KushRestaurantBuilding(new BuildingGui(50, 250, 50, 50));
		KushRestaurantBuildingPanel krbp = new KushRestaurantBuildingPanel(krb, "Kush's Restaurant", buildingPanels);
		krb.setBuildingPanel(krbp);
		buildingPanels.addBuildingPanel(krbp);
		locationInfo.name = "Kush's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Kush's Restaurant");
		animPanel.addBuildingGui(krb);
		
		//Adds Luca's Restaurant
		LucaRestaurantBuilding lrb = new LucaRestaurantBuilding(new BuildingGui(50, 150, 50, 50));
		LucaRestaurantBuildingPanel lrbp = new LucaRestaurantBuildingPanel(lrb, "Luca's Restaurant", buildingPanels);
		lrb.setBuildingPanel(lrbp);
		buildingPanels.addBuildingPanel(lrbp);
		locationInfo.name = "Luca's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Luca's Restaurant");
		animPanel.addBuildingGui(lrb);
		
		//Adds Byron's Restaurant
		ByronRestaurantBuilding brb = new ByronRestaurantBuilding(new BuildingGui(675, 250, 50, 50));
		ByronRestaurantBuildingPanel brbp = new ByronRestaurantBuildingPanel(brb, "Byron's Restaurant", buildingPanels);
		brb.setBuildingPanel(brbp);
		buildingPanels.addBuildingPanel(brbp);
		locationInfo.name = "Byron's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Byron's Restaurant");
		animPanel.addBuildingGui(brb);
		
		//Adds Jeffrey's Restaurant
		JeffreyRestaurantBuilding jrb = new JeffreyRestaurantBuilding(new BuildingGui(675, 150, 50, 50));
		JeffreyRestaurantBuildingPanel jrbp = new JeffreyRestaurantBuildingPanel(jrb, "Jeffrey's Restaurant", buildingPanels);
		jrb.setBuildingPanel(jrbp);
		buildingPanels.addBuildingPanel(jrbp);
		locationInfo.name = "Jeffrey's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Jeffrey's Restaurant");
		animPanel.addBuildingGui(jrb);
		
		//Adds Mike's Restaurant
		MikeRestaurantBuilding mrb = new MikeRestaurantBuilding(new BuildingGui(675, 50, 50, 50));
		MikeRestaurantBuildingPanel mrbp = new MikeRestaurantBuildingPanel(mrb, "Mike's Restaurant", buildingPanels);
		mrb.setBuildingPanel(mrbp);
		buildingPanels.addBuildingPanel(mrbp);
		locationInfo.name = "Mike's Restaurant";
		factory.addLocationToMap(locationInfo);
		nameList.add("Mike's Restaurant");
		animPanel.addBuildingGui(mrb);
		
	}
	
	
	public String getName() {
		return myName;
	}

	public List<String> getNameList() {
		return nameList;
	}
	
	/** 
	 * 
	 * @param rest
	 */
	public void addRestaurant(BuildingPanel rest){
		
	}
	
	
	
	
	/**
	 * 
	 * Sets the test view
	 */
	public void setTestView(boolean test){
		animPanel.setTestView(test);
	}
	
	
	
	
	
	
	
	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
	}


	@Override
	public void addPersonWithRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public GuiPanel getPanel() {
		return null;
	}

}
