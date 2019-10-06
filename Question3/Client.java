package Question3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
	//from sample code, required params
	Scanner din;
	PrintStream pout;
	Socket server;
	int pid=1;
	LamportClock c;
	//open tcp socket
	public void getSocket(String hostAddress, int port) throws IOException{
		
		server = new Socket(hostAddress, port);
		din = new Scanner(server.getInputStream());
		pout = new PrintStream(server.getOutputStream());
	}
	
	//TCP send receive print
	
	//add timeout here 100mill
	//retry on timeout here, loop through list of server ips
	public String TCPSendClientRequest(String hostAddress, int tcpPort, String outMessage) throws IOException {
		 getSocket(hostAddress, tcpPort);
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
				String response = client.TCPSendClientRequest(hostAddress, tcpPorts[tcpPortIndex], message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	}
}