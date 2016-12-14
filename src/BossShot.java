




public class BossShot extends Entity {
	/** The vertical speed at which the players shot moves */
	private double moveSpeed =0.000003;
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
	
	
	public BossShot(Game game,String sprite,int x,int y) {
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
		
		while(thread!=null){
			
			setVerticalMovement(moveSpeed ) ;   
			move();
			           
if(this.getY()>550){
				
				game.removeEntity(this);
}
				
			}
			
	}
	
	
	
}