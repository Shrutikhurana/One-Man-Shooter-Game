





public class Shield extends Entity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed =0;
	/** The game in which this entity exists */
	private Game game;

	
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created. Hint: Useful to reference back to other entities!c
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	
	
	public Shield(Game game,String sprite,int x,int y) {
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
		if(other instanceof AlienEntity){
			
			//System.out.println(game.score);
		}
		if (other instanceof ShotEntity || other instanceof BossShot ) {
			
			game.removeEntity(this);
			game.removeEntity(other);
		}
		
		
if(other instanceof ShipEntity){
		//do nothing	
		}
	
	}



	
	public void run(){   
		
		//do nothing
			
	}
	
	
	
}