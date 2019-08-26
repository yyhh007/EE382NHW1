package Question4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
	  public static void main (String[] args) throws IOException {
	    int tcpPort;
	    int udpPort;
	    if (args.length != 3) {
	      System.out.println("ERROR: Provide 3 arguments");
	      System.out.println("\t(1) <tcpPort>: the port number for TCP connection");
	      System.out.println("\t(2) <udpPort>: the port number for UDP connection");
	      System.out.println("\t(3) <file>: the file of inventory");

	      System.exit(-1);
	    }
	    tcpPort = Integer.parseInt(args[0]);
	    udpPort = Integer.parseInt(args[1]);
	    String fileName = args[2];
	    //System.out.println(System.getProperty("user.dir"));
	    // parse the inventory file
	    File file = new File(System.getProperty("user.dir")+"\\src\\Question4\\input\\inventory.txt"); 
	    ArrayList<String> inventory = new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new FileReader(file)); 
	    
	    String st; 
	    while ((st = br.readLine()) != null) 
	    	inventory.add(st);
	   
	    // TODO: handle request from clients
	  	byte[] buf = new byte[1024];
	  	DatagramPacket datapacket, returnpacket;
	  	try{
		  	DatagramSocket datasocket= new DatagramSocket(udpPort);
		  	
		  	while(true){
		  		datapacket=new DatagramPacket(buf, buf.length);
		  		datasocket.receive(datapacket);
		  		returnpacket = new DatagramPacket(datapacket.getData(),
		  										datapacket.getLength(),
		  										datapacket.getAddress(),
		  										datapacket.getPort());
		  		datasocket.send(returnpacket);
		  	}
	  	}catch(SocketException e){
	  		
	  		
	  	}
	  	
	  	
	  	
	  
	  } 
}
	
