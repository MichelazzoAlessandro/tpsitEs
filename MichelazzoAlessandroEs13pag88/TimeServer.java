
import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class TimeServer implements Runnable {

private DatagramSocket socket;
        
 public TimeServer(int port) throws SocketException {
  socket = new DatagramSocket(port);
  socket.setSoTimeout(1000); 
 }
 
 public void run() {
  DatagramPacket answer;
  byte[] buffer = new byte[1024]; 
  ByteBuffer data;
  DatagramPacket request = new DatagramPacket(buffer, buffer.length); 
 
  while (!Thread.interrupted()) {
   try {
    socket.receive(request); 
    
    Date now = new Date();
    long timestamp = now.getTime()/1000;
    
    data = ByteBuffer.wrap(buffer, 0, 8);
    data.putLong(timestamp);
    answer = new DatagramPacket(data.array(), 8, request.getAddress(), request.getPort());
    socket.send(answer); 
   }
   catch (IOException exception) {}
  }
 }

 public static void main(String[] args) throws IOException {
  int c;
  Thread thread;
        
  try {
   TimeServer udp_echo = new TimeServer(12345);
   thread = new Thread(udp_echo);
   thread.start();
   c = System.in.read();
   thread.interrupt();
   thread.join();
  }
  catch (SocketException exception) {
   System.err.println("Errore!");
  }
  catch (InterruptedException exception) {
   System.err.println("Fine.");
  }
 }    
}
