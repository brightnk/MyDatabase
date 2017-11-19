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
	private Socket connectionSock = null;
	private int id;
	public boolean ok_connect = true;
	
	public MyClient(MyClientGUI gui) throws UnknownHostException, IOException {
		this.myGui = gui;
		String hostname = DBConfig.HOSTNAME;
		int port = DBConfig.PORT;
		
		myGui.messages += "Connecting to server on port " + port +"\n";
		connectionSock = new Socket(hostname, port);
		InputStreamReader isr = new InputStreamReader(connectionSock.getInputStream());
		serverInput = new BufferedReader(isr);
		pw = new PrintWriter(connectionSock.getOutputStream(),true);
	}
	
	public void close() throws IOException {
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
			serverMsg = serverInput.readLine(); // assume that the id was the first message sent
			id = Integer.parseInt(serverMsg.split("#")[1].trim()); 
			while(true) {
				serverMsg = serverInput.readLine(); 
				if (serverMsg == null)
					break;
				myGui.messages += "Client# " + id + ", Received: " + serverMsg +"\n";
				myGui.updateMessage();
			}
		} catch (Exception ex) {
			ok_connect = false;
			//System.out.println(ex);
		}
	}
	
}
