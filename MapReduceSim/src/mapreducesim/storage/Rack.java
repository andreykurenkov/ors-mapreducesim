package mapreducesim.storage;

public class Rack extends Node {

	private int speed; // between rack and root, gbps

	public Rack(Root root, String name) {
		super(root, name);
		setSpeed(9);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return this.speed;
	}
}