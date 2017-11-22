import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class MyClient extends Thread
{	
	private MyClientGUI myGui; 
	private BufferedReader serverInput = null ;
	private PrintWriter pw = null;
	public Socket connectionSock = null;
	private int id;
	public boolean ok_connect = true;
	
	public MyClient(MyClientGUI gui, String username, String password) throws UnknownHostException, IOException {
		this.myGui = gui;
		String hostname = DBConfig.HOSTNAME;
		int port = DBConfig.PORT;
		
		myGui.messages += "Connecting to server on port " + port +"\n";
		connectionSock = new Socket(hostname, port);
		InputStreamReader isr = new InputStreamReader(connectionSock.getInputStream());
		serverInput = new BufferedReader(isr);
		pw = new PrintWriter(connectionSock.getOutputStream(),true);
		pw.println(username);
		pw.println(password);
	}
	
	public void close() throws IOException {
		pw.println("@");
		pw.close();
		serverInput.close();
		connectionSock.close();
	}
	public void sendData(String msg) {
		pw.println(msg); 
	}
	
	public void run() {
		String serverMsg;
		// used to listen message from server
		try {
			
			while(true) {
				serverMsg = serverInput.readLine(); 
				if (serverMsg == null)
					break;
				else if(serverMsg.equals("requestCLOSECLIENTsocket")){
					this.close();
					break;
				}
				myGui.messages += serverMsg +"\n";
				myGui.updateMessage();
			}
		} catch (Exception ex) {
			ok_connect = false;
			//System.out.println(ex);
		}
	}
	
}
