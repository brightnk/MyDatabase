import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
           currentUser = mydb.login(username, password);
           if(currentUser==null) out.println(WELCOMEWORD+"log in failed, please check your input");
           
           else{
            // (outputstream, autoflush)
            loginOK=true;
            // Send a welcome message to the client.
            out.println(WELCOMEWORD+"Welcome User: " + currentUser.name);
            out.println(WELCOMEWORD+"Enter @ to quit");
           }

            String msg;
            // waiting for client to send message
            while (loginOK) {
                msg = in.readLine();
                if (msg == null || msg.equals("@")) {
                    break;
                }
                
                //here to deal with the input msg and call function
                
                System.out.println("Message from client #" + id + ", [" + msg + "]");
                inputmsgHandler(msg);
                myServer.userInputs.add(msg);
                myServer.updateText();
            }
        } catch (IOException e) {
            System.out.println("Error client# " + id + ": " + e);
        } finally {
            try {
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
		
		
		
		out.println(WELCOMEWORD+"your input is not valid, please double check");
		
	}
	
	
	

}
