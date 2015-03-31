/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab9;

/**
 *
 * @author matthieu
 */
import Lab8.Client.ClientInterfaceController;
import Lab8.Client.RunClient;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author user
 */
public class ClientInterface extends Application {
    
    private Stage      primaryStage;
    private AnchorPane rootLayout;
    private InetAddress hostAddress;
    private int port;
    private ClientInterfaceController controller;
    private Client client;
    
    
    @Override
    public void start(Stage primaryStage) throws IOException, Exception {
        
        
        hostAddress = InetAddress.getLocalHost();
        port = 1025;
       // channel = this.initiateConnection();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Client Chat");
        //initRootLayout();
        
        
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              /*try {
                  
                  
              } catch (IOException ex) {
                  Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
              }*/
          }
      });
        
    }
    
    public void initialize(Client client){
        this.client=client;
    }
    /*public void initRootLayout() {
        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientInterface.class.getResource("/Lab8.Client/ClientInterface.fxml"));
            rootLayout = (AnchorPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);            
            // Give the controller access to the main app.
            controller = loader.getController();
            controller.setMain(this);                  
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    public void send(String message) throws IOException{
        client.sendMessage(message);
    }

    
   
    
    
}
