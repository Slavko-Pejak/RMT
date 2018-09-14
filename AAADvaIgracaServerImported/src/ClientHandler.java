import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

	Socket socketForCommunication; //we will use it to make input and output streams so we can communicate with client
	private boolean inGame = false;
	
	public Thread gameThread = null;
	public String username; //ime igraca
	
	
	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public ClientHandler(Socket socketForCommunication) { //constructor, that's how we get our socket for communication
		super();
		this.socketForCommunication = socketForCommunication;
	}

	public Room checkingForAnExistingRoom() { //looking for a room with one players, if such room is found second player will be added, no need for delay
		//i haven't used for-each loop because i don't know if i can increase number of players in roomList like that
		for(int i=0; i < ServerMain.roomList.size(); i++) {
			if(ServerMain.roomList.get(i).getPlayersInRoom() == 1) {
				ServerMain.roomList.get(i).setSocketSecondPlayer(socketForCommunication); //setting second player socket
				ServerMain.roomList.get(i).setPlayersInRoom(2); //i think it is better to do it right here, there so no reason for delaying
				return ServerMain.roomList.get(i);
			}
		}
		return null;
	}
	
	public static Thread findRoomAndSetThread(Room newRoom){
		for(int i = 0; i < ServerMain.roomList.size(); i++){
			if(ServerMain.roomList.get(i).getRoomID().equals(newRoom.getRoomID())){
				if(ServerMain.roomList.get(i).getFirstPlayerClientHandler().gameThread != null){
					return ServerMain.roomList.get(i).getFirstPlayerClientHandler().gameThread;
				}
			}
		}
		return null;
	}
	
	public static void tellFirstPlayerAboutGamesEnd(Room gamePlayRoom){
		String firstPlayerUserName = gamePlayRoom.getFirstPlayerClientHandler().username;
		System.out.println("First player username: " + firstPlayerUserName);
		for(int i = 0; i < ServerMain.clientHandlerList.size(); i++){
			if(ServerMain.clientHandlerList.get(i).username.equals(firstPlayerUserName)){
				ServerMain.clientHandlerList.get(i).setInGame(false);
				System.out.println("We set in game to false!");
			}
		}
		/*
		for (ClientHandler ch : ServerMain.clientHandlerList) {
			if(ch.username.equals(firstPlayerUserName)){
				ch.inGame = false;
			}
		}
		*/
	}
	
	public static boolean daLiSobaJosUvekPostoji(Room justCreatedRoom){
		for(int i=0; i < ServerMain.roomList.size(); i++){
			if(ServerMain.roomList.get(i).getRoomID().equals(justCreatedRoom.getRoomID())){
				System.out.println(justCreatedRoom.getRoomID());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void run() {  //just like new main that is made just for that one player
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketForCommunication.getInputStream()));
			PrintStream printWriter = new PrintStream(socketForCommunication.getOutputStream());
			System.out.println(username);
			String message;
			boolean end = false;
			while(!end) { //server constantly getting messages and responding to them
				message = bufferedReader.readLine();
				System.out.println("Message is: " + message);
				switch (message) {
				case "play": 
					// 1. you need to check if there is already a room with one player waiting for an opponent
					Room room = checkingForAnExistingRoom(); // 1. is done here
					if(room == null) { //room with one player is not found
						Room newRoom = new Room();
						System.out.println(newRoom.getRoomID());
						newRoom.addFirstPlayerToRoom(socketForCommunication);
						newRoom.setFirstPlayerClientHandler(this);
						ServerMain.roomList.add(newRoom);
						System.out.println("Room created!");
						printWriter.println("waitingForOpponent");
						//novo
						//String porukaOKrajuPartije  = bufferedReader.readLine();
						//System.out.println(porukaOKrajuPartije);
						
						//provera da li soba postoji
						System.out.println("Soba postoji");
						boolean ok = true;
						while(ok){
							if(daLiSobaJosUvekPostoji(newRoom) == false){
								ok = false;
							}
						}
						System.out.println("Soba je unistena, kraj partije!");
					}else { //room with one player is found
						printWriter.println("opponentFound");
						room.setSecondPlayerClientHandler(this);
						GameplayHandler gameplayHandler = new GameplayHandler(room);//000!!!-------!!!000 //we are passing 'room' as an argument because it has 
						gameThread = new Thread(gameplayHandler);
						gameThread.start();
						System.out.println("Pre isAlive!");
						while(gameThread.isAlive()){
							//ne radi nista!!
						}
						System.out.println("Posle isAlive!");
						//obavesti prvog igraca
						//tellFirstPlayerAboutGamesEnd(room);
						//even this has to be done by looking room id | or not because you will pass the room object
					}
					// 2. if there is no such room, you should create one and put player who sent request in that room (you need to make sure you have his socket) (you are in client handler so you have it dummy)
					break;
				case "end":
					System.out.println("END");
					bufferedReader.close();
					printWriter.close();
					end = true;
					//think of something
					//keep in mind closing streams and that you are in thread not in main method
					break;
				default:
					System.out.println("This is not good! >>> \""+message+"\"");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
