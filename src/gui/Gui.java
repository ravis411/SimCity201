package gui;

import java.awt.*;

public interface Gui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    
    /** This function is called to switch between the default or test views
     * 
     * @param test
     */
    public void setTestView(boolean test);
}
