package test;

import java.io.*;

public class Serialization {

	public static <T> void Serialize(T o, String path) throws IOException
	{	
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
	}
	

    public static <T> T Deserialize(String path) throws IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        T o = (T)ois.readObject();
        ois.close();
		return o;
    }
	
}
