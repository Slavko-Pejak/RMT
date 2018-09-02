import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerMain {

	
	public static LinkedList<Room> roomList = new LinkedList<>(); //must be in class block, can not be in main method
	
	public static void main(String[] args) { //start of main method
		ServerSocket serverSocket;
		Socket socket;
		
		LinkedList<Socket> players = new LinkedList<>(); //not sure if will need this
		
		
			try {
				serverSocket = new ServerSocket(16000);
				System.out.println("Server started!");
				while(true) {
					socket = serverSocket.accept();
					ClientHandler clientHandler = new ClientHandler(socket);
					Thread thread = new Thread(clientHandler);
					thread.start();				
					System.out.println("New player added!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
