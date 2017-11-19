
public class User{
	
	String name;
	String password;
	boolean isAdmin;
	UserAdminAction myDB;
	
	public User(String name, String password, boolean isAdmin, UserAdminAction myDBinterface){
		this.name = name;
		this.password =password;
		this.isAdmin = isAdmin;
		this.myDB = myDBinterface;

	};
		
	
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
	
	
	
	
	
	
	
	
	
	
	interface UserAdminAction{
		
		void addUser(User user);
		void deleteUser(User user);
		void updateUser(User user1, User user2);
		void createDB(String name);
		void removeDB(String name);

		
	}
	
	
	
}