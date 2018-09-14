import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

public class ServerMain {

	
	public static LinkedList<Room> roomList = new LinkedList<>(); //must be in class block, can not be in main method
	public static LinkedList<ClientHandler> clientHandlerList = new LinkedList<>(); //lista svih igraca
	
	public static String generisiID() { //method that is generating ID, later you need to check if it is unique
        boolean dobarBroj = false;
        String kod = "";
        for(int i=0;i<10;i++) {
            dobarBroj = false; //at beginning 'dobarBroj' is false, but in next iteration it will be true, so we need to set it again to false 
            int broj;
            do {
                Random rand = new Random();
                broj = 48 + rand.nextInt(75); //in ASCII first 48 symbols are some signs, and we don't want them, we want numbers, small and large letters
                if( (broj >= 58 && broj <= 64) || (broj >= 91 && broj <= 96)  ) { //just look at ASCII and you will see why we need this part of code
                    //does nothing, probably could and should be inverse but who gives a shit
                }else {
                    dobarBroj = true;
                }
            }while(!dobarBroj);
           
            char slovo = (char)broj;
            kod = kod + slovo +"";
        }
        return kod;
    }
	
	public static boolean checkingIfUserNameIsUnique(String newUserName) {
		for (ClientHandler ch: ServerMain.clientHandlerList) {
			if(ch.username.equals(newUserName)) {
				return false;
			}
		}
		return true;
	}
	
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
					
					boolean ok = false;
					while(!ok){
						String noviUserName = generisiID();
						if(checkingIfUserNameIsUnique(noviUserName)){
							ok = true;
							clientHandler.username = noviUserName;
						}
					}
					
					clientHandlerList.add(clientHandler);
					
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
