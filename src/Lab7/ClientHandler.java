/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab7;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matthieu
 */
public class ClientHandler implements Runnable {
    Socket clientSocket;
    Server server;
    PrintWriter pw;
    
    public ClientHandler(Socket clientSocket,Server server) throws IOException{
        this.clientSocket=clientSocket;
        this.server=server;
        this.pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        
        
    }
    
    @Override
    public void run(){
        
        try {
            
                server.addToPWMap(clientSocket,pw);
                server.read(clientSocket);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
