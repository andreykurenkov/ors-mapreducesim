package mapreducesim.storage;

/**
 * Represents the root--the parent of Racks.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class Root extends Node {

	public Root() {
		super();
	}

	public int getNumRacks() {
		return this.getChildren().size();
	}

}
