import java.net.*;

import java.util.Random;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * The main hook of our game. This class with both act as a manager for the
 * display and central mediator for the game logic.
 * 
 * Display management will consist of a loop that cycles round all entities in
 * the game asking them to move and then drawing them in the appropriate place.
 * With the help of an inner class it will also allow the player to control the
 * main ship.
 * 
 * As a mediator it will be informed when entities within our game detect events
 * (e.g. alient killed, played died) and will take appropriate game actions.
 * 
 * @author Kevin Glass
 */

public class Game extends Canvas implements Runnable, ActionListener,
		MouseListener, MouseMotionListener {
	boolean mouseStart = false;
	int mx; // variable for storing cursor's x cordinate
	int my; // variable for storing cursor's y cordinate
	public boolean right = false; // variable to check whether collided with
									// wall or not
	double moveSpeedX; // speed variable
	private Thread thread; // thread variable
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/** The list of all the entities that exist in our game */
	ArrayList<Entity> entities = new ArrayList();
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList<Entity> removeList = new ArrayList();
	/** The entity representing the player */
	public Entity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 1;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 200;
	/** The number of aliens left on the screen */
	public int alienCount;
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	boolean health = false;
	/**
	 * True if game logic needs to be applied this loop, normally as a result of
	 * a game event
	 */


        Socket s;
        DataOutputStream dos;
	DataInputStream dis;
  
	int score = 0;
	int ctr = 0;
	int hits = 0;
	String message2 = new String();
	private boolean allKilled = false;
	boolean boss = false;
	JButton b = new JButton("vv");
	JMenuBar menuBar = new JMenuBar();

	/*
	 * 
	 * 
	 * Add a JMenu and add options to it
	 */
	ButtonGroup group = new ButtonGroup();
	JMenu game = new JMenu("Game");
	JMenuItem emouse = new JMenuItem("Enable Mouse Navigation");
	JMenuItem dmouse = new JMenuItem("Disable Mouse Navigation");

	JMenuItem help = new JMenuItem("Help");
	JMenuItem scores = new JMenuItem("High Scores");
	JMenu Difficulty = new JMenu("Difficulty");
	JRadioButtonMenuItem low = new JRadioButtonMenuItem("Low");
	JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium");
	JRadioButtonMenuItem high = new JRadioButtonMenuItem("High");
	JMenu cheat = new JMenu("Cheats");
	JMenuItem ec = new JMenuItem("Enter Cheats");

	/*
	 * 
	 * 
	 * menu specified
	 */

	/**
	 * Construct our game and set it running.
	 */
	public Game() {
       try{
  		s=new Socket("172.20.10.2",3456);
 }catch(Exception e){}
		addMouseListener(this);
		addMouseMotionListener(this);
		// create a frame to contain our game
		JFrame container = new JFrame(
				"ONE MAN SHOOTER GAME");

		// get hold the content of the frame and set up the resolution of the
		// game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(new FlowLayout());

		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, 800, 600);
		panel.add(this);
		// setup the menu items and actionListener to all of them
		menuBar.add(game);
		game.add(help);
		game.add(scores);
		game.add(emouse);
		emouse.addActionListener(this);
		game.add(dmouse);
		dmouse.addActionListener(this);
		help.addActionListener(this);
		scores.addActionListener(this);
		menuBar.add(Difficulty);
		Difficulty.add(low);
		group.add(low);
		low.addActionListener(this);
		Difficulty.add(medium);
		group.add(medium);
		medium.addActionListener(this);
		Difficulty.add(high);
		group.add(high);
		high.addActionListener(this);
		menuBar.add(cheat);
		cheat.add(ec);
		ec.addActionListener(this);

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		// finally make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		container.setJMenuBar(menuBar);
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());

		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
		// to see at startup
		initStart();
		start();
		// startGame();

	}

	/**
	 * Start a fresh game, this should clear out any old data and create a new
	 * set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();

		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}

	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
	private void initStart() {
		// create the player ship and place it roughly in the center of the
		// screen
		ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
		entities.add(ship);

	}

	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the
		// screen
		String spriteName = null;
		ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
		entities.add(ship);
		ship.start();

		// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		alienCount = 0;

		for (int row = 0; row < 5; row++) {
			for (int x = 0; x < 5; x++) {
				if (row == 0 || row == 1)
					spriteName = "sprites/alien.gif";
				if (row == 2 || row == 3)
					spriteName = "sprites/alien2.gif";
				if (row == 4)
					spriteName = "sprites/alien3.gif";
				Entity alien = new AlienEntity(this, spriteName,
						200 + (x * 80), (30) + row * 30, 0.2);
				entities.add(alien);

				alienCount++;
				alien.start();

			}

		}
		//

		/*
		 * for(int i=0; i<50; i++) for(int j=0; j<5; j++){ Entity shield = new
		 * Shield(this, "sprites/shield.png",10 + (i * 2), (450) +(j*2));
		 * entities.add(shield); }
		 * 
		 * for(int i=0; i<50; i++) for(int j=0; j<5; j++){ Entity shield = new
		 * Shield(this, "sprites/shield.png",350+ (i * 2), (450) +(j*2));
		 * entities.add(shield); }
		 * 
		 * for(int i=0; i<50; i++) for(int j=0; j<5; j++){ Entity shield = new
		 * Shield(this, "sprites/shield.png",750 - (i * 2), (450) +(j*2));
		 * entities.add(shield); }
		 */
	}

	public void removeEntity(Entity entity) {

		entity.stop();
		removeList.add(entity);

	}

	public void stopGame() {

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.stop();
		}

		leftPressed = false;
		rightPressed = false;
		firePressed = false;
		this.stop();
	}

	/**
	 * Notification that the player has died.
	 */
	public void notifyDeath() {
		stopGame();
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true;
		boss = false;
		allKilled = false;
		hits = 0;
		mouseStart = false;
try{
                       String message3=""+score;      
                        dos=new DataOutputStream(s.getOutputStream());                
                        dos.writeUTF(message3); 



ReadAndWrite file = new ReadAndWrite();
			
				// Check high scores from the file

 			int	high = file.ReadFromFile("HighScores.data");
                              if(high<score)
                              {
			file = new ReadAndWrite();
			//String message2=""+score;
			file.writeInFile(message3, "HighScores.data");}

}catch(Exception e){}


	}

	/**
	 * Notification that the player has won since all the aliens are dead.
	 */
	public void notifyWin() {
		stopGame();
		message = "Well done! You Win!";
		waitingForKeyPress = true;
		mouseStart = false;
try{
                String message3=""+score;      
                        dos=new DataOutputStream(s.getOutputStream());                
                        dos.writeUTF(message3); 
ReadAndWrite file = new ReadAndWrite();
			
				// Check high scores from the file

 			int	high = file.ReadFromFile("HighScores.data");
                              if(high<score)
                              {
			file = new ReadAndWrite();
			//String message2=""+score;
			file.writeInFile(message3, "HighScores.data");}

}catch(Exception e){}
	}

	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!

		alienCount--;

		// Check if all aliens are killed or not
		if (alienCount == 0 && allKilled == false) {
			allKilled = true; // if allaliens are killed set the boolean to true
			message2 = "Your score is: " + score; // update the score
		}
		// check whether the boss has appeared or not
		if (allKilled == true && boss == false) {
			boss = true; // if boss didn't appeared make the boss appear and set
							// the boolean to true
			health = true;
			Entity boss = new BossEntity(this, "sprites/alien1.gif", 0,
					(50) + 0 * 30, 5);
			entities.add(boss);
			boss.start();

			alienCount++;

		}

		// if all aliens are killed plus boss is killed, notify the user win

		if (alienCount == 0 && boss == true && allKilled == true) {
			boss = false;
			allKilled = false;
			notifyWin();

			
			
try{
message2 = "" + score;
                        dos=new DataOutputStream(s.getOutputStream());                
                        dos.writeUTF(message2);
			// try to read from file
}
catch(Exception e){}


/*			ReadAndWrite file = new ReadAndWrite();
				if (highScore < score) { // checks previous score and current
											// score and updates accordingly
					file.writeInFile(message2, "HighScores.data");
				}
			} catch (IOException e) {
				System.out.print("You don't have any old high score record");
			}



        */              
		}

	}

	/**
	 * Attempt to fire a shot from the player. Its called "try" since we must
	 * first check that the player can fire at this point, i.e. has he/she
	 * waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}

		// if we waited long enough, create the shot entity, and record the
		// time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",
				ship.getX() + 10, ship.getY() - 30, -0.000005);
		entities.add(shot);
		shot.start();

	}

	/**
	 * The main game loop. This loop is running during all game play as is
	 * responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves - Moving the
	 * game entities - Drawing the screen contents (entities, text) - Updating
	 * game events - Checking Input
	 * <p>
	 */
	@Override
	public void run() {// System.out.println("Game Thread");

		// keep looping round til the game ends
		while (gameRunning) {

			// Get hold of a graphics context for the accelerated
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			//to display Scores
			Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
			g2d.setPaint(Color.white);
			g2d.setFont(new Font("Serif", Font.BOLD, 15));
			String sa = "" + score;
			g2d.drawString("Your Score:", 20, 15);
			
			
			// cycle round drawing all the entities we have in the game
			g2d.drawString(sa, 120, 15);
			g2d.dispose();
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				entity.draw(g);

			}

			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify
			// both entities that the collision has occured
			for (int p = 0; p < entities.size(); p++) {
				for (int s = p + 1; s < entities.size(); s++) {
					Entity me = entities.get(p);
					Entity him = entities.get(s);

					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}

			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			// if we're waiting for an "any key" press then draw the
			// current message
			if (waitingForKeyPress) {

				g.setColor(Color.white);
				g.drawString(message,
						(800 - g.getFontMetrics().stringWidth(message)) / 2,
						250);
				g.drawString("Press any key", (800 - g.getFontMetrics()
						.stringWidth("Press any key")) / 2, 300);
				g.drawString("Cheat YOLO : Makes alien Mad", (800 - g.getFontMetrics()
						.stringWidth("Cheat YOLO : Makes alien Mad")) / 2, 350);
				g.drawString("Cheat Flibble : Makes alien Calm", (800 - g.getFontMetrics()
						.stringWidth("Cheat Flibble : Makes alien Calm")) / 2, 370);
				g.drawString("Happy Space Invading", (800 - g.getFontMetrics()
						.stringWidth("Happy Space Invading")) / 2, 400);

			}

			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();

			// resolve the movement of the ship. First assume the ship
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			ship.setHorizontalMovement(0);

			//
			ship.start();
			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);

			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);

			}
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();
			}
			// System.out.println("eeee");

			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try {
				Thread.sleep(1);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * A class to handle keyboard input from the user. The class handles both
	 * dynamic input during game play, i.e. left/right and shoot, and more
	 * static type input (i.e. press any key to continue)
	 * 
	 * This has been implemented as an inner class more through habbit then
	 * anything else. Its perfectly normal to implement this as seperate class
	 * if slight less convienient.
	 * 
	 * @author Kevin Glass
	 */
	private class KeyInputHandler extends KeyAdapter {
		/**
		 * The number of key presses we've had while waiting for an "any key"
		 * press
		 */
		private int pressCount = 1;

		/**
		 * Notification from AWT that a key has been pressed. Note that a key
		 * being pressed is equal to being pushed down but *NOT* released. Thats
		 * where keyTyped() comes in.
		 * 
		 * @param e
		 *            The details of the key that was pressed
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "press"

			if (e.getKeyCode() == KeyEvent.VK_S) {

				System.out.println("YOOOOOOOOOOO");
			}

			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		}

		/**
		 * Notification from AWT that a key has been released.
		 * 
		 * @param e
		 *            The details of the key that was released
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notification from AWT that a key has been typed. Note that typing a
		 * key means to both press and then release it.
		 * 
		 * @param e
		 *            The details of the key that was typed.
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may
			// have had a keyType() event from the user releasing
			// the shoot or move keys, hence the use of the "pressCount"
			// counter.
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start
					// our new game
					waitingForKeyPress = false;
					startGame();
					score = 0;

					pressCount = 0;
				} else {
					pressCount++;
				}
			}

			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}

	/**
	 * The entry point into the game. We'll simply create an instance of class
	 * which will start the display and game loop.
	 * 
	 * @param argv
	 *            The arguments that are passed into our game
	 */

	public static void main(String argv []) {

		new Game();
                 

		// RobotExp exp = new RobotExp();
		// exp.auto();
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		thread = null;
		// System.out.println("ffff");
	}

	public ArrayList<Entity> getElements() {
		return entities;
	}

	/*
	 * for sounds but they don't work it compiles but sound is not played
	 */
	public static void gameSound(String path) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(Game.class
					.getResource(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			System.out.println("audio test");
			clip.start();

		} catch (Exception e) {
			/* Sound works but gives error in university computers */
			// System.out.println(e.getMessage());

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int highScore;
		if (e.getSource() == emouse) {

			mouseStart = true; // for mouse handling
		}
		if (e.getSource() == dmouse) {

			mouseStart = false; // for mouse handling
		}
		if (e.getSource() == low) { // sets difficulty by changing speeds
			moveSpeedX = 0.2;
		}
		if (e.getSource() == medium) {

			moveSpeedX = 0.4;
		}
		if (e.getSource() == high) {

			moveSpeedX = 0.8;
		}
		
		
		if (e.getSource() == help) {
			// for general help to the users
			JOptionPane
					.showMessageDialog(
							null,
							"Welcome to Space Invaders\n Press Space to Fire\n Arrow Keys to move you aliens\n GOOD LUCK");
		}
		
		if (e.getSource() == scores) {


			ReadAndWrite file = new ReadAndWrite();
			/*try {
				// Check high scores from the file

				highScore = file.ReadFromFile("HighScores.data");
				JOptionPane.showMessageDialog(null, "All Time High Score is: "
						+ score);
			} catch (Exception ea) {
				System.out.println("File does not exists!");
			}*/

try{
                      dis=new DataInputStream(s.getInputStream());
                      String st=dis.readUTF(); 
		
                      System.out.println("yo"+st);
                	JOptionPane.showMessageDialog(null, "All Time High Score is: "
						+ st);
}

catch(Exception et){}			
	}
		
		//for cheat engine
		if (e.getSource() == ec) {
			String s = JOptionPane.showInputDialog(null,
					"Enter cheat Please sir");
			System.out.println(s);
			if (s.equals("Flibble")) {

				moveSpeedX = 0;
			}
			if (s.equals("YOLO")) {

				moveSpeedX = 3;
			}
		}

	}

	public void mouseMoved(MouseEvent e) { // called during motion when no
											// buttons are down

		mx = e.getX();
		my = e.getY();
		if (mouseStart && mx < 750)
			ship.x = mx;

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}