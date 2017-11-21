import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class DBHelper {
	
	static Gson gson = new Gson();
	static final String DBFILENAME = "mydb.json";
	
	
	
	
	
	public static void readFromDB(MyDB mydb){
		try {
			JsonReader reader = new JsonReader(new FileReader(DBFILENAME));
			mydb = gson.fromJson(reader, MyDB.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void writeToDB(MyDB mydb){
		String mydbTxt =gson.toJson(mydb);
		System.out.println(mydbTxt);
		try {
			MyDB.DBwritable=false;
			BufferedWriter out = new BufferedWriter(new FileWriter(DBFILENAME));
			out.write(mydbTxt);
			out.close();
			MyDB.DBwritable=true;
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public static void connectToDB(MyDB mydb){
		File dbfile = new File(DBFILENAME);
		if(dbfile.exists()&&!dbfile.isDirectory()){
			readFromDB(mydb);
		}else{
			mydb = new MyDB();		
			writeToDB(mydb);
		}
		
		
	}
	
	
	
	
	
	

}
