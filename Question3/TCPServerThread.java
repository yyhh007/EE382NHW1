package Question3;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;


public class TCPServerThread extends Thread {
	Seats seat = null;
	Socket theClient;
	Queue <Timestamp>requestq;
	LamportClock c;
	int pid;
	int csAccepted;
	int [] portList = null;
	Socket server;
	Scanner din;
	PrintStream pout;
	String hostAddress= "localhost";
	String userCommand= null;
	
	//open tcp socket
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
		 if (returnString.equals("ack")) csAccepted++; 
		 server.close();
		 /*String returnString = din.nextLine();
		 if(returnString == "ok") csAccepted++;
		 returnString = returnString.replace("\t", "\n");
		 server.close();
		 System.out.println(returnString);
		 return returnString;*/
	}
	
	
	
	public TCPServerThread(int seatNumber, Socket s, Seats seat, int tcpPortNumbers, int [] tcpPortList) {
		this.seat= seat;
		
		theClient = s;
		c = new LamportClock();
		this.portList=tcpPortList;
		//we got a queue that has max size the number of servers, since each server can request once only
		requestq = new PriorityQueue<Timestamp>(tcpPortNumbers, new Comparator<Timestamp>() {
			public int compare(Timestamp a, Timestamp b) {return Timestamp.compare(a, b);}	
		});
	}
	
	public synchronized byte[] requestCS(int pid) throws IOException{
		//c.clockTick();
		byte[] returnByte = null;
		for (int port : portList) {
			//System.out.println(Integer.toString(pid));
			//System.out.println(Integer.toString(c.getValue()));
			System.out.println("request "+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
			TCPSendClientRequest(hostAddress, port, "request "+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
			//System.out.println(response);
			//if(response == "ok") csAccepted++;
			
		}
		c.clockTick();
		requestq.add(new Timestamp(c.getValue(), pid));
		
		while(csAccepted != portList.length && pid == requestq.peek().pid) {
			System.out.println("task not head of queue");
			
		}
		System.out.println("everyone said ok and im first in line");
		String [] localCommandList = userCommand.split(" ");
		
		switch (localCommandList[0]) {
			case "reserve":					
				returnByte = seat.reserveSeat(localCommandList[1]).getBytes();
				break;
			case "booSeat":
				returnByte = seat.bookSeat(localCommandList).getBytes();
				break;
			case "delete":
				returnByte = seat.delete(localCommandList[1]).getBytes();
				break;
		}
		this.seat=seat.getCurrentSeatAssignment();
		//seat.loadCurrentSeatStatus(seat.getCurrentSeatAssignment());
		requestq.remove();
		//return;
		c.clockTick();
		
		return returnByte;
	}
	
	public synchronized void sendAck() {
		
	}
	
	
	//on release cs, set queue to pop, and new seat information
	public synchronized void releaseCS(int pid, String changedSeat, String changedName) throws IOException{
		requestq.remove();
		c.clockTick();
		for (int port : portList) {
			//String response = TCPSendClientRequest(hostAddress, port, "release"+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue())+" "+changedSeat+" "+changedName);		
		}
		
	}
	
	public synchronized String handleRequest(String requestString) {
		
		String [] commandList = requestString.split(" ");
		String returnString = null;
		
		return returnString;
	}
	
	//may not be right, should be in cs to change seats
	public synchronized void synchSeats(Seats newSeat) {
		
		this.seat = newSeat;
	}
	public void initSeats() {
		
	}
	
	//System.currentTimeMillis()
	public void run() {
		csAccepted = 0;
		try {
			Scanner s = new Scanner(theClient.getInputStream());
			PrintWriter pout = new PrintWriter(theClient.getOutputStream());
			String command = s.nextLine();
			System.out.println(Integer.toString(theClient.getLocalPort())+" recieved command: "+command);
			String [] commandList = command.split(" ");
			byte [] returnByte = null;
			switch (commandList[0]) {
			case "reserve":
				userCommand = command;
				pid = Integer.parseInt(commandList[commandList.length-1]);
				returnByte = requestCS(Integer.parseInt(commandList[commandList.length-1]));
				
				
				//returnByte = seat.reserveSeat(commandList[1]).getBytes();
				break;
			case "booSeat":
				requestCS(Integer.parseInt(commandList[commandList.length-1]));
				pid = Integer.parseInt(commandList[commandList.length-1]);
				userCommand = command;
				//returnByte = seat.bookSeat(commandList).getBytes();
				break;
			case "delete":
				requestCS(Integer.parseInt(commandList[commandList.length-1]));
				pid = Integer.parseInt(commandList[commandList.length-1]);
				userCommand = command;
				//returnByte = seat.delete(commandList[1]).getBytes();
				break;
			case "search":
				returnByte = seat.searchName(commandList[1]).getBytes();
				break;
			case "request":
				requestq.add(new Timestamp(Integer.parseInt(commandList[2]), Integer.parseInt(commandList[1])));
				c.receiveAction(Integer.parseInt(commandList[2]));
				String returnString = "ack";
				returnByte = returnString.getBytes();
				break;
			case "ack":
				
				break;
			case "release":
				break;
			case "crashSync":
				break;
			case "ackSync":
				break;
			default:
				break;
			}			
			//System.out.println("Recived command: "+s.nextLine());
			System.out.println(Integer.toString(theClient.getLocalPort())+"  current command recieved command: "+command);
			pout.println(new String(returnByte));
			pout.flush();
			theClient.close();
			//
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	
}