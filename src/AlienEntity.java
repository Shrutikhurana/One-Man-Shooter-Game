import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class AlienEntity extends Entity {
	Thread t1 = new Thread();

	private Game game;

	String reference;

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
	public AlienEntity(Game game, String ref, int x, int y, double nmoveSpeedX) {

		super(ref, x, y);
		reference = ref;
		this.game = game;
		game.moveSpeedX = nmoveSpeedX;
		dx = -game.moveSpeedX;

	}

	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other
	 *            The other entity
	 */
	@Override
	public void collidedWith(Entity other) {
		// No need for any collision detection here
		// all collision detection carried out by shot entity
	}

	/*
	 * 
	 * 
	 * This method is going to control the aliens
	 */

	public synchronized void run() {
		Entity entity;
		while (thread != null) {

			try {
				if (getX() < 750 && game.right == false) { // If its inside the
															// 720
															// and haven't
															// touched the right
															// side

					setHorizontalMovement(game.moveSpeedX);
					move();
					Thread.sleep(2);

					if (getX() == 750) {// if alien reaches the corner change
										// the
										// booleon so as to transverse direction

						game.right = true; // change right as true
						for (int j = 0; j < game.entities.size(); j++) { // if
																			// alien
																			// have
																			// tocuhed
																			// right
																			// side
							entity = (Entity) game.entities.get(j); // access
																	// entites
																	// and check
																	// if they
																	// are alien
							if (entity instanceof AlienEntity) {

								entity.setVerticalMovement(10); // if they are
																// alien
								entity.move(); // make them move down
								entity.setVerticalMovement(0); // now stop them
																// from going
																// down further

							}

						}

					}
				}
				if (getX() > 10 && game.right == true) { // now it has touched
															// right so
															// direction is
															// reversed
					setHorizontalMovement(-game.moveSpeedX);
					move();
					Thread.sleep(2);

					if (getX() == 10) {
						game.right = false;// if reaches corner change boolean
											// for change of direction
						for (int j = 0; j < game.entities.size(); j++) { // access
																			// each
																			// entity
							entity = (Entity) game.entities.get(j); // if its an
																	// alien
							if (entity instanceof AlienEntity) {

								entity.setVerticalMovement(10); // make them
																// move down by
																// 10

								entity.move();

								entity.setVerticalMovement(0); // now stop there
																// fither down
																// movement

							}

						}

					}
				}
			} catch (Exception e) {
			}
		}
	}
}