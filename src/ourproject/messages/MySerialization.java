package ourproject.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class MySerialization {
	

	
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try{

			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(obj);
			os.close();
			return out.toByteArray();
		
		}catch(Exception e){
			return null;
		}finally{
			
		}
	}
	
	
	
	public static Object deserialize(byte[] data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try{

			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			in.close();
			return is.readObject();
		
		}catch(Exception e){
			return null;
		}finally{
			
		}
	}
	
}
