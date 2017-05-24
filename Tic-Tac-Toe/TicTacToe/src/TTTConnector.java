/*Kyle Saggar
Establishes a connection between the two clients
so data can be sent back and forth between the two
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Connection GUI for TTT
public class TTTConnector extends JFrame implements ActionListener{

	private JTextField ipAddress;
	private JTextField port;
	private JTextField playerName;
	private JButton connectButton;
	
	public TTTConnector(){
		
		setSize(300,200);
		setTitle("TTT Connection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		getContentPane().setBackground(Color.white);
		
		JLabel ipLabel = new JLabel("IP Adress: ");
		ipLabel.setBounds(40,30,80,30);
		
		JLabel portLabel = new JLabel("Port #: ");
		portLabel.setBounds(40,55,80,30);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBounds(40,80,80,30);
		
		add(ipLabel);
		add(portLabel);
		add(nameLabel);
		
		ipAddress = new JTextField(20);
		ipAddress.setBounds(130,37,110,17);
		
		port = new JTextField(20);
		port.setBounds(130,63,110,17);
		
		playerName = new JTextField(20);
		playerName.setBounds(130,88,110,17);
		
		add(ipAddress);
		add(port);
		add(playerName);
		
		connectButton = new JButton("Connect");
		connectButton.setBounds(90,120,100,15);
		connectButton.addActionListener(this);
		
		add(connectButton);
		this.getRootPane().setDefaultButton(connectButton);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ipAddress.getText().equals("")|| port.getText().equals("")||playerName.getText().equals("")){
			JOptionPane.showMessageDialog(null, "You must fill in all fields!","*Sigh*",JOptionPane.ERROR_MESSAGE);
		}
		else{
			//Launch the TTT Client
			 SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	new TTTClient(ipAddress.getText(),playerName.getText(),Integer.parseInt(port.getText()));
		            }
		        });
		
						
			
			//get rid of the TTT Connector GUI
			this.dispose();
		}
	}
	
	public static void main(String[] args){
		new TTTConnector();
	}
	
}
