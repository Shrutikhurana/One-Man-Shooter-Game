
/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends Entity {
	/** The game in which the ship exists */
	private Game game;
	Thread thread;
	boolean touch = false;

	/**
	 * Create a new entity to represent the players ship
	 * 
	 * @param game
	 *            The game in which the ship is being created
	 * @param ref
	 *            The reference to the sprite to show for the ship
	 * @param x
	 *            The initial x location of the player's ship
	 * @param y
	 *            The initial y location of the player's ship
	 */
	public ShipEntity(Game game, String ref, int x, int y) {
		super(ref, x, y);
		this.game = game;
	}

	/**
	 * Notification that the player's ship has collided with something
	 * 
	 * @param other
	 *            The entity with which the ship has collided
	 */
	public void collidedWith(Entity other) {
		if (collidesWith(other)) {
			// if it collides with any entity
			// remove it and notify that you lost
			game.removeEntity(this);
			game.notifyDeath();

		}

	}

	public void run() {

		// main logic for the ship to stay in bounds
		if (getX() != 750 && getX() != 10)
			move();
		if (getX() == 750 && getHorizontalMovement() < 0)
			move();
		if (getX() == 10 && getHorizontalMovement() > 0)
			move();

	}

}