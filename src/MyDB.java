import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyDB implements User.UserAdminAction{
	
	private ArrayList<User> users =new ArrayList<User>();
	ArrayList<Db> dbs = new ArrayList<Db>();
	
	public MyDB(){
		
		User defaultUser = new User("admin", "admin", true, this);
		users.add(defaultUser);
	}
	
	
	//for user login check, return null if no record find
	public User login(String name, String password){
		for(User user: users){
			if(user.name ==name &&user.password==password) return user;
		}
		return null;
	}
	
	
	@Override
	public void addUser(User user){
		if(login(user.name, user.password)==null) users.add(user);
		else System.out.println("user already exist");
	}


	@Override
	public void deleteUser(User user) {
		for(User myUser: users){
			if(myUser.name ==user.name &&myUser.password==user.password){
				users.remove(myUser);
				System.out.println("delete 1 user successfully");
				return;
			}
		}
		System.out.print("no user found");
		
	}


	@Override
	public void updateUser(User olduser, User updatedUser) {
		for(User myUser: users){
			if(myUser.name ==olduser.name &&myUser.password==olduser.password){
				myUser = updatedUser;
				System.out.println("update 1 user successfully");
				return;
			}
		}
		System.out.print("no user found");
	}


	@Override
	public void createDB(String name) {
		for(Db db:dbs){
			if(db.dbName==name){
				System.out.println("The db name is already existing, please change");
				return;
			}
		}
		dbs.add(new Db(name));
	}


	@Override
	public void removeDB(String name) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}


class Db{
	static int LASTID =0;
	int dbId;
	String dbName;
	ArrayList<Table> tables = new ArrayList<Table>(); 
	
	public Db(String name){
		LASTID++;
		this.dbId = LASTID;
		this.dbName = name;
	};
	
	
	
}

class Table{
	int tableId;
	String tableName;
	LinkedHashMap<String, Object> recordMeta = new LinkedHashMap<String, Object>();
	ArrayList<String> recordsTxt = new ArrayList<String>();
	
	public Table(){};
	
}