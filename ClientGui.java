import java.io.*;
import java.net.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import Server.Request.Bibliography;


import jdk.internal.org.jline.utils.InputStreamReader;
import sun.awt.AWTAccessor.AccessibleContextAccessor;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class ClientGui {
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
            if (!fromServer.equals("close"))
                break;

            str = stdIn.readLine();
        if (str != null) {
            System.out.println("Client: " + str);
            out.println(str);


        }
        JFrame frame = new JFrame("SUBMIT");
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.ORANGE);
        frame.setBounds(100, 100, 450, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        Font lblFont = newFont("Serif", Font.BOLD, 20);
        JLabel[] uscore = new JLabel[15];

        frame.setSize(300,300);
         

        Msg msg = new Msg();
        titlewrd = new JLabel(" MESSAGE");
        titlewrd.setBounds (260,100,500,150);
        titlewrd.setFont(lblFont);

        add (titlewrd);
        //this.add(man);
        man.setBounds(100,100,400,400);

        JPanel panel = new JPanel();
        panel.setBounds(r);
        frame.getContentPane().add(panel);



        msglayout = new FlowLayout();
        container = getContentPane();
        setLayout(null);


        sButton = new JButton("SUBMIT");
        add(SButton);
        sButton.setBounds(30,520, 50, 29);
        sButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event){
                    System.out.println("....."); // book description
                }
            }
        );

        uButton  = new JButton("UPDATE");
        add(uButton);
        uButton.setBounds(30,520, 50, 29);
        uButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event){
                    System.out.println("....."); // update book description
                }
            }
        );

        gButton  = new JButton("GET");
        add(gButton);
        gButton.setBounds(30,520, 50, 29);
        gButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event){
                    System.out.println("....."); // get book description
                }
            }
        );

        rButton  = new JButton("REMOVE");
        add(rButton);
        rButton.setBounds(30,520, 50, 29);
        rButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event){
                    System.out.println("....."); // remove book description
                }
            }
        );





        //panel.add(button);
        //button.addActionListener(new Action());

      /*  static class Action implements ActionListener{
            public void actionPerformed (ActionEvent e) {
                JFrame frame2 = new JFrame("click");
                frame2.setVisible(true);
                frame2.setSize(200,200);
                JLabel label = new JLabel("   done  ");
                JPanel panel = new JPanel();
                frame2.add(panel);
                panel.add(label);
                

            }
        */


            
    

        }
        p.close();
        b.close();
        stdIn.close();
        s.close();



        }
        

        


    }
 


