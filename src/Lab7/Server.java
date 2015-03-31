/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import Lab8.Server.*;

/**
 *
 * @author matthieu
 */
public class Server extends AbstractServer implements MultichatServer{
    
    private ServerSocket socket;
    private HashMap<Socket, PrintWriter> printW ;
    private HashMap<Socket, String> users;

    public Server(InetAddress address, int port) {
        super(address,port);
        
        printW = new HashMap<Socket, PrintWriter>( );
        users = new HashMap<Socket, String>();
    }
    
    public void addToPWMap(Socket clientSocket, PrintWriter pw){
        printW.put(clientSocket, pw );
        
    }
    
    public synchronized void addToNMap(Socket clientSocket, String name){
        users.put(clientSocket, name);
    }
    
    
    
    
    
    @Override
    public void start() {
        try{
        socket=new ServerSocket();
        //Socket clientSocket=new Socket();
        InetSocketAddress add=new InetSocketAddress(this.address,this.port);
        System.out.println(this.address);
        socket.bind(add);
        }catch(IOException e){
            e.printStackTrace();
        }
        //clientSocket=socket.accept();
        //return clientSocket;
        
        Thread t = new Thread();
        t.start();
        
    }
    
    public Socket accept() throws IOException{
        return socket.accept();
    }
    
    public void read(Socket clientSocket) throws IOException{
        
        
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
       // StringBuilder builder = new StringBuilder();
        
            String aux = "";
        while (!clientSocket.isClosed()){
            
            
                aux = in.readLine();
                
                //builder.append(aux);
                //String text = builder.toString();
                if(aux != null){
                    System.out.println(aux);
                    if (aux.equals("/quit")) clientSocket.close();
                    else if (aux.regionMatches(0,"/nickname ",0,10)) addToNMap(clientSocket, aux.substring(10,aux.length()));
                    else broadcast(aux,clientSocket);
                }
            }
 
    }
    
    public synchronized void broadcast(String aux, Socket clientSocket){
        Socket tmpSock;
        Set keys = printW.keySet();
        Iterator it = keys.iterator();
        System.out.println(aux);
        while (it.hasNext()){
            tmpSock=(Socket)it.next();
            
                if(users.containsKey(clientSocket)) aux = users.get(clientSocket)+": "+aux;
                printW.get(tmpSock).println(aux);
            
        }
        
        
    }
}
