package interfaces;

import Person.Role.Role;

public interface GuiPanel {

	/**
	 * Function that adds a specific Gui for a role. Implementing
	 * panels should decide which gui to add to its panel
	 * @param r the role to add
	 */
	void addGuiForRole(Role r);
	
	/**
	 * Remove this role from the gui
	 * @param r
	 */
	void removeGuiForRole(Role r);
}
