import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class User{

	String name;
	String password;
	boolean isAdmin;
	UserDBAction selectedDB;
	UserTableAction selectedTable;
	
	public User(String name, String password, boolean isAdmin){

		this.name = name;
		this.password =password;
		this.isAdmin = isAdmin;

	};
	
	/*
	 * General method working on root DB before enter any selected db
	 * 
	 
	
	private void addUser(User user){
		if(isAdmin) myDB.addUser(user);
		else System.out.println("you don't have the previlege");
	}
	
	private void deleteUser(User user){
		if(isAdmin) myDB.deleteUser(user);
		else System.out.println("you don't have the previlege");
	}
	
	private void updateUser(User olduser, User newUser){
		if(isAdmin) myDB.updateUser(olduser, newUser);
		else System.out.println("you don't have the previlege");
	}
	
	private void createDB(String name){
		if(isAdmin) myDB.createDB(name);
		else System.out.println("you don't have the previlege");
	}
	
	private void removeDB(String name){
		if(isAdmin) myDB.removeDB(name);
		else System.out.println("you don't have the previlege");
	}
	
	
	private void useDB(String name){
		selectedDB = myDB.useDB(name);
		if(selectedDB==null){
			System.out.println("Database selected isnot existing");
		}
	}
	
*/
	/*
	 *   methods working on db level before enter into tables 
	 * 
	 */
	private void createTable(String tableName, ArguSet...args){
		if(selectedDB !=null){
			selectedDB.addTable(tableName, args);
		}
	}
	
	private void removeTable(String name){
		if(selectedDB !=null){
			selectedDB.removeTable(name);
		}
	}
	
	private void useTable(String name){
		if(selectedDB !=null){
			selectedTable =selectedDB.useTable(name);
		}
	}
	
	/*
	 * methods working on tables, manipulating the records
	 * 
	 */
	
	private void insertRecord(HashMap<String, String> insertMap){
		selectedTable.insertRecord(insertMap);
	}
	//search from all db records string, assume 1 condition only
	private void searchRecord(Condition condition){
		selectedTable.searchRecord(condition);
	}
	
	private void updateUser(String fieldName, String newValue, Condition condition){
		selectedTable.updateRecord(fieldName, newValue, condition);
	}
	
	

	//to do method
	public void displayRecord(ArrayList<String> records, String...fieldNames) {

	}
	
	
	

	
	interface UserDBAction{
		
		void addTable(String tableName, ArguSet... args);
		void removeTable(String name);
		Table useTable(String name);
	}
	
	interface UserTableAction{
		
		
		ArrayList<String> searchRecord(Condition condition);
		void insertRecord(HashMap<String, String> insertMap);
		void updateRecord(String fieldName, String newValue, Condition condition);
		void deleteRecord(Condition condition);

	}
	
}

class ArguSet{
	String name;
	String type;
	int length;
}


class Condition{
	String comparer;
	String fieldName;
	String condition;
	
	
}