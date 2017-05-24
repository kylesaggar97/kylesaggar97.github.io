/*Kyle Saggar
 * tic tac toe client handles receiving messages from the server with a thread
 * updates the board with their move and he move sent from the server
 * sends the server its move aswell
 */
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;

//Handles Client input (hoping for single-threaded)

public class TTTClient extends JFrame {

	private boolean myTurn;				// is it the user's turn
	private JLabel label;				// displays status info
	private Panel[][] panels;			// stores the x and o pics

	private String oppName;				// used for output purposes
	private String myPiece;				// either "x.jpg" or "o.jpg"

	private String oppPiece;
	private String myName;				// for debugging purposes

	private PrintWriter writer;			// used to write out to the server
	private BufferedReader reader;		//used to read from the server
	private Socket theSock;				
	private STATUS stat;				//hacking swing

	public TTTClient(String ip, String name, int port) {

		myName = name;
		this.setLayout(null);

		//create the board
		setSize(600,600);
		setTitle("Tic Tac Toe - "+name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates grid of tic tac toe board
		JPanel grid= new JPanel();
		grid.setBounds(0, 0, 600, 500);
		grid.setLayout(new GridLayout(3,3,5,5));
		grid.setBackground(Color.black);
		add(grid);

		//creates panel for label to be in
		JPanel labelPanel=new JPanel();
		labelPanel.setBounds(0, 500, 600, 100);
		labelPanel.setBackground(new Color(83,199,237));
		add(labelPanel);

		//creates label to output whos turn it is and who won
		label=new JLabel();
		labelPanel.add(label);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 18));
		label.setBounds(250, 570, 100, 30);


		panels=new Panel[3][3];

		//creates panels in a matrix to represent tic tac toe board
		for(int i = 0; i < 3; i ++){

			for(int k = 0; k < 3; k ++){

				panels[i][k] = new Panel(i,k);
				panels[i][k].setBackground(Color.white);
				grid.add(panels[i][k]);
			}

		}

		String input = "";
		try {

			//Set up the sockets, reader and writer
			theSock = new Socket ( ip , port );
			reader = new BufferedReader ( new InputStreamReader ( theSock.getInputStream() ) );
			writer = new PrintWriter ( theSock.getOutputStream() );

			//sends the server the player's name
			writeMessage ( name );

			//gets input from server
			input = reader.readLine();

		} catch (IOException e) {

			e.printStackTrace();
		}

		//gets Opp. Player's Name, received from server
		//gets from server if they go first and if they are x or o
		int commaLoc =  input.indexOf( ',' );
		oppName = input.substring( commaLoc+1 );
		stat = STATUS.valueOf( input.substring( 0 , commaLoc ) );
		
		
		if ( stat == STATUS.GO ) {

			myPiece = "x.jpg";
			oppPiece = "o.jpg";
			displayMessage ( "It's your turn." );
			myTurn=true;
		}
		else {

			myPiece = "o.jpg";
			oppPiece = "x.jpg";
			myTurn=false;
		}

		//launches the thread for game reading
		System.out.print("THREAD LAUNCHED");
		Thread t = new Thread(new ReadHandler());
		t.start();
		setVisible(true);
	}

	// converts a string value to it's enum counterpart
	private STATUS getStatus(String value){
		return STATUS.values()[Integer.parseInt(value)];
	}

	//sends a message to the server
	private void writeMessage(String message){

		writer.println ( message );
		writer.flush ();
	}

	//properly updates the JLabel via the inner classes
	//only need to update JLabel this way for the inner class.
	public void displayMessage(final String message){

		if(stat == STATUS.GO){
			SwingUtilities.invokeLater(
					new Runnable(){

						public void run(){
							label.setText(message);
						}
					}
					);
		}
		else{
			try{
				SwingUtilities.invokeAndWait(
						new Runnable(){

							public void run(){
								label.setText(message);
							}
						}
						);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	//	checks if anyone has won based on the passed in panel

	class Panel extends JPanel implements MouseListener{

		private BufferedImage image;
		private int w,h;		//used for drawing the pic
		private boolean empty;

		private int row;		// the row of the TTT matrix element
		private int col;		// the col of the TTT matrix element

		public Panel(int r, int c){
			row = r;
			col = c;

			empty = true;
			this.addMouseListener(this);
		}


		//changes the panel to a given file pic
		private void changePic(String fname){

			try {

				image = ImageIO.read(new File(fname));
				w = image.getWidth();
				h = image.getHeight();

			} catch (IOException ioe) {
				System.out.println(ioe);
				System.exit(0);
			}


		}

		//only handle an event when clicked
		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		//If it's the user's turn and the cell is free
		//Sends the selection to the server
		//updates the pic and label
		public void mouseClicked(MouseEvent e) {

			if ( empty && myTurn ) {

				changePic ( myPiece );
				writeMessage ( row + "," + col );
				repaint();
				empty=false;
				myTurn = false;
				displayMessage ( "It's " + oppName + "'s turn." );
			}
		}

		public Dimension getPreferredSize() {

			return new Dimension(w,h);
		}

		//this will draw the image
		//repaint calls this method (when it's ready)
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			if(!empty)
				g.drawImage(image,0,0,this);
		}

	}

	//Handles reading messages from the server
	public class ReadHandler implements Runnable{

		public void  run(){

			String message;
			stat = STATUS.GO;

			String input = null;

			try {

				//Receives messages while stat is go
				do{
					
					//changes GUI message
					if(!myTurn) 
						displayMessage ( "It's " + oppName + "'s turn." );
					else
						displayMessage ( "It's your turn." );

					//Waits for server message to change panel for opponent.
					input = reader.readLine();

					int firstComma = input.indexOf( "," );

					stat = STATUS.valueOf( input.substring( 0 , firstComma) );

					int leftOrRight = Integer.parseInt( input.substring( firstComma + 1 , firstComma + 2 ) );

					int upOrDown = Integer.parseInt( input.substring( firstComma + 3 ) );

					Panel update=panels[leftOrRight][upOrDown];
					
					//if current panel is empty updates the board
					if(update.empty){
						
						update.changePic( oppPiece );
						update.empty=false;
						repaint();
					}
					
					myTurn = true;

				}while(stat == STATUS.GO);

			} catch (IOException e) {

				e.printStackTrace();
			}

			//Determine how to update the label for win, lose or draw
			if ( stat == STATUS.WON )
				displayMessage ( "You won!" );
			else if(stat==STATUS.DRAW)
				displayMessage ( "DRAW!" );
			else
				displayMessage ( oppName + " won!" );
			
			try{
				reader.close();
				theSock.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			return;
		}
	}
}
