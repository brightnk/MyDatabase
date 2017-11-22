import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class User{

	String name;
	String password;
	boolean isAdmin;
	String selectedDBname;
	String selectedTableName;
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
	public void createTable(String tableName, PrintWriter out, ArguSet...args){
		if(selectedDB !=null){
			selectedDB.addTable(tableName, out,args );
		}else{
			out.println("please select db first");
		}
	}
	
	public void removeTable(String name, PrintWriter out){
		if(selectedDB !=null){
			selectedDB.removeTable(name, out);
		}else{
			out.println("please select db first");
		}
	}
	
	public UserTableAction useTable(String name, PrintWriter out){
		if(selectedDB !=null){
			selectedTable =selectedDB.useTable(name, out);
			if(selectedTable!=null) this.selectedTableName=name;
			return selectedTable;
		}
		out.println("please select db first");
		return null;
	}
	
	/*
	 * methods working on tables, manipulating the records
	 * 
	 */
	
	public void insertRecord(HashMap<String, String> insertMap){
		selectedTable.insertRecord(insertMap);
	}
	//search from all db records string, assume 1 condition only
	public void searchRecord(Condition condition){
		selectedTable.searchRecord(condition);
	}
	
	public void updateUser(String fieldName, String newValue, Condition condition){
		selectedTable.updateRecord(fieldName, newValue, condition);
	}
	
	

	//to do method
	public void displayRecord(ArrayList<String> records, String...fieldNames) {

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

interface UserDBAction{
	
	void addTable(String tableName, PrintWriter out, ArguSet... args);
	void removeTable(String name, PrintWriter out);
	UserTableAction useTable(String name, PrintWriter out);
}

interface UserTableAction{
	
	
	ArrayList<String> searchRecord(Condition condition);
	void insertRecord(HashMap<String, String> insertMap);
	void updateRecord(String fieldName, String newValue, Condition condition);
	void deleteRecord(Condition condition);

}

