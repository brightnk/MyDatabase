import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class MyServer extends JFrame{
	
	JLabel labelPort, labelReceive, labelResult;
	JTextField txtPort;
	JTextArea txtReceive, txtResult;
	JButton btnBind;
	ServerSocket serverSock = null;
	Thread server;
	ArrayList<String> userInputs = new ArrayList<String>();
	
	public MyServer(){
		super("My Server");
		this.setBounds(50, 50, 500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		labelPort = new JLabel("Port: "+DBConfig.PORT);
		
		this.addView(labelPort, 50, 50, 150, 20);
		btnBind = new JButton("Start Server");
		this.addView(btnBind, 250, 50, 100, 20);
		labelReceive = new JLabel("Receive:");
		labelResult = new JLabel("Result:");
		this.addView(labelReceive, 50, 100, 50, 20);
		this.addView(labelResult, 250, 100, 50, 20);
		
		txtReceive = new JTextArea();
		txtReceive.setEditable(false);
		txtResult = new JTextArea();
		txtResult.setEditable(false);
		JScrollPane scroll1 = new JScrollPane (txtReceive);
		this.addView(scroll1, 50, 150, 150, 200);
	    scroll1.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    JScrollPane scroll2 = new JScrollPane (txtResult);
	    this.addView(scroll2, 250, 150, 150, 200);
	    scroll2.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		

	    
	    
	    
	    btnBind.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int port = DBConfig.PORT;
				if(serverSock != null){
					try {
						serverSock.close() ;
						
						server.interrupt();
					} catch (IOException exp) {
						// TODO Auto-generated catch block
						exp.printStackTrace();
					}
				}
				
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
		
	    String results = "";
	    for(int i=0; i<numbers.size();i++){
	    	results += Integer.toString(numbers.get(i))+ "\n";
	    }
		txtResult.setText(results);
		
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyServer();
	}

}
