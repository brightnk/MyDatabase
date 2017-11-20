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
	
	
	
	public void addUser(User currentUser, User user){
		if(currentUser.isAdmin){
			if(login(user.name, user.password)==null) users.add(user);
			else System.out.println("user already exist");
		}else System.out.println("not admin");
	}


	
	public void deleteUser(User currentUser, User user) {
		
		if(currentUser.isAdmin){
			for(User myUser: users){
				if(myUser.name.equals(user.name) &&myUser.password.equals(user.password)){
					users.remove(myUser);
					System.out.println("delete 1 user successfully");
					return;
				}
			}
			System.out.print("no user found");
		}else System.out.println("not admin");
		
		
		
	}



	public void updateUser(User currentUser, User olduser, User updatedUser) {
		
		if(currentUser.isAdmin){
			for(User myUser: users){
				if(myUser.name.equals(olduser.name) &&myUser.password.equals(olduser.password)){
					myUser = updatedUser;
					System.out.println("update 1 user successfully");
					return;
				}
			}
			System.out.print("no user found");
		}else System.out.println("not admin");
		
		
		
	}


	
	public void createDB(User currentUser, String name) {
		
		if(currentUser.isAdmin){
			for(Db db:dbs){
				if(db.dbName.equals(name)){
					System.out.println("The db name is already existing, please change");
					return;
				}
			}
			dbs.add(new Db(name));
		}else System.out.println("not admin");
		
	}


	
	public void removeDB(User currentUser, String name) {
		if(currentUser.isAdmin){
			for(Db db:dbs){
				if(db.dbName.equals(name)){
					dbs.remove(db);
					return;
				}
			}
			System.out.println("The db name is not exsiting");
		}else System.out.println("not admin");
		
		
		
	}


	
	public User.UserDBAction useDB(String name) {
		for(Db db:dbs){
			if(db.dbName.equals(name)){
				return db;
			}
		}
		System.out.println("The db name is not exsiting");
		return null;
	}
	
}





//Database class
class Db implements User.UserDBAction{
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
	public void addTable(String tableName, ArguSet... args) {
		for(Table table:tables){
			if(table.tableName.equals(tableName)){
				System.out.println("The table is already existing, please change name");
				return;
			}
		}
		tables.add(new Table(tableName, args));

	}

	@Override
	public void removeTable(String name) {
		for(Table table:tables){
			if(table.tableName.equals(name)){
				tables.remove(table);
				return;
			}
		}
		System.out.println("The table name is not exsiting");

	}

	@Override
	public Table useTable(String name) {
		for(Table table:tables){
			if(table.tableName.equals(name)){
				
				return table;
			}
		}
		System.out.println("The table name is not exsiting");
		return null;
	};
	
	
	
}

class Table implements User.UserTableAction{
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
			recordMeta.put(arguSet.name, new DataType(arguSet.type, arguSet.length));
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
		switch(type){
			case "INT": Integer.parseInt(insertValue);
				break;
			case "String":
				break;
			case "Double": Double.parseDouble(insertValue);
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