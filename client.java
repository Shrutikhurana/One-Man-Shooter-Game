import java.net.*;
import java.io.*;

public class client
{
   public static void main(String [] args)
   {
      int port = 1234;
      try
      {
         Socket client = new Socket("localhost", port);
         int i=0;
         String sCurrentLine;
         DataInputStream d = new DataInputStream(new FileInputStream("HighScores.data"));
         while ((sCurrentLine = d.readLine()) != null)
          {
        	System.out.println(sCurrentLine);
       
                i++;
                if(i==1) break;
          }	
	
  //int score=Integer.parseInt(sCurrentLine); 
  //int score=(sCurrentLine); 
     //   	System.out.println(sCurrentLine);
       
         OutputStream outToServer = client.getOutputStream(); 
         DataOutputStream out =new DataOutputStream(outToServer);	
         out.writeUTF(sCurrentLine);
       

         d=new DataInputStream(client.getInputStream());
         System.out.println("The highest score as send by the server is..");
         System.out.println(d.readUTF());  

//System.out.println(score);
     //   
      }

      catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
