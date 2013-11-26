package interfaces;

import Person.Role.Role;

public interface GuiPanel {

	/**
	 * Function that adds a specific Gui for a role. Implementing
	 * panels should decide which gui to add to its panel
	 * @param r
	 */
	void addGuiForRole(Role r);
	
}
