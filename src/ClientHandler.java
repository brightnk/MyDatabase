import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket socket;
    private int id;
    private MyServer myServer;

    public ClientHandler(Socket socket, int id, MyServer myServer) {
        this.socket = socket;
        this.id = id;
        this.myServer=myServer;
    }

	@Override
	public void run() {
		System.out.println("Building connection with client# " + id + " at " + socket);
		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);
           
            // (outputstream, autoflush)
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

            // Send a welcome message to the client.
            out.println("#"+id);
            out.println("Welcome Client # " + id);
            out.println("Enter @ to quit");
            

            String msg;
            // waiting for client to send message
            while (true) {
                msg = in.readLine();
                if (msg == null || msg.equals("@")) {
                    break;
                }
                System.out.println("Message from client #" + id + ", [" + msg + "]");
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

}
