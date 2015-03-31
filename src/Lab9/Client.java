/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab9;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author matthieu
 */
public class Client {
    InetAddress group;
    MulticastSocket socket;
    InetSocketAddress mcastAddress;
    int port;
    String username;
    HashMap <String,String> connected;
    
    
    private static class UTF8Control extends ResourceBundle . Control {
        // TODO : override newBundle method
       }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Client(InetAddress address, int port) throws UnknownHostException, IOException{
        this.port=port;
        this.group=address;
        this.mcastAddress = new InetSocketAddress(group, this.port);
        System.out.println(group);
        this.socket= new MulticastSocket(this.port);
        this.socket.joinGroup(group);
        this.username="default";
        connected = new HashMap <> ();
    }
    
    public void sendMessage(String message) throws IOException{
       
        DatagramPacket msg = new DatagramPacket(message.getBytes(),message.length(),group,port);
        
        socket.send(msg);
        
        
    }
    
    public String receiveMessage() throws IOException{
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        socket.receive(recv);
        return new String(recv.getData(), 0, recv.getLength());
    }
    
    public void quitGroup() throws IOException{
        socket.leaveGroup(group);
    }
    
    public void sendConnectedMsg() throws UnknownHostException, IOException {
        String [] tmp = InetAddress.getLocalHost().toString().split("/");
        
        String message = "%%%connected??"+tmp[1]+"??"+username;
        System.out.println(message);
        DatagramPacket msg = new DatagramPacket(message.getBytes(),message.length(),group,port);
        socket.send(msg);
    }
    
    public void sendDiscMsg() throws UnknownHostException, IOException{
        String [] tmp = InetAddress.getLocalHost().toString().split("/");
        
        String message = "%%%disconnected??"+tmp[1]+"??"+username;
        System.out.println(message);
        DatagramPacket msg = new DatagramPacket(message.getBytes(),message.length(),group,port);
        socket.send(msg);
    }
    
    public boolean addConnectedPeople(String connectedMessage){
        String[] tmp = connectedMessage.split("??");
        if(!connected.containsKey(tmp[1])){
                connected.put(tmp[1], tmp[2]);
                return true;
        }
        else return false;
        
    }
    
    public boolean removeConnectedPeople(String disconnectMessage){
        String[] tmp = disconnectMessage.split("??");
        if(!connected.containsKey(tmp[1])){
                connected.remove(tmp[1]);
                return true;
        }
        else return false;
    }
    
    public String manageMsg() throws IOException{
        String tmp = receiveMessage();
        if (tmp.contains("%%%connected??")){
            addConnectedPeople(tmp);
            return "";
        }
        else if(tmp.contains("%%%disconnected??")){
            removeConnectedPeople(tmp);
            return "";
        }else if(tmp.regionMatches(0, "/quit", 0, 5)) {
            this.quitGroup();
            return username+" left the chat";
        }
        else return tmp;
        
            
        }
    
}
