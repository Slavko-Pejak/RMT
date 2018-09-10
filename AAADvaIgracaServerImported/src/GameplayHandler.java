import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

public class GameplayHandler implements Runnable {
	
	private Room gameplayRoom;
	//public ClientHandler clientHandler; //000!!!-------!!!000
	
	
	public GameplayHandler(Room gameplayRoom/*, ClientHandler ch*/) { //000!!!-------!!!000
		super();
		this.gameplayRoom = gameplayRoom;
		//clientHandler = ch; //000!!!-------!!!000
	}
	
	public static void destroyRoom(Room gameplayRoom){
		for(int i = 0; i < ServerMain.roomList.size(); i++){
			if(ServerMain.roomList.get(i).getRoomID().equals(gameplayRoom.getRoomID())){
				ServerMain.roomList.remove(i);
			}
		}
	}


	@Override
	public void run() {
		try {
			gameplayRoom.getFirstPlayerClientHandler().gameThread = Thread.currentThread();
			
			//streams for communicating with first player
			BufferedReader bufferedReaderFirstPlayer = new BufferedReader(new InputStreamReader(gameplayRoom.getSocketFirstPlayer().getInputStream()));
			PrintStream printWriterFirstPlayer = new PrintStream(gameplayRoom.getSocketFirstPlayer().getOutputStream());
			
			
			//streams for communicating with second player
			BufferedReader bufferedReaderSecondPlayer = new BufferedReader(new InputStreamReader(gameplayRoom.getSocketSecondPlayer().getInputStream()));
			PrintStream printWriterSecondPlayer = new PrintStream(gameplayRoom.getSocketSecondPlayer().getOutputStream());
			
			//now the game can begin
			
			printWriterFirstPlayer.println("start1");
			printWriterSecondPlayer.println("start2");
			
			String firsPlayerWord = bufferedReaderFirstPlayer.readLine();
			String secondPlayerWord = bufferedReaderSecondPlayer.readLine();
			
			GameplayHandler.destroyRoom(gameplayRoom);  //destroying room after the game
			
			if(firsPlayerWord.length() > secondPlayerWord.length()) {
				printWriterFirstPlayer.println("victory");
				printWriterSecondPlayer.println("defeat");
			}else if(firsPlayerWord.length() < secondPlayerWord.length()) {
				printWriterFirstPlayer.println("defeat");
				printWriterSecondPlayer.println("victory");
			}else if( firsPlayerWord.length() == secondPlayerWord.length()){
				printWriterFirstPlayer.println("draw");
				printWriterSecondPlayer.println("draw");
			}
			
			
			//clientHandler.inGame = false; //000!!!-------!!!000
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
