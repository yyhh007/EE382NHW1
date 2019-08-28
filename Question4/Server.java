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
		    int orderID=1;
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
		    File file = new File(System.getProperty("user.dir")+"\\src\\input\\inventory.txt"); 
		    ArrayList<String> inventory = new ArrayList<String>();
		    
		    BufferedReader br = new BufferedReader(new FileReader(file)); 
		    
		    String st; 
		    while ((st = br.readLine()) != null) 
		    	inventory.add(st);
		   
		    // TODO: handle request from clients
		  	byte[] buf = new byte[65535];
		  	DatagramPacket datapacket, returnpacket;
		  	try{
			  	DatagramSocket datasocket= new DatagramSocket(udpPort);
			  	
			  	while(true){
			  		datapacket=new DatagramPacket(buf, buf.length);
			  		datasocket.receive(datapacket);
			  		String data = new String(datapacket.getData());
			  		
			  		
			  		String command = data.substring(0, data.indexOf(" "));
			  		System.out.println(command);
			  		byte [] returnByte=null;
			  		switch(command) {
			  		case "purchase":
			  			returnByte = UDPPurchase(orderID, data, inventory);
			  			System.out.println(new String(returnByte));
			  			break;
			  		case "cancel":
			  			returnByte = UDPCancel(orderID);
			  			break;
			  		case "search":
			  			returnByte = UDPSearch();
			  			break;
			  		case "list":
			  			returnByte = UDPList();
			  			break;
			  		}
			  		
			  		returnpacket = new DatagramPacket(returnByte,
			  										datapacket.getLength()+100,
			  										datapacket.getAddress(),
			  										datapacket.getPort());
			  		datasocket.send(returnpacket);
			  	}
		  	}catch(SocketException e){
		  		
		  		
		  	}
	  	
	  
	  }

	private static byte [] UDPList() {
		// TODO Auto-generated method stub
		return null;
	}

	private static byte [] UDPSearch() {
		// TODO Auto-generated method stub
		return null;
	}

	private static byte [] UDPCancel(int orderID) {
		// TODO Auto-generated method stub
		String id = String.valueOf(orderID);
		for ()
		
		return null;
	}

	private static byte [] UDPPurchase(int orderID, String data, ArrayList<String> inventory) {
		// TODO Auto-generated method stub
		
		//return responses
		String returnedStringPurchaseComplete = "Your order has been placed, OrderID"+String.valueOf(orderID)+" "+data.substring(data.indexOf(" ")+1);
		String [] returnResponses = {"Not Available - We do not sell this product","Not Available - Not enough items",returnedStringPurchaseComplete};
		
		
		String [] commandList = data.split("\\s+");
		
		//check if item is in inventory, not enough item, or enough item
		for (int x=0; x<inventory.size() ;x++) {
			if (commandList[2].contentEquals(inventory.get(x).substring(0, inventory.get(x).indexOf(" ")))) {
				//
				if(Integer.valueOf(commandList[3].replaceAll("\\D+", ""))>Integer.valueOf(inventory.get(x).replaceAll("\\D+", ""))) {
					return returnResponses[1].getBytes();
				}
				else {
					purchaseOrder.add(String.valueOf(orderID)+" "+data.substring(data.indexOf(" ")+1));
					return returnResponses[2].getBytes();
				}
			}
		}
		
		return returnResponses[0].getBytes();
	} 
}