/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab8.Server;

import java.net.InetAddress;

/**
 *
 * @author user
 */
public class AbstractServer {
    protected InetAddress address;
    protected int port;

    public AbstractServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
    
    
    
}
