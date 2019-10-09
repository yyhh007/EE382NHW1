package Question3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;


public class Client {
	//from sample code, required params
	Scanner din;
	PrintStream pout;
	Socket server;
	int pid=1;
	LamportClock c;
	//open tcp socket
	public void getSocket(String hostAddress, int port, String [] serverAddressArray, int [] portlist) throws IOException{
		
		//server = new Socket(hostAddress, port);
		Instant first = Instant.now();
		Instant second = Instant.now();

		Duration duration =  Duration.between(first,second);
		server = new Socket();
		//if IO exception then loop for 
		long numMillis = 0;
		//loops on  each server in the list
		//if the server fails over 100ms
			//next server
		//else
			//break
		for(int i =0; i<serverAddressArray.length;i++) {
			
			numMillis = 0;
			first = Instant.now();		
			while(numMillis <101) {
				try {
				if(server.isConnected()) {
						break;
				}
				second = Instant.now();
				duration =  Duration.between(first,second);
				numMillis = duration.toMillis();
				//if the connection passed then break
				
				server.connect(new InetSocketAddress(serverAddressArray[i],portlist[i]), 100);
				
				}catch (SocketTimeoutException e) {
					
					e.printStackTrace();
					break;
				}  
				catch (IOException e) {
					e.printStackTrace();}
			}
			
			//if the server fails over 100ms
			if(numMillis<100) {
				break;
			} 
			
			if (i==serverAddressArray.length-1) {
				i = 0;
			}
				
				
		}

	
		din = new Scanner(server.getInputStream());
		pout = new PrintStream(server.getOutputStream());
		
	}
	
	//TCP send receive print
	//
	//add timeout here 100mill
	//retry on timeout here, loop through list of server ips
	public String TCPSendClientRequest(String hostAddress, int tcpPort, String [] serverAddressArray, int [] portlist, String outMessage) throws IOException {
		 getSocket(hostAddress, tcpPort,serverAddressArray,portlist);
		 pout.println(outMessage);
		 pout.flush();
		 String returnString = din.nextLine();
		 returnString = returnString.replace("\t", "\n");
		 server.close();
		 System.out.println(returnString);
		 return returnString;
	}
	
	//System.currentTimeMillis()
	public static void main (String[] args) {
		
		Client client = new Client();
		String hostAddress;
		int numberOfServer;
		int []tcpPorts=new int[args.length-1];
		int tcpPortIndex = 0;
		//run arguments
		//getting all the ports 
		
		numberOfServer = Integer.parseInt(args[0]);
		hostAddress = args[1].substring(0, args[1].indexOf(":"));
		String [] serverAddressArray =new String[numberOfServer];
		serverAddressArray[0] = "127.0.0.1";
		serverAddressArray[1] = "127.0.0.1";
		
		for (int i = 1; i<args.length; i++) 
			//System.out.println(args[i].substring(args[i].indexOf(":")+1));
			tcpPorts[i-1] = Integer.parseInt(args[i].substring(args[i].indexOf(":")+1));
		
		//System.out.println(tcpPorts);
	    Scanner sc = new Scanner(System.in);
	    /*int numServer = sc.nextInt();
		
	    for (int i = 0; i < numServer; i++) {
	      // TODO: parse inputs to get the ips and ports of servers
	    }*/
	    //client.TCPSendClientRequest(hostAddress, tcpPort, message);
	    while(sc.hasNextLine()) {
	    	String cmd = sc.nextLine();
	    	String[] tokens = cmd.split(" ");
	      	String message = null;
	      	if (tokens[0].equals("reserve")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	      		message = "reserve "+tokens[1];
	    	} else if (tokens[0].equals("bookSeat")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    		message = "bookseat "+ tokens[1]+ " "+tokens[2] ;
	    	} else if (tokens[0].equals("search")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    		message = "search "+tokens[1];
	    	} else if (tokens[0].equals("delete")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    		message = "delete " +tokens[1];
	    	} else {
	        System.out.println("ERROR: No such command");
	    	}
	      	message+=" 1";
	      	
	      	
	      	try {
	      		System.out.println("resquesting message: "+message+" at port "+hostAddress+Integer.toString(tcpPorts[tcpPortIndex]));
				String response = client.TCPSendClientRequest(hostAddress, tcpPorts[tcpPortIndex],serverAddressArray,tcpPorts , message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	}
}