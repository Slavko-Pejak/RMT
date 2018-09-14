import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {

	/*
	 * The principle here is that a player can click on "play" and server will get him in a Play Room
	 * if there is already a room with only one player he will be added to that room and game can
	 * begin, if there are no Play Rooms with one player room will be created and player will be 
	 * added to that room and he would be the one who waits for another player to show up.
	 * Basically the server will do most of the work, which is probably a terrible idea if we
	 * consider that a large number of players could play the game. In that case server would 
	 * be vulnerable to DOS attacks. 
	 */
	
	public static void main(String[] args) {
		try {
			//Socket socket = new Socket("localhost", 16000);
			//Socket socket = new Socket("89.216.215.184", 16000); //ne radi MOJA
			//Socket socket = new Socket("192.168.1.94", 16000);
			//Socket socket = new Socket("fe80::dcd0:2540:eeea:df3%17", 16000);
			//Socket socket = new Socket("127.0.0.1", 16000);
			Socket socket = new Socket("89.216.18.149", 16000);
			Scanner scanner = new Scanner(System.in);
			Boolean end = false;
			String input;
			String serverAnswer;
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream printWriter = new PrintStream(socket.getOutputStream());
			
			while(!end) {
				System.out.println("'play'");
				System.out.println("'end'");
				input = null;
				scanner.reset();
				input = scanner.nextLine();
				switch (input) {
				case "play": //sending "play" to server
					//some GUI that says "searching..." or something like that
					System.out.println("Input: " + input);
					printWriter.flush();
					printWriter.print(input + "\n");
					serverAnswer = bufferedReader.readLine(); //server is answering on your request
					switch (serverAnswer) { //inner switch start
						case "waitingForOpponent": 
							 //some GUI that says that you created a room and you are waiting for an opponent
							System.out.println("Waiting for an opponent...");
							String start1 = bufferedReader.readLine(); //waiting for "start"
							System.out.println(start1);
							System.out.println("Game begins");
							//we need a timer
							//for now lets just do it without a timer
							System.out.println("Enter your word:");
							String myWord1 = scanner.nextLine();
							printWriter.println(myWord1);
							String gameResult1 = bufferedReader.readLine();
							System.out.println("Game result: " + gameResult1);
							//novo
							//printWriter.println("krajIgre");
							break;
						case "opponentFound": //game can start
							System.out.println("Game will start soon...");
							//game GUI
							//you don't have to worry about starting both game GUI's at same time because 
							//every player has for example 90 seconds to make a word
							//timer can start the moment every letter has been shown
							String start2 = bufferedReader.readLine(); //waiting for "start"
							System.out.println(start2);
							System.out.println("Game begins");
							//we need a timer
							//for now lets just do it without a timer
							System.out.println("Enter your word:");
							String myWord2 = scanner.nextLine();
							printWriter.println(myWord2);
							String gameResult2 = bufferedReader.readLine();
							System.out.println("Game result: " + gameResult2);
							break;
						default: //not sure if i need this, but let it stay for now
							break;
					}
					break; //inner switch end
					//end of "play"
				case "end":
					printWriter.print(input + "\n");
					bufferedReader.close();
					printWriter.close();
					end = true; 
					return;
					//break;
				default:
					System.out.println("Not a valid command!!!");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//nije bitan broj igraca, vec koji igrac prvi ponovo ukuca play
//zahtev igraca koji prvi u drugoj partiji ukuca play je ignorisan
//zahtev onog koji drugi posalje play je prihvacen
}