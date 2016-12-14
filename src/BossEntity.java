import java.util.Random;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class BossEntity extends Entity {
	Thread t1 = new Thread();
	/** The speed at which the alient moves horizontally */
	private double moveSpeedX = 0.005;
	/** The game in which the entity exists */
	private Game game;
	private boolean dive = false;
	boolean right = false;
	int x = 0;
	static int randomInt;
	static int randomInt2;

	// private int iterations=0;

	/**
	 * Create a new alien entity
	 * 
	 * @param game
	 *            The game in which this entity is being created
	 * @param ref
	 *            The sprite which should be displayed for this alien
	 * @param x
	 *            The intial x location of this alien
	 * @param y
	 *            The intial y location of this alient
	 */
	public BossEntity(Game game, String ref, int x, int y, int nmoveSpeedX) {
		super(ref, x, y);
		this.game = game;
		moveSpeedX = nmoveSpeedX;
		dx = -moveSpeedX;
		dive = false;
	}

	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other
	 *            The other entity
	 */
	@Override
	public void collidedWith(Entity other) {

	}

	@Override
	public synchronized void run() {
		// setHorizontalMovement(0.001);

		while (thread != null) {

			Random randomGenerator = new Random();// create random numbers
			randomInt = randomGenerator.nextInt(5);
			randomInt2 = randomGenerator.nextInt(100);
			
			
			if (BossEntity.randomInt2 == 1) { // 1% chance of firing bullet
				// when we hit boss 3 times
				// he gets angry and fire bullets

				BossShot shot1 = new BossShot(game, "sprites/shot.gif", getX(),
						getY());
				// starts a boss bulllet thread
				shot1.start();
				game.entities.add(shot1);

			}
			if (getX() < 750 && right == false) { // If its inside the x
													// positive and not going
													// towards right

				setHorizontalMovement(moveSpeedX);
				move();
				try {
					Thread.sleep(10);

				} catch (Exception e) {

				}
				if (getX() == 750) {// if alien reaches the corner change the
									// booleon so as to transverse direction
					right = true;

					setVerticalMovement(25);
					move();
					try {
						Thread.sleep(10);
					} catch (Exception e) {
					}
				}
				setVerticalMovement(0);
			}

			if (getX() > 10 && right == true) {
				setHorizontalMovement(-moveSpeedX);
				move();
				try {
					Thread.sleep(10);
				} catch (Exception e) {

				}
				if (getX() == 10) {
					right = false;// if reaches corner change boolean for change
									// in dir
					setVerticalMovement(25);
					move();
					try {
						Thread.sleep(10);
					} catch (Exception e) {

					}
				}
				setVerticalMovement(0);
			}
		}
	}
}
