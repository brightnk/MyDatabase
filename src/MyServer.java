import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class MyServer extends JFrame{
	
	
	JLabel labelPort, labelReceive;
	JTextField txtPort;
	JTextArea txtReceive, labelCommand;
	JButton btnBind;
	ServerSocket serverSock = null;
	Thread server;
	ArrayList<String> userInputs = new ArrayList<String>();
	MyDB mydb;
	String acceptableCommands = "*Notice the space in command, but no space around ','\n"
			+ "**() means it's a varaible, no need to key in--except the () around 'length'.\n\n"
			+ "create user - command: \ncreate user (username) (password) (boolean isadmin) \n"
			+ "create database -command: \ncreate database (databaseName) \n"
			+ "create table -command: \ncreate table (tableName) (fieldName:fieldtype(length)),(fieldName2:fieldtype2\n(length2))...\n"
			+ "delete user - command: \ndelete user (username) \n"
			+ "delete user - command: \ndelete database (databaseName) \n"
			+ "delete table - command: \ndelete table (tableName) \n"
			+ "delete record - command:\ndelete record where (fieldName) (=,>,<) (value) -one condition allowed so far\n"
			+ "use database - command: \nuse database (databaseName) \n"
			+ "search record - command: \nselect (field1),(field2),(field3) where (fieldname) (=><) (val) -one condition allowed so far\n"
			+ "insert record - command: \ninsert field1,field2,field3 value val1,val2,va3 \n"
			+ "update user - command: \nupdate user (username) with (newUsername) (newPassword) (boolean isAdmin) \n"
			+ "update record - command: \nupdate record (fieldname) with (newVal) where (fieldname2) (=><) (val)";
	
	public MyServer(){
		super("My Server");
		this.setBounds(50, 50, 600, 850);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		labelPort = new JLabel("Port: "+DBConfig.PORT);
		
		this.addView(labelPort, 50, 50, 150, 20);
		btnBind = new JButton("Start Server");
		this.addView(btnBind, 250, 50, 100, 20);
		labelReceive = new JLabel("Receive:");
		labelCommand = new JTextArea();
		labelCommand.setText(acceptableCommands);
		labelCommand.setEditable(false);
		this.addView(labelReceive, 50, 100, 250, 20);
		this.addView(labelCommand, 50, 330, 500, 450);
		
		txtReceive = new JTextArea();
		txtReceive.setEditable(false);
		JScrollPane scroll1 = new JScrollPane (txtReceive);
		this.addView(scroll1, 50, 120, 500, 200);
	    scroll1.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

		

	    
	    
	    
	    btnBind.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int port = DBConfig.PORT;
				//check if already online
				if(serverSock != null){
					try {
						serverSock.close() ;
						
						server.interrupt();
					} catch (IOException exp) {
						// TODO Auto-generated catch block
						exp.printStackTrace();
					}
				}
				
				
				mydb = DBHelper.connectToDB();
				
			    server = new Thread(){
			    	public void run(){
			    		try
						{
							final int PORT = port;
							System.out.println("Waiting for a connection on port " +PORT);
							serverSock = new ServerSocket(PORT);
							Socket connectionSock;
							
							int id = 0;
							Thread t;
							ClientHandler ch ; 
							while(true) {
								
								System.out.println("Waiting for a client");
								connectionSock = serverSock.accept();
								id++;
								System.out.println("Server welcomes client # : " + id);
								ch = new ClientHandler(connectionSock,id, MyServer.this);
								t = new Thread(ch);
								t.start();
							}		
						}
						catch (IOException linke)
						{
							System.out.println(linke.getMessage());
						}
					
			    	}
			    };
				
			    server.start();				
			}
    	
	    });
		
		
	}
	
	
	public void addView(Component comp, int x, int y, int w, int h){
		
		this.add(comp);
		comp.setBounds(x,y,w,h);
				
	}
	
	public void updateText(){
		String received = "";
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=0; i<userInputs.size();i++){
			received += userInputs.get(i) + "\n";
			
			try{
				numbers.add(Integer.parseInt(userInputs.get(i)));
			}catch(Exception other){}

		}
		
		txtReceive.setText(received);
	    Collections.sort(numbers);
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyServer();
	}

}
