/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab8.Client;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author user
 */
public class RunClient extends Task <Void>{
    private ClientInterfaceController controller;
    private SocketChannel channel;
    private Selector selector;

    public RunClient(ClientInterfaceController controller, SocketChannel channel) {

        this.controller = controller;
        this.channel = channel;
        
        
    }

    
    private void processSelectedKeys(Set keys) throws Exception {
    Iterator itr = keys.iterator();
    while (itr.hasNext()) {
      SelectionKey key = (SelectionKey) itr.next();
      if (key.isReadable()){
          processRead(key);
      }
      itr.remove();
    }
  }  
    
    private void processRead(SelectionKey key) throws Exception {
        ByteBuffer bbuf = ByteBuffer.allocate(8192);
        ((SocketChannel)key.channel()).read(bbuf);
        Charset charset = Charset.defaultCharset();
        bbuf.flip();
        CharBuffer cbuf = charset.decode(bbuf);
        System.out.println(cbuf);
        Platform.runLater(() ->controller.printMessage(cbuf.toString()));
        key.attach(bbuf);
        bbuf.compact();
  }

    @Override
    protected Void call() throws Exception {
        try {
            selector = Selector.open();
   
            channel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);


            while(channel.isOpen()) {
                // events multiplexing loop
                if (selector.select() > 0) {
                    //System.out.println("Process");
                    processSelectedKeys(selector.selectedKeys());
                }
            }
        } catch (Exception e) {

        } 
        return null;
    }

    

    
    
}
