import java.io.*;
import java.net.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ClientGui extends JFrame implements ActionListener {
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
    protected JTextArea serverMsg;
    private JTextArea userMsg;

    private String textIP;
    private int textPort;
    private JPanel panel;
    private JPanel secondaryPanelOne;
    private JPanel secondaryPanelTwo;
    private JPanel buttonPanel;

    private JLabel ipLabel;
    private JLabel portLabel;

    private Socket kkSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public ClientGui() {
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
        this.userMsg = new JTextArea();
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

        this.sendButton = new JButton("Send Message");

        this.panel.add(this.secondaryPanelOne);
        this.panel.add(this.secondaryPanelTwo);
        this.panel.add(this.sendButton);
        this.add(this.panel);
        this.setSize(400, 300);
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
                    try {
                        out.close();  // change variables
                        in.close();
                        kkSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    connectButton.setText(conButton);
                }
            }

        });

        this.sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String message = userMsg.getText();
                if(message!= "" && isConnected()) {
                    out.write(message);
                    out.flush();
                }
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
            new ClientGui();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    Boolean connectToPort(String ip, int port) {
        try {
            kkSocket = new Socket(ip, port);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: taranis.");
            System.exit(1);
        }
        return false;
    }

    Boolean isConnected() {
        return !kkSocket.isClosed();
    }
}
