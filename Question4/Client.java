package Question4;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	
	Scanner din;
	PrintStream pout;
	Socket server;
	
	public void getSocket(String hostAddress, int port) throws IOException{
		
		server = new Socket(hostAddress, port);
		din = new Scanner(server.getInputStream());
		pout = new PrintStream(server.getOutputStream());
	}
	
	
	public void TCPSendClientRequest(String hostAddress, int tcpPort, String outMessage) throws IOException {
		 getSocket(hostAddress, tcpPort);
		 pout.println(outMessage);
		 pout.flush();
		 String returnString = din.nextLine();
		 server.close();
		 System.out.println(returnString);
	}
	
	
	public static void main (String[] args) throws IOException {
		  
		
		//Server.main(new String[] {18udp,21tcp,'hi'});
		Client client = new Client();
		String hostAddress;
		int tcpPort;
		int udpPort;
		String mode = "TCP"; //default comm mode is set to TCP(0)
		if (args.length != 3) {
			System.out.println("ERROR: Provide 3 arguments");
			System.out.println("\t(1) <hostAddress>: the address of the server");
			System.out.println("\t(2) <tcpPort>: the port number for TCP connection");
			System.out.println("\t(3) <udpPort>: the port number for UDP connection");
			System.exit(-1);
		}
		
		//run arguments
		hostAddress = args[0];
		tcpPort = Integer.parseInt(args[1]);
		udpPort = Integer.parseInt(args[2]);
		
		//UDP buffer packet
		byte[] udprbuffer = new byte[1024];
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String cmd = sc.nextLine();
			String[] tokens = cmd.split(" ");
		
		
			String message = "";
			switch (tokens[0]) {
			case "purchase":
				message = "purchase user1 "+tokens[1]+" "+tokens[2];
				break;
			case "search":
				message = "search "+tokens[1];
				break;
			case "cancel":
				message = "cancel "+tokens[1];
				break;
			case "list":
				message = "list";
				break;
			case "setmode":
				mode = ((tokens[1].equals("T") == true) ? "TCP" : "UDP"); 
				System.out.println("Protocol set to: "+mode);
				break;
			}
		  
			if (mode == "TCP") client.TCPSendClientRequest(hostAddress, tcpPort, message);
			else UDPSendClientRequest(hostAddress, message, udpPort, udprbuffer);
		  
		}
	}

private static String UDPSendClientRequest(String hostAddress, String message, int udpPort, byte[] udprbuffer) {
	// TODO Auto-generated method stub
	String recieveString = "error";
	//System.out.println(message);
	try {	  
		InetAddress ia = InetAddress.getByName(hostAddress);
		DatagramSocket datasocket = new DatagramSocket();
		
		byte[] buffer = new byte[message.length()];
		buffer=message.getBytes();
		DatagramPacket sPacket, rPacket;
		//System.out.println(buffer.length + new String(buffer));
		sPacket = new DatagramPacket(buffer, buffer.length, ia, udpPort);
		//System.out.println("got buffer");
		datasocket.send(sPacket);
		
		rPacket = new DatagramPacket(udprbuffer, udprbuffer.length);
		//System.out.println("getting response");
		datasocket.receive(rPacket);
		//System.out.println("got response");
		recieveString = new String(rPacket.getData(), 0, rPacket.getLength());
		
		//System.out.println("Returned value: "+recieveString);
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(IOException e){
		e.printStackTrace();
	}
	return recieveString;
}
}