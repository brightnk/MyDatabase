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
			out.println(ClientHandler.WELCOMEWORD+"please select db first");
		}
	}
	
	public void removeTable(String name, PrintWriter out){
		if(selectedDB !=null){
			selectedDB.removeTable(name, out);
		}else{
			out.println(ClientHandler.WELCOMEWORD+"please select db first");
		}
	}
	
	public UserTableAction useTable(String name, PrintWriter out){
		if(selectedDB !=null){
			selectedTable =selectedDB.useTable(name, out);
			if(selectedTable!=null) this.selectedTableName=name;
			return selectedTable;
		}
		out.println(ClientHandler.WELCOMEWORD+"please select db first");
		return null;
	}
	
	/*
	 * methods working on tables, manipulating the records
	 * 
	 */
	
	public void insertRecord(HashMap<String, String> insertMap, PrintWriter out){
		if(selectedTable!=null)
		selectedTable.insertRecord(insertMap, out);
		else out.println(ClientHandler.WELCOMEWORD+"please select table first");
			
	}
	//search from all db records string, assume 1 condition only
	public ArrayList<String> searchRecord(Condition condition, PrintWriter out){
		return selectedTable.searchRecord(condition, out);
	}
	
	
	public void updateRecord(String fieldName, String newValue, Condition condition, PrintWriter out){
		selectedTable.updateRecord(fieldName, newValue, condition, out);
	}
	
	public void deleteRecord(String comparer,String fieldName,	String condition, PrintWriter out){
		if(selectedTable!=null)
			selectedTable.deleteRecord(comparer, fieldName, condition, out);
			else out.println(ClientHandler.WELCOMEWORD+"please select table first");
	}
	

	//to do method
	public void displayRecord(ArrayList<String> records, PrintWriter out, String...fieldNames) {
			selectedTable.displayRecord(records, out, fieldNames);
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
	
	public Condition(){}
	
}

interface UserDBAction{
	
	void addTable(String tableName, PrintWriter out, ArguSet... args);
	void removeTable(String name, PrintWriter out);
	UserTableAction useTable(String name, PrintWriter out);
}

interface UserTableAction{
	
	
	ArrayList<String> searchRecord(Condition condition, PrintWriter out);
	void insertRecord(HashMap<String, String> insertMap, PrintWriter out);
	void updateRecord(String fieldName, String newValue, Condition condition, PrintWriter out);
	void displayRecord(ArrayList<String> records, PrintWriter out,String...fieldName);
	void deleteRecord(String comparer,String fieldName,	String condition, PrintWriter out);

}

