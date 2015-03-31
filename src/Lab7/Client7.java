/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab7;

import Lab8.Client.ClientInterfaceController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author user
 */
public class Client7 {
    
    Socket requestSocket;
    BufferedWriter pw;
    BufferedReader in;
    String message;
    
    InetAddress address;
    int port;
    
    private ClientInterfaceController controller;
    
    public Client7(ClientInterfaceController controller) throws UnknownHostException{
        this.controller = controller;
        address = InetAddress.getLocalHost();
        port = 1025;
        
    }
    
    
    public void run()
    {
        try{
            
            //1. creating a socket to connect to the server
            requestSocket = new Socket(address, port);
            
            System.out.println("Connected :"+requestSocket.isConnected()+"\n closed : "+ requestSocket.isClosed());
            //2. get Input and Output streams
            
            
            
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            
            
            //3: Communicating with the server
            Receiver receiver = new Receiver(in);
            Thread t = new Thread(receiver);
            t.start();
            
            this.pw = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
            
        }
        catch(UnknownHostException unknownHost){
            System.out.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        
    }
    public void send(String msg) throws IOException
    {
        System.out.println("Send : "+msg);
        pw.write(msg);
        pw.flush();
    }
    
    
}
