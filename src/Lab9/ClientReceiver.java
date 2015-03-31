/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab9;

import Lab8.Client.ClientInterfaceController;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author matthieu
 */
public class ClientReceiver extends Task<Void> {

    private ClientInterfaceController CIC;
    private Client client;
    private boolean chatting;
    private String tmp;

    public String getTmp() {
        return tmp;
    }
    
    
    
    public ClientReceiver(Client client, ClientInterfaceController CIC){
        this.client=client;
        this.CIC=CIC;
    }
    @Override
    protected Void call() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        chatting=true;
        while(chatting){
            
            tmp= client.manageMsg();
            /*if(tmp.equals("/quit")) {
                client.quitGroup();
                stopChatting();
            }*/
            
            if(!tmp.equals(""))Platform.runLater(() ->CIC.printMessage(getTmp()));
        }
        return null;
    
    }
    
    public void stopChatting(){
       chatting=false; 
    }
}
