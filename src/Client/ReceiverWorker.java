package Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ReceiverWorker implements Runnable {

   private ServerSocket serverSocket;
   private ClientNode node;
   public boolean continueFlag = true;
   private TCPSender tcpSender = new TCPSender();

   public ReceiverWorker(ServerSocket sc, ClientNode node) {
      this.serverSocket = sc;
      this.node = node;
   }

   @Override
   public void run() {
      System.out.println("Started Client receiver thread;");

      while (continueFlag) {
         Socket socket = null;
         Command request = null;
         try {
            socket = serverSocket.accept();
            System.out.println("client socket excepted");
            //Get the message
            request = CommandFactory.process(socket, node.ServerToClientPATH);
            
            try {
               Command response = node.notify(request);
               tcpSender.sendData(socket, response.unpack());
            } catch (Exception e) {
               e.printStackTrace();
            }
            
            
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            try {
               if (socket != null) {
                  socket.close();
                  socket = null;
               }
            } catch (IOException e) {
               System.out.println("Error while closing resources: " + e.getMessage());
            }
         }

      }
   }

   public boolean isContinueFlag() {
      return continueFlag;
   }

   public void setContinueFlag(boolean continueFlag) {
      this.continueFlag = continueFlag;
   }

   public Node getNode() {
      return node;
   }

   public void setNode(ClientNode node) {
      this.node = node;
   }

}