import java.io.*;
import java.io.FileOutputStream.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


public class ReadAndWrite {
	public void writeInFile(String s,String file) throws IOException{
		File outFile = new File(file);
		FileOutputStream outFileStream 
				= new FileOutputStream(outFile);
		PrintWriter outStream = new PrintWriter(outFileStream);

		//write values of primitive data types to the stream
		outStream.println(s);
		

		//output done, so close the stream
		outStream.close();

	}
	public int ReadFromFile(String file) throws IOException{
		File inFile = new File(file);
		FileReader fileReader = new FileReader(inFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		String str;

		str = bufReader.readLine();
		int i = Integer.parseInt(str);

		//similar process for other data types

		bufReader.close();
		return i;
	}
	public boolean checkFile(String file){
		File f=new File(file); 
		return f.getAbsoluteFile().isFile();
	}
	
}     
