import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandler implements Runnable {
	public final static String WELCOMEWORD = "JDB-->";
	private Socket socket;
    private int id;
    private MyServer myServer;
    private MyDB mydb;
    private boolean loginOK = false;
    private User currentUser;
    PrintWriter out;
    public ClientHandler(Socket socket, int id, MyServer myServer) {
        this.socket = socket;
        this.id = id;
        this.myServer=myServer;
        mydb = myServer.mydb;
    }

	@Override
	public void run() {
		System.out.println("Building connection with client# " + id + " at " + socket);
		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            out = new PrintWriter(socket.getOutputStream(),true);
           String username=in.readLine();
           String password=in.readLine();
           System.out.println(username+" "+password);
           MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
   		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
   		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;MyJson tempJson = new MyJson("{\""+ condition.fieldName +"\": \""+ condition.condition+"\"}");
		MyJson.typeCmp = recordMeta.get(condition.fieldName).type;
		MyJson.fieldNameCmp = condition.fieldName;nt = 0;nt = 0;nt = 0;nt = 0;nt = 0;sdfadfasfcccvvvsdfadfasfcccvvvsdfadfasfcccvvv           this.currentUser = mydb.login(username, password);
           
           
           if(currentUser==null) {
        	   out.println(WELCOMEWORD+"log in failed, please check your input");
        	   out.println("requestCLOSECLIENTsocket");
           }
           
           else{
            // Link the reference. JSON only deserialize to be object from the file, not ref.
        	if(currentUser.selectedDBname!=null) currentUser.selectedDB = mydb.useDB(currentUser.selectedDBname, out);
        	if(currentUser.selectedTableName!=null) currentUser.selectedTable = currentUser.useTable(currentUser.selectedTableName, out);
            loginOK=true;
            // Send a welcome message to the client.
            out.println(WELCOMEWORD+"Welcome User: " + currentUser.name);
            out.println(WELCOMEWORD+"Enter @ to quit");
           }

            String msg;
            // waiting for client to send message
            while (loginOK) {
                msg = in.readLine().trim();
                if (msg == null || msg.equals("@")) {
                    break;
                }
                
                //here to deal with the input msg and call function
                
                System.out.println("Message from client #" + currentUser.name + ", [" + msg + "]");
                inputmsgHandler(msg);
                myServer.userInputs.add(msg);
                myServer.updateText();
            }
        } catch (IOException e) {
            System.out.println("Error client# " + id + ": " + e);
        } finally {
            try {
            	if(currentUser!=null) {
            		currentUser.canLogin=true;
            		DBHelper.writeToDB(mydb);
            	}
            	
                socket.close();
            } catch (IOException e) {
                System.out.println("Client # :" + id + " ... Couldn't close a socket");
            }
            System.out.println("client# " + id + " left");
        }
		
		
	}
	
	
	
	/*
	 * *
	 * input string Handler write here
	 */
	public void inputmsgHandler(String msg){
		
		String[] commands = msg.split(" ");
		Condition condition;
		try{
		switch(commands[0].toLowerCase()){		
		case "create":
				switch(commands[1].toLowerCase()){
				//create user - command: create user (username) (password) (boolean isadmin);
				case "user":boolean isadmin = Boolean.parseBoolean(commands[4]);
							mydb.addUser(this.currentUser, new User(commands[2],commands[3],isadmin), out);
					break;
				//create database -command: create database (databaseName)
				case "database": mydb.createDB(this.currentUser, commands[2], out);
					break;
				//create table -command: create table (tableName) (fieldName:fieldtype(length)),(fieldName2:fieldtype2(length2))...
				case "table" :
					String tableName = commands[2];
					String inputargs = commands[3];
					String[] totalArgs = inputargs.split(",");
					String[] arg;
					ArguSet[] newArguSet = new ArguSet[totalArgs.length];
					for(int i=0; i<totalArgs.length;i++){
						arg = totalArgs[i].split("[:\\(\\)]");
						newArguSet[i] = new ArguSet();
						newArguSet[i].name = arg[0];
						newArguSet[i].type = arg[1];
						System.out.println(arg[2]);
						newArguSet[i].length = Integer.parseInt(arg[2]);
					}
					this.currentUser.createTable(tableName, out, newArguSet);

					break;
				
				}
			break;
			
		case "delete":
				switch(commands[1].toLowerCase()){
				//delete user - command: delete user (username)
				case "user": mydb.deleteUser(this.currentUser, commands[2], out);
					break;
					
				//delete user - command: delete database (databaseName);
				case "database": mydb.deleteDB(this.currentUser, commands[2], out);
					break;
				//delete table - command: delete table (tableName);
				case "table":
					if(currentUser.selectedTableName.equals(commands[2])){
						currentUser.selectedTable=null;
						currentUser.selectedTableName=null;
					}
					currentUser.removeTable(commands[2],out);
					break;
				//delete record - command: delete record where (fieldName) (=,>,<) (value);
				case "record":
							currentUser.deleteRecord(commands[4],commands[3],commands[5], out);
					break;
				}
			break;
			
			
		case "use":
			switch(commands[1].toLowerCase()){
			//use database - command: use database (databaseName)
			case "database":
				currentUser.selectedDB = mydb.useDB(commands[2], out);
				if(currentUser.selectedDB!=null) currentUser.selectedDBname = commands[2];
			break;
					
			case "table":
				currentUser.selectedTable = currentUser.useTable(commands[2], out);
			}
			break;
			//select (field1),(field2),(field3) where (fieldname) (=><) (val);
		case "select":
			condition = new Condition();
			condition.fieldName = commands[3];
			condition.comparer = commands[4];
			condition.condition = commands[5];
			currentUser.displayRecord(currentUser.searchRecord(condition, out), out, commands[1].split(","));
			
			
			break;
			
			//insert record - command: insert field1,field2,field3 value val1,val2,va3;
		case "insert":
			String []subfield = commands[1].split(",");
			String []subVal = commands[3].split(",");
			HashMap<String, String> insertMap = new HashMap<String,String>();
			for(int i=0; i<subfield.length;i++){
				insertMap.put(subfield[i], subVal[i]);				
			}
			currentUser.insertRecord(insertMap, out);
			
			break;
			
		case "update":
				switch(commands[1].toLowerCase()){
				//update user - command: update user (username) with (newUsername) (newPassword) (boolean isAdmin)
				case "user":
					boolean isadmin = Boolean.parseBoolean(commands[6]); 
					mydb.updateUser(this.currentUser, commands[2], new User(commands[4],commands[5],isadmin), out);
					break;
				//update record - command: update record (fieldname) with (newVal) where (fieldname2) (=><) (val);
				case "record":
					String fieldname = commands[2];
					String newVal = commands[4];
					condition = new Condition();
					condition.fieldName = commands[6];
					condition.comparer = commands[7];
					condition.condition = commands[8];
					currentUser.updateRecord(fieldname, newVal, condition, out);
					
					
					
				}
			break;

		}
		
		DBHelper.writeToDB(mydb);
		
		}catch(Exception e){
			out.println(WELCOMEWORD+"your input is not valid, please double check");
			e.printStackTrace();
		}
	}
	
	
	

}
