package test;

import java.io.*;
import java.util.Vector;

public class CommonFunction {


	public static void Load_File(String filename,StringBuffer data,String DecoderCharset) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new  FileInputStream(filename),DecoderCharset));
		
		int x;
		while((x = reader.read())!=-1)
			data.append((char)x);
		reader.close();
	}
	
	public static void Load_File(String filename,Vector<String> data,String DecoderCharset) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new  FileInputStream(filename),DecoderCharset));
				
		String temp;
		while((temp = reader.readLine())!=null)
			data.add(temp);
		reader.close();
	}
	public static void Save_File(String path,String filename,String InputString) throws IOException
	{
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		
	    BufferedWriter bw = new BufferedWriter(new FileWriter(dir+"\\"+filename)); 
	  
	    bw.write(InputString);
	    bw.flush();
	    bw.close();      	
	}
	
	
}