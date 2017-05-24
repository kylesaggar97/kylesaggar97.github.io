/*Kyle Saggar
 * creates the server for a tic tac toe game
 * sends back and forth information and maintains a board checking
 * to see when the game has ended
 * 
 */
import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	private char[][] board;					// the board
	private int turns;						// the total number of turns taken
	private char[] marks ={'X','O'};		// X is always the first player
	private BufferedReader[] readers;
	private PrintWriter[] writers;
	private ServerSocket server;

	public Server(){
		board = new char[3][3];
		readers = new BufferedReader[2];
		writers = new PrintWriter[2];
	}

	public void go(){
		
		//players sockets
		Socket sock1;
		Socket sock2;
		
		int playNum=0;
		
		//first player and second players sockets
		Socket play1;
		Socket play2;

		try{
			//Set up the server socket, print IP and port info 
			server=new ServerSocket(4242);
			System.out.println("IP: "+InetAddress.getLocalHost().getHostAddress());

			//Accept two players and set up the readers and writers
			sock1=server.accept();
			sock2=server.accept();

			//Randomly determine who goes first
			playNum=new Random().nextInt(2);

			if(playNum==0){

				play1=sock1;
				play2=sock2;
			}
			else{
				play1=sock2;
				play2=sock1;
			}
			
			//assigns readers and writers to the sockets
			readers[0]=(new BufferedReader(new InputStreamReader(play1.getInputStream())));
			readers[1]=(new BufferedReader(new InputStreamReader(play2.getInputStream())));
			writers[0]=(new PrintWriter(play1.getOutputStream()));
			writers[1]=(new PrintWriter(play2.getOutputStream()));

			//Get the names of the two players
			String player1Name=readers[0].readLine();
			String player2Name=readers[1].readLine();

			//Send the names to opposing player and indicate who goes first
			writeMessage(writers[0], STATUS.GO, player2Name);
			writeMessage(writers[1], STATUS.DRAW, player1Name);

			String move="";
			STATUS stat = STATUS.GO;
			
			main interaction loop
			do{
				//Get move from next player
				move=readers[turns%2].readLine();

				//Receives x and y coord
				int upOrDown=Integer.parseInt(move.substring(0,move.indexOf(",")));
				int leftOrRight=Integer.parseInt(move.substring(move.indexOf(",")+1));

				board[upOrDown][leftOrRight]=marks[turns%2];

				//figure out if the game should still go on
				stat=gameOver(marks[turns%2]);

				turns++;

				//send STATUS and opposing players move
				writeMessage(writers[turns%2],stat,move);

			}while(stat == STATUS.GO);

			//send the other player final info
			turns = (turns+1)%2;
			if(stat==STATUS.LOST)
				writeMessage(writers[turns],STATUS.WON,0+","+0);
			else
				writeMessage(writers[turns], stat,0+","+0);	
				
				server.close();

		}catch(IOException e){

			e.printStackTrace();
		}
	}

	//writes a socket with status, followed by a message. All seperated by commas.
	private void writeMessage(PrintWriter writer,STATUS stat, String message){

		writer.println(stat+","+message);
		writer.flush();
	}

	//determines if the game is over
	//sends back Lose or Draw if it is over
	//sends back GO if there is no winner
	private STATUS gameOver(char mark){

		STATUS stat=STATUS.GO;

		int col=0;
		
		//check vertically if the spots around spot are taken by the player 
		do{
			
			stat= checkVertical(mark, col);
			col++;
		}while(stat==STATUS.GO&&col<3);
		
		col=0;
		
		//checks if the spots horizontally are taken by the player
		while(stat==STATUS.GO&&col<3){

				stat= checkHoriz(mark, col);
				col++;
		}
		
		//checks both the diagonal
		if(board[1][1]==mark && stat==STATUS.GO){
			stat= checkDiagonal(mark);								
		}		
		
		//returns if the game is over or not
		if(stat==STATUS.WON)
			return STATUS.LOST;

		if(stat==STATUS.GO&&turns>=8)
			return STATUS.DRAW;

		return STATUS.GO;
	}

	//checks the vertical spot in the column returns true if all spots have been taken by the player
	private STATUS checkVertical(char symbol, int col ){

		int num=0;

		for(int row=0;row<board.length;row++){
			if(board[row][col]==(symbol)){
				num++;
			}
		}
		
		if(num==3){
			return STATUS.WON;
		}
		
		return STATUS.GO;
	}

	//checks the horizontal spots on the board
	private STATUS checkHoriz(char symbol, int row ){

		int num=0;

		for(int col=0;col<board.length;col++){
			if(board[row][col]==(symbol)){
				num++;
			}
		}
		
		if(num==3){
			return STATUS.WON;
		}
		
		return STATUS.GO;
	}

	//checks both diagonals
	private STATUS checkDiagonal(char Symbol){

		int num=0;

		for(int i=0;i<board.length;i++){
			if(board[i][i]==(Symbol)){
				num++;
			}
		}
		if(num==3){
			return STATUS.WON;
		}

		num=0;

		int row=0;
		int col=2;

		for(int s=0; s<board.length; s++){

			if(board[row][col]==(Symbol)){
				num++;
			}
			row++;
			col--;			
		}

		if(num==3){
			return STATUS.WON;
		}		

		return STATUS.GO;
	}

	public static void main(String[] args){
		new Server().go();
	}
}
