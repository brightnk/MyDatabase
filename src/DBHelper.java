import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class DBHelper {
	
	//add Adapter to both interface, so Gson know how to serialize & deserialize the interface obj(ref)
	static Gson gson = new GsonBuilder()
			.registerTypeAdapter(UserDBAction.class, new InterfaceAdapter<UserDBAction>())
			.registerTypeAdapter(UserTableAction.class, new InterfaceAdapter<UserTableAction>())
            .create();
	
	static final String DBFILENAME = "mydb.json";
	
	
	
	
	
	public static MyDB readFromDB(){	
		try {
			
			MyDB mydb = new MyDB();
			JsonReader reader = new JsonReader(new FileReader(DBFILENAME));
			mydb = gson.fromJson(reader, MyDB.class);
			for(User user : mydb.users) user.canLogin =true;
			
			return mydb;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void writeToDB(MyDB mydb){
		if(MyDB.DBwritable){
			
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
		}else{
			try{
				Thread.sleep(200);
				writeToDB(mydb);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public static MyDB connectToDB(){
		MyDB mydb = new MyDB();
		File dbfile = new File(DBFILENAME);
		if(dbfile.exists()&&!dbfile.isDirectory()){
			return readFromDB();
		}else{
			mydb = new MyDB();		
			writeToDB(mydb);
			return mydb;
		}
		
		
	}

}


final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	@Override
    public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
        final JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", object.getClass().getName());
        wrapper.add("data", context.serialize(object));
        return wrapper;
    }
    
    @Override
    public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject wrapper = (JsonObject) elem;
        final JsonElement typeName = get(wrapper, "type");
        final JsonElement data = get(wrapper, "data");
        final Type actualType = typeForName(typeName); 
        return context.deserialize(data, actualType);
    }

    private Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    private JsonElement get(final JsonObject wrapper, String memberName) {
        final JsonElement elem = wrapper.get(memberName);
        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
        return elem;
    }

	
}
