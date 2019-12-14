/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserverprogram;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import static udpserverprogram.Server.count;
 
public class Client {
  
    public final static int PORT = 2222;
    static Map<Integer, String> datapackets = new TreeMap<>();
    int b;
    static int count;
    
    public static void main(String[] args) {
        
        String hostname = "localhost";

        try (DatagramSocket socket = new DatagramSocket()) {
          InetAddress ia = InetAddress.getByName(hostname);
          BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
          while (true) {
            byte[] data = new byte[1024];
            String theLine = userInput.readLine();
            if (theLine.equals("quit")) {
                data = theLine.getBytes();
                DatagramPacket outgoing  = new DatagramPacket(data, data.length, ia, PORT);
                socket.send(outgoing);
                break;
            }
            data = theLine.getBytes();
            DatagramPacket outgoing  = new DatagramPacket(data, data.length, ia, PORT);
            socket.send(outgoing);
            
            
            while(true) {
                byte[] receive = new byte[1024];
                DatagramPacket incoming = new DatagramPacket(receive, receive.length, ia, PORT);
                socket.receive(incoming);
                byte[] tempArray = incoming.getData();
                ByteArrayInputStream input = new ByteArrayInputStream(tempArray);
                int b = input.read();
                String s = new String(incoming.getData(), 0, incoming.getLength());
                HashMap<Integer, String> datapackets = new HashMap<>();
                datapackets.put(b,s);

             
//                 for (Integer keys : datapackets.keySet())   {
//                     if(keys <= keys++) {
//                         System.out.print("order: " + keys + " ");
//                         System.out.println("values " datapackets.getValue());
//                     }
//                     else {
//                         System.out.println("Missing packet.");
//                         incoming = new DatagramPacket(receive, receive.length, ia, PORT);
//                         socket.receive(incoming);
//                         tempArray = incoming.getData();
//                         input = new ByteArrayInputStream(tempArray);
//                         b = input.read();
//                         keys = b;
//                     }
//                 }
//                 //s = new String(incoming.getData(), 0, incoming.getLength());
//                 System.out.println("Server: " + s.substring(1));
//                        if(s.substring(1).equals("/e"))
//                         break;
////                
//                if(b <= b++) {
//                    System.out.print("b: "+ b + " ");    
//                    String s = new String(incoming.getData(), 0, incoming.getLength());
//                    System.out.println("Server: " + s.substring(1));
//                    if(s.substring(1).equals("/e"))
//                        break;
//                }
//                
//                if(!(b <= b++)) {
//                    System.out.println("Packet not received. Waiting.");
//                    socket.receive(incoming);
//                    tempArray = incoming.getData();
//                    input = new ByteArrayInputStream(tempArray);
//                    b = input.read();
//                    if(b <= b++) {
//                        System.out.println("b: "+ b);    
//                        String s = new String(incoming.getData(), 0, incoming.getLength());
//                        System.out.println("Server: " + s.substring(1));
                        if(s.substring(1).equals("/e")) {
                            System.out.println("Server: " + s.substring(1));
                            break;
                        }
//                    }
//                }

          //}
          
            for (Integer keys : datapackets.keySet())   {
                Integer value = keys;
                Integer nextNum = value +1;
                if(!(value++ == nextNum)) {
                    String dataset = datapackets.get(keys).substring(1); 
                    System.out.print("order: " + keys + " " + "Server:  ");
                    System.out.println(dataset);
                }
                else {
                    System.out.println("Missing packet please try again");
                }
            }
            }
            
              
          } // end while
        } catch (IOException ex) {
          System.err.println(ex);
        }
  }
}
