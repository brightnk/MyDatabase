import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.*;
public class MyDB{
	static boolean DBwritable = true;
	ArrayList<User> users =new ArrayList<User>();
	ArrayList<Db> dbs = new ArrayList<Db>();	
	public MyDB(){
		
		User defaultUser = new User("admin", "admin", true);
		users.add(defaultUser);
	}
	
	
	//for user login check, return null if no record find
	public User login(String name, String password){
		for(User user: users){
			if(user.name.equals(name) &&user.password.equals(password)) return user;
		}
		return null;
	}
	
	
	
	public void addUser(User currentUser, User user, PrintWriter out){
		if(currentUser.isAdmin){
			boolean isExsit =false;
			for(User myUser:users){
				if(myUser.name.equals(user.name)){
					isExsit =true;
					break;
				}
			}
			
			if(!isExsit){
				users.add(user);
				out.println(ClientHandler.WELCOMEWORD+"added 1 new user successfully");
			}else out.println(ClientHandler.WELCOMEWORD+"user already exist");
		}else out.println(ClientHandler.WELCOMEWORD+"You are not admin");
	}


	
	public void deleteUser(User currentUser, String username, PrintWriter out) {
		
		if(currentUser.isAdmin){
			for(User myUser: users){
				if(myUser.name.equals(username)){
					users.remove(myUser);
					out.println(ClientHandler.WELCOMEWORD+"delete user successfully");
					System.out.println(ClientHandler.WELCOMEWORD+"delete user successfully");
					return;
				}
			}
			out.println(ClientHandler.WELCOMEWORD+"no user found");
		}else out.println(ClientHandler.WELCOMEWORD+"You are not admin");
		
		
		
	}



	public void updateUser(User currentUser, String olduserName, User updatedUser, PrintWriter out) {
		
		if(currentUser.isAdmin){
			for(User myUser: users){
				if(myUser.name.equals(olduserName)){
					users.set(users.indexOf(myUser), updatedUser);
					System.out.println(ClientHandler.WELCOMEWORD+"update user successfully");
					out.println(ClientHandler.WELCOMEWORD+"update user successfully");
					return;
				}
			}
			
			out.println(ClientHandler.WELCOMEWORD+"no user found");
		}else out.println(ClientHandler.WELCOMEWORD+"You are not admin");
		
		
		
	}


	
	public void createDB(User currentUser, String name, PrintWriter out) {
		
		if(currentUser.isAdmin){
			for(Db db:dbs){
				if(db.dbName.equals(name)){
					out.println(ClientHandler.WELCOMEWORD+"The db name is already existing, please change");
					return;
				}
			}
			dbs.add(new Db(name));
		}else out.println(ClientHandler.WELCOMEWORD+"you are not admin");
		
	}


	
	public void deleteDB(User currentUser, String name, PrintWriter out) {
		if(currentUser.isAdmin){
			if(currentUser.selectedDBname.equals(name)){
				currentUser.selectedDBname=null;
				currentUser.selectedDB = null;
			}
			for(Db db:dbs){
				if(db.dbName.equals(name)){
					dbs.remove(db);
					return;
				}
			}
			out.println(ClientHandler.WELCOMEWORD+"The db name is not exsiting");
		}else out.println(ClientHandler.WELCOMEWORD+"not admin");
		
		
		
	}


	
	public UserDBAction useDB(String name, PrintWriter out) {
		for(Db db:dbs){
			if(db.dbName.equals(name)){
				
				out.println(ClientHandler.WELCOMEWORD+"now using database: "+db.dbName);
				return db;
			}
		}
		out.println(ClientHandler.WELCOMEWORD+"The db name is not exsiting");
		return null;
	}
	
}





//Database class
class Db implements UserDBAction{
	static int LASTID =0;
	int dbId;
	String dbName;
	ArrayList<Table> tables = new ArrayList<Table>(); 
	
	public Db(String name){
		LASTID++;
		this.dbId = LASTID;
		this.dbName = name;
	}

	@Override
	public void addTable(String tableName, PrintWriter out, ArguSet... args) {
		for(Table table:tables){
			if(table.tableName.equals(tableName)){
				out.println(ClientHandler.WELCOMEWORD+"The table is already existing, please change name");
				return;
			}
		}
		tables.add(new Table(tableName, args));
		out.println(ClientHandler.WELCOMEWORD+"add table successfully");

	}

	@Override
	public void removeTable(String name, PrintWriter out) {
		
		for(Table table:tables){
			if(table.tableName.equals(name)){
				tables.remove(table);
				out.println(ClientHandler.WELCOMEWORD+"delete 1 table successfully");
				return;
			}
		}
		out.println(ClientHandler.WELCOMEWORD+"The table name is not exsiting");

	}

	@Override
	public UserTableAction useTable(String name, PrintWriter out) {
		for(Table table:tables){
			if(table.tableName.equals(name)){
				out.println(ClientHandler.WELCOMEWORD+"Now using table: "+ name);
				return table;
			}
		}
		out.println(ClientHandler.WELCOMEWORD+"The table name is not exsiting");
		return null;
	};
	
	
	
}

class Table implements UserTableAction{
	static int LASTID =0;
	int recordID=0;
	int tableId;
	String tableName;
	Map<String, DataType> recordMeta = new LinkedHashMap<String, DataType>();
	ArrayList<String> recordsTxt = new ArrayList<String>();
	
	public Table(String name, ArguSet...arguSets){
		LASTID++;
		tableId=LASTID;
		this.tableName=name;
		for(ArguSet arguSet:arguSets){
			recordMeta.put(arguSet.name, new DataType(arguSet.type.toUpperCase(), arguSet.length));
		}		
	};
	
	@Override
	public void insertRecord(HashMap<String, String> insertMap) {
		 this.recordID++;
		 String fieldname;
		 String insertString = "{id:"+recordID;
		 try{
			for(Map.Entry<String, DataType> entry: recordMeta.entrySet()){
				fieldname = entry.getKey();
				String insertValue = insertMap.get(fieldname);
				String metaType = entry.getValue().type;
				int metaLength = entry.getValue().length;
				typeCheck(metaType, insertValue);
				if(insertValue.length()>metaLength) throw new Exception();
				else{
					
					insertString += ", "+fieldname+": "+insertValue;
					
				}
			}
			insertString +="}";
			recordsTxt.add(insertString);
		 }catch(Exception e){
			 this.recordID--;
			 System.out.println(e.getMessage());
		 }

	}
	public void typeCheck(String type, String insertValue){
		switch(type.toUpperCase()){
			case "INT": Integer.parseInt(insertValue);
				break;
			case "STRING":
				break;
			case "DOUBLE": Double.parseDouble(insertValue);
				break;
		}
	}


	
	@Override
	public ArrayList<String> searchRecord(Condition condition) {
		ArrayList<String> afterSearch =new ArrayList<String>();
		JSONObject myJson;
		for(String record: recordsTxt){
			try {
				myJson = new JSONObject(record);
				switch(condition.comparer){
					case ">": if(myJson.getString(condition.fieldName).compareTo(condition.condition)>0) afterSearch.add(record);
						break;
					case "<":if(myJson.getString(condition.fieldName).compareTo(condition.condition)<0) afterSearch.add(record);
						break;
					case "=":if(myJson.getString(condition.fieldName).compareTo(condition.condition)==0) afterSearch.add(record);
						break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return afterSearch;
		
	}




	@Override
	public void updateRecord(String fieldName, String newValue, Condition condition) {
		JSONObject myJson;
		for(String record: recordsTxt){
			try {
				myJson = new JSONObject(record);
				switch(condition.comparer){
					case ">": if(myJson.getString(condition.fieldName).compareTo(condition.condition)>0){
						myJson.put(fieldName,newValue);
						record = myJson.toString();
					}
						break;
					case "<":if(myJson.getString(condition.fieldName).compareTo(condition.condition)<0) {
						myJson.put(fieldName,newValue);
						record = myJson.toString();
					}
						break;
					case "=":if(myJson.getString(condition.fieldName).compareTo(condition.condition)==0) {
						myJson.put(fieldName,newValue);
						record = myJson.toString();
					}
						break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}



	@Override
	public void deleteRecord(Condition condition) {
		JSONObject myJson;
		for(String record: recordsTxt){
			try {
				myJson = new JSONObject(record);
				switch(condition.comparer){
					case ">": if(myJson.getString(condition.fieldName).compareTo(condition.condition)>0) recordsTxt.remove(record);
						break;
					case "<":if(myJson.getString(condition.fieldName).compareTo(condition.condition)<0) recordsTxt.remove(record);
						break;
					case "=":if(myJson.getString(condition.fieldName).compareTo(condition.condition)==0) recordsTxt.remove(record);
						break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	

	class DataType{
		String type;
		int length;
		public DataType(String type, int length){
			this.type=type;
			this.length=length;
		}
	}







	
	
	
}