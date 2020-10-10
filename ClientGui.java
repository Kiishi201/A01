import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
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
    private JTextField isbnField;
    private JLabel isbnLabel;
    private JTextField titleTextField;
    private JLabel titleLabel;
    private JTextField authorField;
    private JLabel authorLabel;
    private JTextField publisherField;
    private JLabel publisherLabel;
    private JTextField yearField;
    private JLabel yearLabel;
    private JTextArea entr;

    private JPanel panel;
    private JPanel secondaryPanelOne;
    private JPanel secondaryPanelTwo;
    private JPanel inputPanel;
    private JPanel buttonPanel;

    private JLabel ipLabel;
    private JLabel portLabel;

    private static  Socket clientSocket = null;
    private PrintWriter out = null;
    private static BufferedReader in = null;
    private static boolean connected=false;
    public ClientGUI() {
        this.panel = new JPanel(new GridLayout(4, 1));
        this.secondaryPanelOne = new JPanel(new GridLayout(1, 5));
        //this.secondaryPanelTwo = new JPanel(new GridLayout(1, 2));
        this.secondaryPanelTwo = new JPanel(new GridLayout(5, 3));
        this.inputPanel = new JPanel(new GridLayout(5, 2));
        this.buttonPanel = new JPanel(new GridLayout(4, 1));
        // this.ipAddText = new JTextField(10);
        // this.portNoText = new JTextField(5);

        this.ipLabel = new JLabel("IP Address");
        this.ipAddText = new JTextField(20);
        this.portLabel = new JLabel("Port");
        this.portNoText = new JTextField(4);
        this.connectButton = new JButton(conButton);
        this.userMsg = new JTextArea();
        userMsg.setBackground(Color.RED);
        //ClientGUI.serverMsg = new JTextArea();
        //ClientGUI.serverMsg.setBackground(Color.RED);
        this.isbnLabel = new JLabel("Isbn");
        this.isbnField = new JTextField(20);
        this.titleLabel = new JLabel("Title");
        this.titleTextField = new JTextField(20);
        this.authorLabel = new JLabel("Author");
        this.authorField = new JTextField(20);
        this.publisherLabel = new JLabel("Publisher");
        this.publisherField = new JTextField(20);
        this.yearLabel = new JLabel("Year");
        this.yearField = new JTextField(20);
        this.entr = new JTextArea();

        this.sButton = new JButton("SUBMIT");
        this.uButton = new JButton("UPDATE");
        this.gButton = new JButton("GET");
        this.rButton = new JButton("REMOVE");

        this.sendButton = new JButton("Send Message");

        // this.serverMsg = new JTextArea(10, 30);
        // this.serverMsg.setEditable(false);

        this.secondaryPanelOne.add(this.ipLabel);
        this.secondaryPanelOne.add(this.ipAddText);
        this.secondaryPanelOne.add(this.portLabel);
        this.secondaryPanelOne.add(this.portNoText);
        this.secondaryPanelOne.add(this.connectButton);

        addActionListeners();

		/*
		 * this.buttonPanel.add(this.sButton); this.buttonPanel.add(this.uButton);
		 * this.buttonPanel.add(this.gButton); this.buttonPanel.add(this.rButton);
		 */
        this.secondaryPanelTwo.add(this.sButton);
        this.secondaryPanelTwo.add(this.isbnLabel);
        this.secondaryPanelTwo.add(this.isbnField);
        this.secondaryPanelTwo.add(this.uButton);
        this.secondaryPanelTwo.add(this.titleLabel);
        this.secondaryPanelTwo.add(this.titleTextField);
        this.secondaryPanelTwo.add(this.gButton);
        this.secondaryPanelTwo.add(this.authorLabel);
        this.secondaryPanelTwo.add(this.authorField);
        this.secondaryPanelTwo.add(this.rButton);
        this.secondaryPanelTwo.add(this.publisherLabel);
        this.secondaryPanelTwo.add(this.publisherField);
        this.secondaryPanelTwo.add(new JLabel(""));
        this.secondaryPanelTwo.add(this.yearLabel);
        this.secondaryPanelTwo.add(this.yearField);
		/*
		 * this.inputPanel.add(this.isbnLabel); this.inputPanel.add(this.isbnField);
		 * this.inputPanel.add(this.titleLabel);
		 * this.inputPanel.add(this.titleTextField);
		 * this.inputPanel.add(this.authorLabel); this.inputPanel.add(this.authorField);
		 * this.inputPanel.add(this.publisherLabel);
		 * this.inputPanel.add(this.publisherField);
		 * this.inputPanel.add(this.yearLabel); this.inputPanel.add(this.yearField);
		 */
        
        
        
        
        
        
        
       
        

		/*
		 * this.secondaryPanelTwo.add(this.buttonPanel);
		 * this.secondaryPanelTwo.add(this.inputPanel);
		 */

        this.panel.add(this.secondaryPanelOne);
        this.panel.add(this.secondaryPanelTwo);
        this.panel.add(this.userMsg);
        //this.panel.add(ClientGUI.serverMsg);
        this.panel.add(this.sendButton);
        this.add(this.panel);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    // connect
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
                        out.close(); // change variables
                        in.close();
                        clientSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    connectButton.setText(conButton);
                }
            }

        });
        // send
        this.sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String message = userMsg.getText();
                if (message != "" && isConnected()) {
                    out.write(message);
                    out.flush();
                }
            }
        });
        // SUBMIT
        this.sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean realYear = checkYear(yearField.getText());

                if (checkISBN(isbnField.getText()) && realYear) {
                    checkMsg(isbnField, titleTextField, authorField, publisherField, yearField);

                    String finalISBN = isbnField.getText().replaceAll("-", "");
                    String line = "SUBMIT\n".concat(outputLine(finalISBN, titleTextField.getText(), authorField.getText(), publisherField.getText(), yearField.getText()));
                    try {
                        out.println(line);
                        System.out.println("out: "+line);
                        Scanner scn = new Scanner(clientSocket.getInputStream());

                        while (scn.hasNextLine())
                            entr.setText(scn.nextLine());
                        scn.close();

                        clientSocket = new Socket(ipAddText.getText(), Integer.parseInt(portNoText.getText()));
                        out = new PrintWriter(clientSocket.getOutputStream(), true);

                    } catch (Exception ex) {
                        System.out.println(ex);

                    }
                } else {
                    // if()
                    entr.setText("Error");
                    System.out.println("GUI submit error");
                }
            }
        });
        // UPDATE
        this.uButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (checkYear(yearField.getText())) {
                    checkMsg(isbnField, titleTextField, authorField, publisherField, yearField);

                    String finalISBN = isbnField.getText().replaceAll("-", "");
                    String line = "UPDATE\n".concat(outputLine(finalISBN, titleTextField.getText(), authorField.getText(), publisherField.getText(), yearField.getText()));
					/*
					 * String line = "Update" + ";" + finalIsbn + ";" + titleTextField.getText() +
					 * ";" + authorField.getText() + ";" + publisherField.getText() + ";" +
					 * yearField.getText() + "\r\n";
					 */

                    try {
                        out.println(line);
                        System.out.println("out: "+line);
                        Scanner scn = new Scanner(clientSocket.getInputStream());
                        while (scn.hasNextLine())
                            entr.setText(scn.nextLine());

                        clientSocket = new Socket(ipAddText.getText(), Integer.parseInt(portNoText.getText()));
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        scn.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                } else {
                    entr.setText("Invalid");
                }

            }

        });
        // GET
        this.gButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkMsg(isbnField, titleTextField, authorField, publisherField, yearField);
                String finalISBN = isbnField.getText().replaceAll("-", "");
                String line = "GET\n".concat(outputLine(finalISBN, titleTextField.getText(), authorField.getText(), publisherField.getText(), yearField.getText()));
				/*
				 * String line = "Get" + ";" + finalIsbn + ";" + titleTextField.getText() + ";"
				 * + authorField.getText() + ";" + publisherField.getText() + ";" +
				 * yearField.getText() + "\r\n";
				 */
                try {
                    out.println(line);
                    System.out.println("out: "+line);
                    String newline;

                    Scanner scn = new Scanner(clientSocket.getInputStream());
                    entr.setText("");
                    while (scn.hasNextLine()) {
                        newline = scn.nextLine();
                        System.out.println(newline);
                        entr.append("\n");
                        entr.append(newline);
                    }
                    scn.close();
                    clientSocket = new Socket(ipAddText.getText(), Integer.parseInt(portNoText.getText()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        // REMOVE
        this.rButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "warning", "!", dialogButton);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    checkMsg(isbnField, titleTextField, authorField, publisherField, yearField);

                    String finalISBN = isbnField.getText().replaceAll("-", "");
                    String line = "REMOVE\n".concat(outputLine(finalISBN, titleTextField.getText(), authorField.getText(), publisherField.getText(), yearField.getText()));
					/*
					 * String line = "Remove" + ";" + finalIsbn + ";" + titleTextField.getText() +
					 * ";" + authorField.getText() + ";" + publisherField.getText() + ";" +
					 * yearField.getText() + "\r\n";
					 */
                    try {
                        out.println(line);
                        System.out.println("out: "+line);
                        Scanner scn = new Scanner(clientSocket.getInputStream());
                        while (scn.hasNextLine())
                            entr.setText(scn.nextLine());
                        scn.close();

                        clientSocket = new Socket(ipAddText.getText(), Integer.parseInt(portNoText.getText()));
                        out = new PrintWriter(clientSocket.getOutputStream(), true);

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                } else {
                    entr.append("cancelled");
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

    public boolean checkYear(String yearField) {
        if (yearField==null || yearField.isEmpty()|| yearField.equalsIgnoreCase("N/A") ) {
            return true;
        } else {
            try {
                int no = Integer.parseInt(yearField);
                if (no >= 0) {
                    return true;

                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;

            }
        }
    }

    public boolean checkISBN(String isbnField) {
        boolean bool = false;
        int num = 0;
        if (isbnField.compareTo("") == 0) {
            bool = false;
        }
        String realnum = isbnField.replaceAll("-", "");
        if (realnum.length() != 13) {
            bool = false;
        } else {
            if (!realnum.matches("[a-bC-D]+")) {
                for (int x = 0; x < 12; x++) {
                    int number = Integer.parseInt(realnum.substring(x, x + 1));
                    num += (x % 2 == 0) ? number * 1 : number * 3;

                }
                int checknum = 10 - (num % 10);
                if (checknum == 10) {
                    checknum = 0;
                }
                bool = checknum == Integer.parseInt(realnum.substring(12));
            } else {
                bool = false;
            }
        }

        return bool;
    }

    public void checkMsg(JTextField isbnField, JTextField titleTextField, JTextField authorField,
            JTextField publisherField, JTextField yearField) {
        if (isbnField.getText().isEmpty() || isbnField.getText().trim().length() == 0) {
            isbnField.setText("N/A");

        }
        if (titleTextField.getText().isEmpty() || titleTextField.getText().trim().length() == 0) {
            titleTextField.setText("N/A");
        }
        if (authorField.getText().isEmpty() || authorField.getText().trim().length() == 0) {
            authorField.setText("N/A");
        }
        if (publisherField.getText().isEmpty() || publisherField.getText().trim().length() == 0) {
            publisherField.setText("N/A");
        }
        if (yearField.getText().isEmpty() || yearField.getText().trim().length() == 0) {
            yearField.setText("N/A");
        }

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

    static Boolean isConnected() {
        return !clientSocket.isClosed();
    }

    public static void main(String args[]) {
        try {
            new ClientGUI();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("GUI BUILT");
        while(!(ClientGUI.connected)) {
        }
        receive(ClientGUI.in);

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
    public String outputLine(String finalISBN, String title, String author, String publisher, String year) {
    	String line = "";
    	if((finalISBN!=null && !finalISBN.isEmpty())&& !(finalISBN.equals("N/A"))) {
    		line=line.concat("ISBN "+finalISBN+"\n");
    	}
    	if((title!=null && !title.isEmpty())&& !(title.equals("N/A"))) {
    		line=line.concat("TITLE "+title+"\n");
    	}
    	if((author!=null && !author.isEmpty())&& !(author.equals("N/A"))) {
    		line=line.concat("AUTHOR "+author+"\n");
    	}
    	if((publisher!=null && !publisher.isEmpty())&& !(publisher.equals("N/A"))){
    		line=line.concat("PUBLISHER "+publisher+"\n");
    	}
    	if((year!=null && !year.isEmpty())&& !(year.equals("N/A"))) {
    		line=line.concat("YEAR "+year+"\n");
    	}
    	return line;
    }
}
