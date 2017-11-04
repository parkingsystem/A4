package Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ClienNodeWorker implements Runnable {

   private ServerSocket serverSocket;
   private Node node;
   public boolean continueFlag = true;
   public TCPSender tcpSender = new TCPSender();
   public ClienNodeWorker(ServerSocket sc,Node node) {
      this.serverSocket = sc;
      this.node = node;
   }

   @Override
   public void run() {
	     System.out.println("Started client receiver thread;");

	      while (continueFlag) {
	         Socket socket = null;
	         Command request = null;
	         try {
	            socket = serverSocket.accept();
	            
	            //Get the message
	            request = CommandFactory.process(socket);
	            
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
	         
	         //Notify the Node with received command
//	         try {
//	            node.notify(message);
//	         } catch (Exception e) {
//	            e.printStackTrace();
//	         }
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

   public void setNode(Node node) {
      this.node = node;
   }

}