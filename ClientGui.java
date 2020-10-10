
//import java.io.*;
//import java.net.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import java.awt.Graphics;
import java.awt.GridLayout;
//import java.awt.Color;
//import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
//import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements ActionListener {
    private JTextField ipAddText;
    private JTextField portNoText;
    private JButton connectButton;
    private JButton sendButton;
    private JButton uButton;
    private JButton sButton;
    private JButton gButton;
    private JButton rButton;
    private final String conButton = "Connect";
    private final String disconButton = "Disconnect";
    protected static JTextArea serverMsg;
    private JTextArea userMsg;

    //private String textIP;
    //private int textPort;
    private JPanel panel;
    private JPanel secondaryPanelOne;
    private JPanel secondaryPanelTwo;
    private JPanel buttonPanel;

    private JLabel ipLabel;
    private JLabel portLabel;

    private static  Socket clientSocket = null;
    private PrintWriter out = null;
    private static BufferedReader in = null;
    
    private static boolean connected=false;
    public ClientGUI() {
        this.panel = new JPanel(new GridLayout(3, 1));
        this.secondaryPanelOne = new JPanel(new GridLayout(1, 5));
        this.secondaryPanelTwo = new JPanel(new GridLayout(1, 2));
        this.buttonPanel = new JPanel(new GridLayout(4, 1));
        // this.ipAddText = new JTextField(10);
        // this.portNoText = new JTextField(5);

        this.ipLabel = new JLabel("IP Address");
        this.ipAddText = new JTextField(20);
        this.portLabel = new JLabel("Port");
        this.portNoText = new JTextField(4);
        this.connectButton = new JButton(conButton);
        this.sendButton = new JButton("Send Message");
        this.userMsg = new JTextArea();
        ClientGUI.serverMsg = new JTextArea();
        
        // this.serverMsg = new JTextArea(10, 30);
        // this.serverMsg.setEditable(false);
        addActionListeners();

        this.secondaryPanelOne.add(this.ipLabel);
        this.secondaryPanelOne.add(this.ipAddText);
        this.secondaryPanelOne.add(this.portLabel);
        this.secondaryPanelOne.add(this.portNoText);
        this.secondaryPanelOne.add(this.connectButton);

        this.sButton = new JButton("SUBMIT");
        this.uButton = new JButton("UPDATE");
        this.gButton = new JButton("GET");
        this.rButton = new JButton("REMOVE");

        this.buttonPanel.add(this.sButton);
        this.buttonPanel.add(this.uButton);
        this.buttonPanel.add(this.gButton);
        this.buttonPanel.add(this.rButton);

        this.secondaryPanelTwo.add(this.buttonPanel);
        this.secondaryPanelTwo.add(this.userMsg);
        this.secondaryPanelTwo.add(ClientGUI.serverMsg);

        

        this.panel.add(this.secondaryPanelOne);
        this.panel.add(this.secondaryPanelTwo);
        this.panel.add(this.sendButton);
        this.add(this.panel);
        this.setSize(800, 600);
        this.setVisible(true);
       
        
    }

    void addActionListeners() {
        this.connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipAddText.getText();
                int port = Integer.parseInt(portNoText.getText());
                if (connectButton.getText() == conButton && connectToPort(ip, port)) {
                    connectButton.setText(disconButton);
                } else if (connectButton.getText() == disconButton) {
					/*
					 * try { out.close(); // change variables in.close(); clientSocket.close(); }
					 * catch (IOException e1) { e1.printStackTrace(); }
					 */
                    connectButton.setText(conButton);
                }
            }

        });

        this.sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {		
                String message = userMsg.getText();
                if((!(message.isBlank())) && isConnected()) {
                	//System.out.println("Message: "+message);
                    out.write(message +"\n");
                    out.flush();
                }
                //out.write("\n");
            }
        });
    }
    /*
     * public void actionPerformed(ActionEvent e) { if(e.getSource() == sendButton)
     * { new postDialog();
     * 
     * } else if (e.getSource() == rButton) { if (this.rbutton.getText()==
     * conButton) { try {
     * 
     * 
     * } } } }
     */

    // Socket s = null;
    // PrintWriter p = null;
    // BufferedReader b = null;

    // try {
    // s = new Socket();
    // System.out.println("Connected");

    // p = new PrintWriter(s.getOutputStream(), true);
    // b = new BufferedReader(new InputStreamReader(s.getInputStream()));

    // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    // String fromServer;
    // String str;
    // while ((fromServer = b.readLine()) != null) {
    // System.out.println("Server: " + fromServer);
    // if (!fromServer.equals("close"))
    // break;

    // str = stdIn.readLine();
    // if (str != null) {
    // System.out.println("Client: " + str);
    // System.out.println(str);

    // }
    // }
    // b.close();
    // stdIn.close();
    // s.close();
    // } catch (UnknownHostException e) {
    // System.err.println("x ");
    // System.exit(1);
    // } catch (IOException e) {
    // System.err.println("Error");
    // System.exit(1);
    // }

    public static void main(String args[]) {
        try {
            new ClientGUI();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Client built");
        
        //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        /*try {
			in = new BufferedReader(new InputStreamReader(ClientGUI.clientSocket.getInputStream()));
		} catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }*/
        while(!(ClientGUI.connected)) {
        }
        receive(ClientGUI.in);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    Boolean connectToPort(String ip, int port) {
    	
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Reader and Writer built");
            connected=true;
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Host is unknown");
        } catch (IOException e) {
            System.err.println("I/O invalid for socket connection");
        }
        return false;
    }

    static Boolean isConnected() {
        return !clientSocket.isClosed();
    }
    public static void receive(BufferedReader in){
        while(isConnected()){}
            String serverComm;
            System.out.println("Receiving");
                try {
                    while(in.ready()) {
                        serverComm = in.readLine().trim();
                        if(!(serverComm.isEmpty())) {
                        if(serverComm.equalsIgnoreCase("WIPE")) {
                            serverMsg.setText("");
                        }else if(serverComm.equalsIgnoreCase("SPACE")){
                            serverMsg.append("\n");
                        }
                            else {
                            System.out.println("Server: "+serverComm);
                            serverMsg.append(serverComm+ "\n");
                            }
                        }
                            
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
            }
    }
}
