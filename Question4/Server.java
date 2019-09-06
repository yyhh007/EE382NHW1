package Question4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
	static ArrayList<String> purchaseOrder = new ArrayList<String>();
	 
	  public static void main (String[] args) throws IOException {
		    int tcpPort;
		    int udpPort;
		    
		    //server orderID for all customers
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
		 
		    //load initial inventory file items
		    Inventory inven = new Inventory();
		    inven.loadInventory();
		    ArrayList<String> inventory = inven.getInventory();
		    
		    // TODO: handle request from clients
		    //TCP server
		    new Thread(new Runnable() {
		    	public void run() {
		    		System.out.println("tcp server started:");	
		    		try {
		    			ServerSocket listener  = new ServerSocket(tcpPort);
		    			Socket s;
		    			
		    			while ((s = listener.accept())!= null) {
		    				Thread t = new TCPServerThread(inven, s);
		    				t.start();
		    			}
		    		}catch(IOException e) {
		    			System.out.println(e);
		    		}
		    	}
		    }).start();
		    
		  	//UDP server handle, move to runnable
		    new Thread(new Runnable() {
		    	public void run() {
		    		DatagramPacket datapacket, returnpacket;
				  	try{
				  		
					  	DatagramSocket datasocket= new DatagramSocket(udpPort);
					  	while(true){
					  		byte[] buf = new byte[4048];
					  		datapacket=new DatagramPacket(buf, buf.length);
					  		datasocket.receive(datapacket);
					  		String data = new String(datapacket.getData());
					  		String [] commandList = data.split(" ");
					  		
					  		//String command = "";
					  		//command.getClass().getName();
					  		String command = commandList[0];
					  		//System.out.println(command);
					  		byte [] returnByte=null;
					  		
					  		if (command.contains("list")) {
					  			returnByte = inven.List();
					  		} else if (command.equals("purchase")){
					  			commandList[3]=commandList[3].replaceAll("[^\\d.]", "");
					  			returnByte=inven.Purchase(commandList);		  		
					  			
					  		} else if (command.contains("cancel")){
					  			returnByte = inven.Cancel(Integer.valueOf(commandList[1].replaceAll("[^\\d.]", "")));		  		
					  			
					  		} else if (command.contains("search")){
					  			returnByte = inven.Search(commandList);		  							  			
					  		} else {
					  			System.out.println("Command not found"); 
					  		}
					  							  	
					  		datapacket.setData(returnByte);
				  			datapacket.setLength(returnByte.length);
				  			
					  		returnpacket = new DatagramPacket(datapacket.getData(),
					  										datapacket.getLength(),
					  										datapacket.getAddress(),
					  										datapacket.getPort());
					  		datasocket.send(returnpacket);
					  	}
				  	}catch(IOException e){
				  						  	
				  	}
		    	}
		    }).start(); 
	  }
}