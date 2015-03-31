/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab10;

import Lab7.Client7;
import Lab7.ClientHandler;
import Lab7.Server;
import Lab8.Client.ClientInterfaceController;
import Lab8.Client.ClientNIO;
import Lab8.Server.ServerNIO;
import Lab9.Client;
import Lab9.ClientInterface;
import Lab9.ClientReceiver;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author matthieu
 */
public class Lab10 extends Application {
    
    private static final String PROGNAME="Lab10.jar";
    private static InetAddress addressIP;
    private static int port;
    private static boolean multicast, nio, serv, debug;
    private static Thread t;
    
    private static Stage      primaryStage;
    private static AnchorPane rootLayout;
    
    private static ClientInterfaceController controller;
    private static Client client;
    
    private static ServerNIO servernio;
    private static ClientNIO clientnio;
    
    private static Server server;
    private static Client7 client7;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException, Exception {
        LongOpt[] longopts = new LongOpt[7];
        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        
        ArrayList<Thread> tList = new ArrayList<>();
        
        Socket clientSocket;
        ClientHandler ch;
        
        int i=0;
        
         multicast=false;
         nio=false;
         serv=false;
         debug = false;
        
        
        longopts[0]=new LongOpt("address", LongOpt.REQUIRED_ARGUMENT,sb,'a');
        longopts[1]=new LongOpt("debug", LongOpt.NO_ARGUMENT,null,'d');
        longopts[2]=new LongOpt("help", LongOpt.NO_ARGUMENT,null,'h');
        longopts[3]=new LongOpt("multicast", LongOpt.NO_ARGUMENT,null,'m');
        longopts[4]=new LongOpt("nio", LongOpt.NO_ARGUMENT,null,'n');
        longopts[5]=new LongOpt("port", LongOpt.REQUIRED_ARGUMENT,sb1,'p');
        longopts[6]=new LongOpt("server", LongOpt.NO_ARGUMENT,null,'s');
        
        System.setProperty("java.net.preferIPv4Stack","true");
        
        Getopt g = new Getopt(PROGNAME,args, "a:p:dhmns",longopts);
        
        int c;
        while((c = g.getopt())!= -1){
            switch(c){
                case 'a':
                    addressIP=InetAddress.getByName(g.getOptarg());
                    break;
                case 'd':
                    debug = true;
                    System.out.println("d");
                    break;
                case 'h':
                    System.out.println("-a , -- address = ADDR set the IP address\n" +
                                       "-d , -- debug display error messages\n" +
                                       "-h , -- help display this help and quit\n" +
                                       "-m , -- multicast start the client in multicast mode\n" +
                                       "-n , -- nio configure the server in NIO mode\n" +
                                       "-p , -- port = PORT set the port\n" +
                                       "-s , -- server start the server");
                    break;
                case 'm':
                    multicast = true;
                    
                    break;
                case 'n':
                    nio = true;
                    break;
                case 'p':
                    port=Integer.parseInt(g.getOptarg());
                    break;
                case 's':
                    serv = true;
                    break;
            }
        }
        
        if((multicast&&nio)||(multicast&&serv)) System.out.println("incompatibles arguments...");
        else {
             if(serv&&nio){
                    
                servernio = new ServerNIO(addressIP, port);
                servernio.start();              
                
            }else if(serv){
                server = new Server(InetAddress.getLocalHost(),1025);
                server.start();
                while(true){
                    clientSocket=server.accept();
                    
                    ch = new ClientHandler(clientSocket,server);
                    
                    
                    tList.add(new Thread (ch));
                    tList.get(i).start();
                    if(clientSocket.isConnected())server.broadcast("new client connected", clientSocket);
                    i++;
                }
            }else{
                launch();
            }
        }
        
    }

    
    public void initRootLayout() {
        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Lab10.class.getResource("/Lab8/Client/ClientInterface.fxml"));
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
    }
        @Override
    public void start(Stage primaryStage) throws IOException, Exception {
        
        
       // hostAddress = InetAddress.getLocalHost();
       // port = 1025;
       // channel = this.initiateConnection();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Client Chat");
        initRootLayout();
        
        
        if(multicast){
            
            client=new Client(addressIP,port); 
            Thread t = new Thread(new ClientReceiver(client,controller));
            t.start();
              
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    try {
                        client.quitGroup();

                    } catch (IOException ex) {
                        Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });              
                
        }else if(nio){
            
            clientnio = new ClientNIO(addressIP, port,controller);
             primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    try {
                        clientnio.getChannel().close();

                    } catch (IOException ex) {
                        Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            

        }else{
            
            client7 = new Client7(controller);
            client7.run();
            
        }
        
        
        
    }
    
    public static void send(String message) throws IOException{
        if(multicast){
          client.sendMessage(message);  
        }else if(nio){
            clientnio.send(message);
        } else {
            client7.send(message);
        }
        
    }

    
    
}
