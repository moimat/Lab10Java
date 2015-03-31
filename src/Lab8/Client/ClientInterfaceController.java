/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab8.Client;

import Lab9.ClientInterface;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lab10.Lab10;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ClientInterfaceController implements Initializable {

    
    @FXML
    TextArea chat;
    
    @FXML
    TextField msg;
    
    private Lab10 main;
    
    private String username;

    public void setMain(Lab10 main) {
        this.main = main;
    }
    
    
    
    
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        chat.setText("MY CHAT");
        username = "anonynmous";
        
    }
    
    
    
    public void printMessage (String msg){
        String txt = chat.getText();
        
        
        chat.setText(""+txt+"\n"+msg+"");
    }
    
    public void handleSend () throws IOException{
        // Do send function
        
        if(this.msg.getText().regionMatches(0, "/nickname", 0, 9)){
            String old = username;
            username=this.msg.getText().substring(10);
            this.main.send(old+" : Changed his nickname for : "+username);
        }else{
           this.main.send(username+" : "+this.msg.getText()); 
        }
        
        
        this.msg.clear();
        
    }
    
}
