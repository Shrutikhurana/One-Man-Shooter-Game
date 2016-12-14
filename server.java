import java.net.*;
import java.io.*;

class server extends Thread
{
    ServerSocket serverSocket;
   
   server(int port) throws IOException
   {
      serverSocket = new ServerSocket(1234);
      serverSocket.setSoTimeout(100000);
   }

   public void run()
   {
      int highscore=0,score=0;
      while(true)
      {
         try
         {
            Socket server = serverSocket.accept();
            DataInputStream in =
                  new DataInputStream(server.getInputStream());

            score=Integer.parseInt(in.readUTF()); 
            if(score>highscore)
            highscore=score;

            System.out.println(highscore);

            DataOutputStream out =
                  new DataOutputStream(server.getOutputStream());
            out.writeUTF(Integer.toString(highscore)); 

            
            server.close();
         }
         catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      try
      {
         Thread t = new server(1234);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}