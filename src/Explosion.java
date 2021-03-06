



/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public class Explosion extends Entity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed =0;
	/** The game in which this entity exists */
	private Game game;
	/** True if this shot has been "used", i.e. its hit something */
	private boolean used = false;
	
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created. Hint: Useful to reference back to other entities!c
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	
	
	public Explosion(Game game,String sprite,int x,int y) {
		super(sprite,x,y);
		
		this.game = game;
		
		dy = moveSpeed;
	}

	
	/**
	 * Notification that this shot has collided with another
	 * entity
	 * 
	 * @parma other The other entity with which we've collided
	 */
	public void collidedWith(Entity other) {
		
	}



	
	public void run(){   
		try{
		while(thread!=null){
			
			
			thread.sleep(20);
				game.removeEntity(this);
				
				
			}
			
	}catch(Exception e){}
	}
	
	/**
	 * TODO: During thread execution move the shot. If its y coordinate is -100 or lower remove from
	 * the game to free up space.
	 */

	
}