team29
======

#Overview

Our project implements multiple residences, a market, a bank, a restaurant, 2 busses and multiple pedestrians utilizing A*, Day and Night system, among other features. 

Within our project, we initially assigned very specific roles for each team member to be responsible for. However, as development progressed, each team member assisted one another in the assignments we were responsible for, so every team member had a hand in every branch of the project. 

The specific initial emphases of each team member were:

Ryan: Transportation, overworld GUI, car crashes, individual restaurant

Jeffrey: Transportation, git-integration, control panel interface, individual restaurant, error checking

Mike: Person, Roles, shifts, time, All generic interfaces, wrapper classes, building integrations, person debugging, XML parser, individual restaurant

Kushaan: Person, Roles, parties, time, restaurant-market interaction

Dylan: Residence and Residence GUI, original restaurant integration, apartments and apartment guis, XML scenarios, parties

Luca: Market and Market GUI, delivery trucks, individual restaurant, restaurant-market interactions

Byron: Bank and Bank GUI, XML parser, individual restaurant 

Additionally, every member helped each other with design, bug-fixing, and J-Unit testing. We believe the work distribution was overall equal between all 7 of us. 

#Execution instructions

The project is best run through the eclipse editor by using the play button to run SimCity201Gui. If the play button does not work, the main application can be run through the src/gui/SimCity201Gui.java file. 

Loading specific scenarios is achieved through the initial GUI with labeled buttons. Once in the scenario, selecting View -> Settings -> Test View will display the names of all agents and constructs. To run another scenario, the application must be closed and reopened. 

Clicking on various buildings will show a zoomed in view of the building inside. In the food court and the apartments, clicking on buildings in the zoomed in view will zoom in on another building. 

Going into View -> Sim City Controls will display a control panel with various options. Selecting a person from the list of people on the left will display more information about them. The additional controls button creates a new panel with certain commands for the person in focus on the control panel. The zoom to agent button will change the focused view to wherever the focused agent is. The add person button will create a new panel with various options for adding another person to the scene.


#Full Disclosure (Known Issues)

We fully acknowledge that this version of Sim City is a work in progress and will have some issues and bugs present in its execution. As such

+ A* may lead to clogging of building entrances and impact machine performance
+ Of the scenarios present in the load screen, not all are fully implemented or follow specifications. Certain scenarios need to be tested manually using the control panel. 
+ Restaurant - Bank interaction is not implemented
