import java.io.*;
import java.net.*;
import java.net.Socket;
import java.io.;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jdk.internal.org.jline.utils.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JButton;

public class ClientGui {
    public static void main(String[] args) throws IOException{
        Socket s = null;
        PrintWriter p = null;
        BufferedReader b = null;

        try {
            s = new Socket();
            System.out.println("Connected");

            p =  new PrintWriter(s.getOutputStream(), true);
            b = new BufferedReader(new InputStreamReader(s.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("x     ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("frrr");
            System.exit(1);
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System fromServer;
        String str;

        while ((fromServer = in.readline()) != null) {
            System.out.println("Server: " + fromServer );
            if (fromServer.equals("close"))
                break;

            str = stdIn.readLine();
        if (str != null) {
            System.out.println("Client: " + str);
            out.println(str);


        }


        }
        p.close();
        b.close();
        stdIn.close();
        s.close();



        }
        

        


    }
 


