/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab7;

import Lab8.Client.ClientInterfaceController;
import java.io.BufferedReader;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author user
 */
public class Receiver extends Task {
    BufferedReader in;
    String message;
    ClientInterfaceController controller;
    
    public Receiver(BufferedReader in){
        
        this.in=in;
    }
    
    @Override
    protected Object call() throws Exception {
        
        while(true){
            
            message = in.readLine();
            System.out.println("Receiver");
            //Platform.runLater(() ->controller.printMessage(message));
            controller.printMessage(message);
            //System.out.println(message);
            if(message.equals("/quit")) break;
        }
        return null;
    
    }
    
    
}
