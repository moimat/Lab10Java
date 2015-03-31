/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab8.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ServerNIO extends AbstractServer implements MultichatServer {

    private Selector selector;
    
    public ServerNIO(InetAddress address, int port) throws UnknownHostException {
        super(InetAddress.getLocalHost(), 1025);
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }    
    
    @Override
    public void start() {
        System.out.println("Connection");
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind( new InetSocketAddress(getAddress(), getPort()));
            //SocketChannel client = server.accept();
            this.selector = Selector.open();
            
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                selector.select();
                Set<SelectionKey>keys = selector.selectedKeys();
                Iterator<SelectionKey>keyIterator = keys.iterator();
               
                while(keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

                        // Accept the connection and make it non-blocking
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        Socket socket = socketChannel.socket();
                        socketChannel.configureBlocking(false);

                        // Register the new SocketChannel with our Selector, indicating
                        // we'd like to be notified when there's data waiting to be read
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("Connect");
                    }
                    ByteBuffer bbuf = ByteBuffer.allocate(8192);
                    if(key.isReadable()){
                        System.out.println("try to read");
                        ((SocketChannel)key.channel()).read(bbuf);
                        Charset charset = Charset.defaultCharset();
                        bbuf.flip();
                        CharBuffer cbuf = charset.decode(bbuf);
                        System.out.println(cbuf);
                        key.attach(bbuf);
                        bbuf.compact();
                        broadcast(cbuf.toString());
                        if(cbuf.toString().equals("/quit"))
                        {
                            key.channel().close();
                            key.cancel();
                        }

                    }
                    
                    keyIterator.remove();
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ServerNIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    private void broadcast(String msg) throws IOException {
        ByteBuffer msgBuf=ByteBuffer.wrap(msg.getBytes());
        for(SelectionKey key : selector.keys()) {
                if(key.isValid() && key.channel() instanceof SocketChannel) {
                        SocketChannel sch=(SocketChannel) key.channel();
                        sch.write(msgBuf);
                        msgBuf.rewind();
                }
        }
    }
    
}
