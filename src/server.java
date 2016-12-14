import java.net.*;
import java.io.*;
public class server 
{
int highscore;
	server()
	{
		
		while(true)
		{
		try{
			ServerSocket soc=new ServerSocket(3456);
			//System.out.println("yes");
			Socket s=soc.accept();
			System.out.println("yes");

			read r=new read(s,highscore);
			r.start();
			write w=new write(s,highscore);
 			w.start();
		}

catch(Exception e)
{
}}
	}

	public static void main(String [] args)
	{
		 server ser=new server();
		
	}
}

class read extends Thread
{
  Socket soc;
  int high;
	read(Socket s,int x)
	{
		soc=s;
		high=x;
					
	}
	public void run()
	{
		  while(true)
	                   {	
		try
		{
		DataInputStream ds=new DataInputStream(soc.getInputStream());
		String s=ds.readUTF();
		int a=Integer.parseInt(s);
		//System.out.println(a);


			ReadAndWrite file = new ReadAndWrite();
			
				// Check high scores from the file

 	               high = file.ReadFromFile("HighScores.data");
                              if(high<a)
                              {
			file = new ReadAndWrite();
			String message2=""+a;
			file.writeInFile(message2, "HighScores.data");
			System.out.println("congo");
                               }
    
   
                              }       
		catch(Exception e){}
        		  }
		
	        }
}






class write extends Thread
{
  int hs;
Socket s;
	write(Socket s,int highscore)
{
              	 this.s=s;
                  hs=highscore;
}
   public void run()
{
while(true)
{
try
{
			ReadAndWrite file = new ReadAndWrite();
				// Check high scores from the file
 				hs = file.ReadFromFile("HighScores.data");
	DataOutputStream dos=new DataOutputStream(s.getOutputStream());	
	String st=""+hs;
	dos.writeUTF(st);
	//System.out.println(st);                 
	//dos.writeUTF(st);
}
catch(Exception e)
{
}


}
}	
}