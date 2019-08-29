package Question4;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
  public static void main (String[] args) {
	  
	
	//Server.main(new String[] {25,67,'hi'});
	  
    String hostAddress;
    int tcpPort;
    int udpPort;
    String mode = "UDP"; //default comm mode is set to TCP(0)
    if (args.length != 3) {
      System.out.println("ERROR: Provide 3 arguments");
      System.out.println("\t(1) <hostAddress>: the address of the server");
      System.out.println("\t(2) <tcpPort>: the port number for TCP connection");
      System.out.println("\t(3) <udpPort>: the port number for UDP connection");
      System.exit(-1);
    }
    
    hostAddress = args[0];
    tcpPort = Integer.parseInt(args[1]);
    udpPort = Integer.parseInt(args[2]);
    
    //UDP buffer packet
    byte[] udprbuffer = new byte[50];
    
    Scanner sc = new Scanner(System.in);
    while(sc.hasNextLine()) {
      String cmd = sc.nextLine();
      String[] tokens = cmd.split(" ");

      if (tokens[0].equals("setmode")) {
        // TODO: set the mode of communication for sending commands to the server 
        // and display the name of the protocol that will be used in future
    	  mode = ((tokens[1].equals("T") == true) ? "TCP" : "UDP"); 
    	  System.out.println("Protocol set to: "+mode);
      }
      else if (tokens[0].equals("purchase")) {
        // TODO: send appropriate command to the server and display the
        // appropriate responses form the server
    	  //sample message purchase <user-name> <product-name> <quantity>
    	  String productName = tokens[1];
    	  String quantity = tokens[2];
    	 
    	  //UDP client send information
    	  //need to add random user generation
    	  if(mode=="UDP"){
    		  try {
    			InetAddress ia = InetAddress.getByName(hostAddress);
				DatagramSocket datasocket = new DatagramSocket();
				String message = "purchase user1 "+productName+" "+quantity;
				byte[] buffer = new byte[message.length()];
				buffer=message.getBytes();
				DatagramPacket sPacket, rPacket;
				sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
				System.out.println("got buffer");
				datasocket.send(sPacket);
				
				rPacket = new DatagramPacket(udprbuffer, udprbuffer.length);
				System.out.println("getting response");
				datasocket.receive(rPacket);
				System.out.println("got response");
				String recieveString = new String(rPacket.getData(), 0, rPacket.getLength());
				System.out.println("Returned value: "+recieveString);
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
    		  
    	  }
    	  else{
    		  
    	  }
      } else if (tokens[0].equals("cancel")) {
        // TODO: send appropriate command to the server and display the
        // appropriate responses form the server
    	  String cancelOrderNumber = tokens[1];
    	  if(mode=="UDP"){
    		  try {
    			InetAddress ia = InetAddress.getByName(hostAddress);
				DatagramSocket datasocket = new DatagramSocket();
				String message = "cancel "+cancelOrderNumber;
				byte[] buffer = new byte[message.length()];
				buffer=message.getBytes();
				DatagramPacket sPacket, rPacket;
				sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
				System.out.println("got buffer");
				datasocket.send(sPacket);
				
				rPacket = new DatagramPacket(udprbuffer, udprbuffer.length);
				System.out.println("getting response");
				datasocket.receive(rPacket);
				System.out.println("got response");
				String recieveString = new String(rPacket.getData(), 0, rPacket.getLength());
				System.out.println("Cancel status: "+recieveString);
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
    		  
    	  }
    	  else{
    		  
    	  }
    	  
      } else if (tokens[0].equals("search")) {
        // TODO: send appropriate command to the server and display the
        // appropriate responses form the server
    	  String username = tokens[1];
    	  if(mode=="UDP"){
    		  try {
    			InetAddress ia = InetAddress.getByName(hostAddress);
				DatagramSocket datasocket = new DatagramSocket();
				String message = "search "+username;
				byte[] buffer = new byte[message.length()];
				buffer=message.getBytes();
				DatagramPacket sPacket, rPacket;
				sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
				System.out.println("got buffer");
				datasocket.send(sPacket);
				
				rPacket = new DatagramPacket(udprbuffer, udprbuffer.length);
				System.out.println("getting response");
				datasocket.receive(rPacket);
				System.out.println("got response");
				String recieveString = new String(rPacket.getData(), 0, rPacket.getLength());
				System.out.println("User order history: "+recieveString);
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
    		  
    	  }
    	  else{
    		  
    	  }
      } else if (tokens[0].equals("list")) {
        // TODO: send appropriate command to the server and display the
        // appropriate responses form the server
    	  if(mode=="UDP"){
    		  try {
    			InetAddress ia = InetAddress.getByName(hostAddress);
				DatagramSocket datasocket = new DatagramSocket();
				String message = "list";
				byte[] buffer = new byte[message.length()];
				buffer=message.getBytes();
				DatagramPacket sPacket, rPacket;
				sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
				System.out.println("got buffer");
				datasocket.send(sPacket);
				
				rPacket = new DatagramPacket(udprbuffer, udprbuffer.length);
				System.out.println("getting response");
				datasocket.receive(rPacket);
				System.out.println("got response");
				String recieveString = new String(rPacket.getData(), 0, rPacket.getLength());
				System.out.println("Available products: \n"+recieveString);
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
    		  
    	  }
    	  else{
    		  
    	  }
      } else {
        System.out.println("ERROR: No such command");
      }
    }
  }
}