import java.net.Socket;
import java.util.Random;

public class Room {
	private String roomID; //maybe it is better to make this a string, you can use your old method for generating ID's
	private Socket socketFirstPlayer;
	private Socket socketSecondPlayer;
	private int playersInRoom;
	
	
	//--------<CONSTRUCTOR>----------------
	public Room() {
		super();
		boolean ok = false;
		while(!ok) { //it will generate new roomID until that id is unique
			String newID = generisiID();
			if(checkingIfIdIsUnique(newID)) {
				this.roomID = newID;
				ok = true;
			}
		}
		 //you need a method that will make sure you have unique id
		this.playersInRoom = 0; //setting numbers of players in the room to 0 when room is created
	}
	//--------</CONSTRUCTOR>---------------
	
	
	//---------START OF GETTERS AND SETTERS------------------------
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public Socket getSocketFirstPlayer() {
		return socketFirstPlayer;
	}
	public void setSocketFirstPlayer(Socket socketFirstPlayer) {
		this.socketFirstPlayer = socketFirstPlayer;
	}
	public Socket getSocketSecondPlayer() {
		return socketSecondPlayer;
	}
	public void setSocketSecondPlayer(Socket socketSecondPlayer) {
		this.socketSecondPlayer = socketSecondPlayer;
	}
	public int getPlayersInRoom() {
		return playersInRoom;
	}
	public void setPlayersInRoom(int playersInRoom) {
		this.playersInRoom = playersInRoom;
	}
	//---------END OF GETTERS AND SETTERS-----------------------------
	
	public void addFirstPlayerToRoom(Socket playerSocket) {
		setSocketFirstPlayer(playerSocket);
		setPlayersInRoom(getPlayersInRoom() + 1);
	}
	public void addSecondPlayerToRoom(Socket playerSocket) {
		setSocketSecondPlayer(playerSocket);
		setPlayersInRoom(getPlayersInRoom()+1);
	}
	
	public String generisiID() { //method that is generating ID, later you need to check if it is unique
        boolean dobarBroj = false;
        String kod="";
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
	
	public boolean checkingIfIdIsUnique(String roomID) {
		for (Room r: ServerMain.roomList) {
			if(r.getRoomID().equals(roomID)) {
				return false;
			}
		}
		return true;
	}
}
