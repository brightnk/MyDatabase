import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class MyClientGUI extends JFrame{
	
	JLabel labelUserName, labelPW, labelMessage;
	JTextField txtPW, txtUserName, txtSend;
	JTextArea txtMessage;
	JButton btnConnect, btnSend, btnDisconnect;
	MyClient myclient=null;
	String messages = "";
	public MyClientGUI(){
		super("My Client");
		this.setBounds(50, 50, 600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		labelUserName = new JLabel("UserName: ");
		this.addView(labelUserName, 50, 30, 100, 20);
		txtUserName = new JTextField();
		this.addView(txtUserName, 120, 30, 100, 20);
		labelPW = new JLabel("Password:");
		this.addView(labelPW, 50, 50, 100, 20);
		txtPW = new JTextField();
		this.addView(txtPW, 120, 50, 100, 20);
		btnConnect = new JButton("Connect");
		this.addView(btnConnect, 250, 50, 100, 20);
		btnDisconnect = new JButton("DisConnet");
		this.addView(btnDisconnect, 370, 50, 100, 20);
		txtSend = new JTextField();
		this.addView(txtSend,45, 450, 310, 30);
		btnSend = new JButton("Send");
		this.addView(btnSend, 350, 450, 100, 30);
		labelMessage = new JLabel("Messages:");
		this.addView(labelMessage, 50, 120, 100, 20);
		txtMessage = new JTextArea();
		txtMessage.setEditable(false);
		//always scroll to buttom when input
		((DefaultCaret)txtMessage.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane (txtMessage);
		this.addView(scroll, 50, 150, 500, 300);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		btnConnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = txtUserName.getText();
				String password = txtPW.getText();
				if(myclient==null ||myclient.connectionSock.isClosed()){
					
					messages ="";
					
						try {
							myclient = new MyClient(MyClientGUI.this, username, password);
							messages +="Connection made, try login.. \n";
							myclient.start();
							
							for( ActionListener al : btnSend.getActionListeners() ) {
								btnSend.removeActionListener( al );
							}
							
							
							btnSend.addActionListener(new ActionListener(){
								String userinput;
								@Override
								public void actionPerformed(ActionEvent e) {
									if(myclient.ok_connect){
										
										userinput = txtSend.getText().toString();
										if(userinput.equals("clear")){
											messages ="";
											updateMessage();
										}else{
										txtSend.setText("");
										messages +=userinput+"\n";
										myclient.sendData(userinput);
										updateMessage();
										}
									}
									
								}
								
							});
	
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.out.println("error: " + e1.getMessage());
						}finally{
							
						}
	
						
					
						updateMessage();
	
				}else{
					messages += "you already connected to the server \n";
					updateMessage();
				}
			}

		});
	
		btnDisconnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(myclient.ok_connect){
					try{
						
						myclient.close();
						messages +="You have disconnected to Server \n";
						updateMessage();
					}catch(Exception ex){
						System.out.println(ex);
					}
				}
				
			}

		});
	
	
	
	}
	
	public void updateMessage(){
		txtMessage.setText(messages);
	}

	public void addView(Component comp, int x, int y, int w, int h){
		
		this.add(comp);
		comp.setBounds(x,y,w,h);
				
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyClientGUI();
	}

}
