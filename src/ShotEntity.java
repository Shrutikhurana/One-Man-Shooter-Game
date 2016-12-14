import java.awt.Toolkit;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public class ShotEntity extends Entity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed;
	/** The game in which this entity exists */
	private Game game;


	/**
	 * Create a new shot from the player
	 * 
	 * @param game
	 *            The game in which the shot has been created. Hint: Useful to
	 *            reference back to other entities!c
	 * @param sprite
	 *            The sprite representing this shot
	 * @param x
	 *            The initial x location of the shot
	 * @param y
	 *            The initial y location of the shot
	 */

	public ShotEntity(Game game, String sprite, int x, int y, double z) {
		super(sprite, x, y);

		this.game = game;
		this.moveSpeed = z;
		dy = moveSpeed;
	}

	/**
	 * Notification that this shot has collided with another entity
	 * 
	 * @parma other The other entity with which we've collided
	 */
	public void collidedWith(Entity other) {

		if (other instanceof AlienEntity) {
			// If entity is alien
			// remove it
			game.removeEntity(other);
			game.removeEntity(this);
			game.notifyAlienKilled();
			if (((AlienEntity) other).reference == "sprites/alien.gif") {
				game.score += 30;
				// checks what type of alien it was and updates the score
				// accordingly
			}
			if (((AlienEntity) other).reference == "sprites/alien2.gif") {

				// checks what type of alien it was and updates the score
				// accordingly
				game.score += 20;
			}
			if (((AlienEntity) other).reference == "sprites/alien3.gif") {

				// checks what type of alien it was and updates the score
				// accordingly
				game.score += 10;
			}

			// starts a new explosion thread so that explosion appears as soon
			// as the alien has been shot
			Explosion e1 = new Explosion(game, "sprites/e1.gif", other.getX(),
					getY());
			e1.start();
			game.entities.add(e1);
			// System.out.println(game.score);
		}
		if (other instanceof ShotEntity || other instanceof ShipEntity) {

			// no need for any code here because shot cannot collide with the
			// ship LOL
		}
		if (other instanceof BossEntity) {
			// If sit hit the boss, remove the bullet and increate the boss hit
			// by 1
			//update spores everytime
			//random
			game.score=game.score+BossEntity.randomInt*2;
			game.removeEntity(this);
			game.hits++;
			
			
			
			
			if (game.hits ==35) {
				// when I hit the boss 35 times. Then his game is finished and
				// You did it!!
	
				game.removeEntity(other);
				game.notifyAlienKilled();

			}
		}

	}

	public void run() {

		while (thread != null) {

			// normal movement thread, nothing complicated here!

			setVerticalMovement(moveSpeed);
			move();
			if (this.getY() < -100) {

				game.removeEntity(this);

			}
		}
	}

}