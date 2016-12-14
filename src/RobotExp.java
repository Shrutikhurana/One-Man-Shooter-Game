import java.awt.AWTException; 
import java.awt.Robot; 
import java.awt.event.KeyEvent; 
import java.util.Random;
public class RobotExp { 
	int x=0;
	void auto(){
		
			try {                                                                                                      
				while(true){                                                         
			Robot robot = new Robot();                                                       
			// Creates the delay of 5 sec so that you can open notepad before 
			// Robot start writting 
			x++;
			robot.delay(50); 
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(100);
			if(randomInt%2==0){
				for(int i=0; i< randomInt; i++){
					robot.keyPress(KeyEvent.VK_LEFT); 
				}
			}
			else{
				for(int i=0; i< randomInt; i++)
					robot.keyPress(KeyEvent.VK_RIGHT);   
			    } 

			for(int i=0; i< randomInt; i++)
			robot.keyPress(KeyEvent.VK_SPACE);  
				}
			} catch (AWTException e) { 
			e.printStackTrace(); 
			} 
			System.out.println(x);
		
	}
public static void main(String[] args) { 

} 
}                                                                                                                                                                                                                                                                 