package interfaces;

/**
 * An interface meant to be implemented by Buildings that are 
 * themselves Workplaces
 * @author MSILKJR
 */
public interface Workplace {

	/**
	 * Message sent to the workplace that it is time to open
	 */
	void open();
	
	/**
	 * Message sent to the workplace that it is time to close
	 */
	void close();
}
