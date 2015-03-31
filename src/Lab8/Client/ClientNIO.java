/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab8.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 *
 * @author user
 */
public class ClientNIO {
    
    private InetAddress hostAddress;
    private int port;
    private SocketChannel channel;
    private ClientInterfaceController controller;
    private RunClient client;

    public SocketChannel getChannel() {
        return channel;
    }

    
    
    
    public ClientNIO(InetAddress hostAddress, int port, ClientInterfaceController controller) throws UnknownHostException, Exception {
        //this.hostAddress = hostAddress;
        //this.port = port;
        this.controller = controller;
        this.hostAddress = InetAddress.getLocalHost();
        this.port = 1025;
        this.channel = this.initiateConnection();
        
        this.client = new RunClient(controller, channel);
        
        
        Thread t = new Thread(client);
        t.start();        
        
    }
    
    private SocketChannel initiateConnection() throws Exception {
        // Create a non-blocking socket channel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        // Kick off connection establishment
        
        System.out.println("\n\n\n\n Hostname :"+this.hostAddress+"\n Port : "+this.port+" \n\n\n\n");
        
        socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));

        socketChannel.finishConnect();
        
        return socketChannel;
  }
    
    public void send(String msg) throws IOException {
        ByteBuffer msgBuf=ByteBuffer.wrap(msg.getBytes());
        this.channel.write(msgBuf);
        msgBuf.rewind();
    }
    
    
    
    
    
}
