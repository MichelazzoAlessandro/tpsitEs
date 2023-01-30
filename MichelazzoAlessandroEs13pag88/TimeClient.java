
import java.io.*;
import java.net.*;
import java.nio.*;

public class TimeClient {
 private DatagramSocket socket;

 public TimeClient() throws SocketException {
  socket = new DatagramSocket();
  socket.setSoTimeout(1000); 
 }

 public void closeSocket() { socket.close(); }
 
 public long getTime(String host, int port) throws UnknownHostException, IOException, SocketTimeoutException {
  ByteBuffer input, output;
  byte[] buffer = new byte[1024];
  DatagramPacket datagram;
  InetAddress address = InetAddress.getByName(host); 
  
  if (socket.isClosed()) { throw new IOException(); } 
  output = ByteBuffer.allocate(2);
  output.putChar('?');
  datagram = new DatagramPacket(output.array(), 2, address, port); 
  socket.send(datagram); 
  datagram = new DatagramPacket(buffer, buffer.length); 
  socket.receive(datagram); 
  
  if (datagram.getAddress().equals(address) && datagram.getPort() == port) {
      
      input = ByteBuffer.wrap(datagram.getData());
      return input.getLong();
  }
  else { throw new SocketTimeoutException(); }
 }

 public static void main(String args[]) {
  String IP_address;
  int UDP_port = 12345;
  long timestamp;
  TimeClient udp_request;
    
  if (args.length != 1) {
   System.err.println("Errore parametri forniti!");
   return;
  }

  IP_address = args[0];
    
  try {
   udp_request = new TimeClient();
   timestamp = udp_request.getTime(IP_address, UDP_port);
   System.out.println("Risposta: " + timestamp);
   udp_request.closeSocket();
  }
  catch(SocketException exception) { System.err.println("Errore creazione socket!"); }
  catch (UnknownHostException exception) { System.err.println("Indirizzo IP errato!"); }
  catch (SocketTimeoutException exception) { System.err.println("Nessuna risposta dal server!"); }
  catch (IOException exception) { System.err.println("Errore generico di comunicazione!"); }
 }
}
