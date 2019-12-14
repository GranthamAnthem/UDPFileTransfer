/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserverprogram;

import java.io.*;
import java.net.*;
import java.util.Arrays;
 
public class Server {
    
  public final static int PORT = 2222;
  public final static int MAX_PACKET_SIZE = 1024;
  public static int count = 0;
 
  
    public static void main(String[] args) {
    byte[] buffer = new byte[MAX_PACKET_SIZE];

    try (DatagramSocket server = new DatagramSocket(PORT)) {
      DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
      while (true) {
        try {
          server.receive(incoming);
          String s = new String(incoming.getData(), 0, incoming.getLength());
          
          if(s.equals("index")) {
               String name = "";
               File dir = new File(".");
               File[] filesList = dir.listFiles();
               for (File file : filesList) {
                   if (file.isFile() && file.getName().endsWith(".txt") ) {
                       name += file.getName() + " ";
                   }
               }
               
               ByteArrayOutputStream output = new ByteArrayOutputStream();
               byte[] newBuffer = new byte[MAX_PACKET_SIZE];
               newBuffer = name.getBytes();
               count++;
               output.write(count);
               output.write(newBuffer);
               byte[] out = output.toByteArray();
              
               DatagramPacket outgoing = new DatagramPacket(out, out.length, 
                       incoming.getAddress(), incoming.getPort());
               server.send(outgoing);    
               
               
               String endFlag = "/e";
               byte[] endBuffer = endFlag.getBytes();
               ByteArrayOutputStream endOutput = new ByteArrayOutputStream();
               count++;
               endOutput.write(count);
               endOutput.write(endBuffer);
                byte[] endOut = endOutput.toByteArray();
               
               DatagramPacket endPack = new DatagramPacket(endOut, endOut.length, 
                       incoming.getAddress(), incoming.getPort());
               server.send(endPack);
          }
          
          if(s.equals("")) {
              String error = ("No input entered. Try another request");
              byte[] newBuffer = new byte[MAX_PACKET_SIZE];
              newBuffer = error.getBytes();
              DatagramPacket outgoing = new DatagramPacket(newBuffer, newBuffer.length, 
                       incoming.getAddress(), incoming.getPort());
              server.send(outgoing);
               
              String endFlag = "/e";
              byte[] endBuffer = endFlag.getBytes();
              DatagramPacket endPack = new DatagramPacket(endBuffer, endBuffer.length, 
                       incoming.getAddress(), incoming.getPort());
              server.send(endPack);
                }
          
          else if(s.substring(0,3).equals("get")) {
                    String filename = s.substring(4);
                    File file = new File(filename);
                    if(file.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String ok = "ok";
                            byte[] okBuffer = ok.getBytes();
                            
                            count++;
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            output.write(count);
                            output.write(okBuffer);
                            byte[] out = output.toByteArray();
                            
                            DatagramPacket okMessage = new DatagramPacket(out, out.length, 
                                    incoming.getAddress(), incoming.getPort());
                            server.send(okMessage);
                            
                            String currentLine;
                            while ((currentLine = reader.readLine()) != null)  {
                                count++;
                                byte[] newBuffer = currentLine.getBytes();
                                ByteArrayOutputStream outputPacket = new ByteArrayOutputStream();
                                outputPacket.write(count);
                                outputPacket.write(newBuffer);
                                byte[] outPacket = outputPacket.toByteArray();
                                DatagramPacket outgoing = new DatagramPacket(outPacket, outPacket.length, 
                                        incoming.getAddress(), incoming.getPort());
                                server.send(outgoing);
                            }
                            String endFlag = "/e";
                            byte[] endBuffer = endFlag.getBytes();
                            ByteArrayOutputStream endOutput = new ByteArrayOutputStream();
                            count++;
                            endOutput.write(count);
                            endOutput.write(endBuffer);
                            byte[] endOut = endOutput.toByteArray();
               
                            DatagramPacket endPack = new DatagramPacket(endOut, endOut.length, 
                                    incoming.getAddress(), incoming.getPort());
                            server.send(endPack);
                        } 
                    }
                    else {
                        String errorMessage = ("Error file does not exist. Try another request");
                        byte[] newBuffer = new byte[MAX_PACKET_SIZE];
                        newBuffer = errorMessage.getBytes();
                        count++;
                        ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
                        errorOutput.write(count);
                        errorOutput.write(newBuffer);
                        byte[] outError = errorOutput.toByteArray();
                        
                        DatagramPacket outgoing = new DatagramPacket(outError, outError.length, 
                                incoming.getAddress(), incoming.getPort());
                        server.send(outgoing);

                        String endFlag = "/e";
                        byte[] endBuffer = endFlag.getBytes();
                        ByteArrayOutputStream endOutput = new ByteArrayOutputStream();
                        count++;
                        endOutput.write(count);
                        endOutput.write(endBuffer);
                        byte[] endOut = endOutput.toByteArray();

                        DatagramPacket endPack = new DatagramPacket(endOut, endOut.length, 
                                incoming.getAddress(), incoming.getPort());
                        server.send(endPack);
                    }
          }
          
          else if(s.equals("quit")) {
              String quit = ("Disconnecting Server");
                byte[] newBuffer = new byte[MAX_PACKET_SIZE];
                newBuffer = quit.getBytes();
                DatagramPacket outgoing = new DatagramPacket(newBuffer, newBuffer.length, 
                        incoming.getAddress(), incoming.getPort());
                server.send(outgoing);

                server.close();
                break;
          }
          
          incoming.setLength(buffer.length);
        } catch (IOException ex) {
          System.err.println(ex);
        }
        count = 0;
       } // end while
    } catch (SocketException  ex) {
      System.err.println(ex);
    } 
  }
}