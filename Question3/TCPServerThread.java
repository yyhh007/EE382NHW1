package Question3;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashSet;
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
	int seatNumber;
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
	}
	
	public void TCPSendClientRelease(String hostAddress, int tcpPort, String outMessage) throws IOException {
		 getSocket(hostAddress, tcpPort);
		 pout.println(outMessage);
		 pout.flush();
		 server.close();
	}
	
	public TCPServerThread(int seatNumber, Socket s, Queue q, Seats seat, int tcpPortNumbers, int [] tcpPortList) {
		this.seat= seat;
		this.seatNumber = seatNumber;
		this.theClient = s;
		this.requestq = q;
		c = new LamportClock();
		this.portList=tcpPortList;
		//we got a queue that has max size the number of servers, since each server can request once only
		
	}
	
	public synchronized byte[] requestCS(int pid) throws IOException{
		//c.clockTick();
		byte[] returnByte = null;
		for (int port : portList) {
			System.out.println(Integer.toString(theClient.getLocalPort())+" request "+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
			TCPSendClientRequest(hostAddress, port, "request "+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
		}
		c.clockTick();
		requestq.add(new Timestamp(c.getValue(), pid));
		
		//maybe wrong, or pid is wrong 
		while(csAccepted != portList.length && pid != requestq.peek().pid) {
			System.out.println("task not head of queue");	
		}
		System.out.println("everyone said ok and im first in line");
		String [] localCommandList = userCommand.split(" ");
		
		switch (localCommandList[0]) {
			case "reserve":					
				returnByte = seat.reserveSeat(localCommandList[1]).getBytes();
				break;
			case "bookseat":
				returnByte = seat.bookSeat(localCommandList).getBytes();
				break;
			case "delete":
				returnByte = seat.delete(localCommandList[1]).getBytes();
				break;
		}
		
		this.seat=seat.getCurrentSeatAssignment();
		//seat.loadCurrentSeatStatus(seat.getCurrentSeatAssignment());
		requestq.remove();
		sendAck(seat.getChangedValue());
		//return; change clock click to be in front of send ack
		c.clockTick();
		
		return returnByte;
	}
	
	public synchronized void sendAck (String syncValue) throws IOException{
		for (int port : portList) {
			System.out.println(Integer.toString(theClient.getLocalPort())+" release "+ Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
			TCPSendClientRelease(hostAddress, port, "release "+ syncValue+ " "+Integer.toString(pid)+ " "+ Integer.toString(c.getValue()));
		}
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
				pout.println(new String(returnByte));
				pout.flush();
				//returnByte = seat.reserveSeat(commandList[1]).getBytes();
				break;
			case "bookseat":
				pid = Integer.parseInt(commandList[commandList.length-1]);
				userCommand = command;
				returnByte = requestCS(Integer.parseInt(commandList[commandList.length-1]));
				pout.println(new String(returnByte));
				pout.flush();
				//returnByte = seat.bookSeat(commandList).getBytes();
				break;
			case "delete":
				pid = Integer.parseInt(commandList[commandList.length-1]);
				userCommand = command;
				returnByte = requestCS(Integer.parseInt(commandList[commandList.length-1]));
				pout.println(new String(returnByte));
				pout.flush();
				//returnByte = seat.delete(commandList[1]).getBytes();
				break;
			case "search":
				returnByte = seat.searchName(commandList[1]).getBytes();
				pout.println(new String(returnByte));
				pout.flush();
				break;
			case "request":
				this.requestq.add(new Timestamp(Integer.parseInt(commandList[2]), Integer.parseInt(commandList[1])));
				c.receiveAction(Integer.parseInt(commandList[2]));
				String returnString = "ack";
				returnByte = returnString.getBytes();
				pout.println(new String(returnByte));
				pout.flush();
				break;
			case "ack":
				System.out.println(Integer.toString(theClient.getLocalPort())+"ack");
				break;
			case "release":
				requestq.remove();
				if(commandList.length==5) {
					if(commandList[1].contentEquals("delete")) seat.syncSeats(Integer.parseInt(commandList[2]), "");
					else seat.syncSeats(Integer.parseInt(commandList[2]), commandList[1]);
					
				}
				System.out.println(Integer.toString(theClient.getLocalPort())+"release");
				
				break;
			case "crashSync":
				String timestampString = "";
				for(Timestamp t: requestq) {
					timestampString=timestampString+t.timestampToString()+"/";
				}	
				System.out.println(Integer.toString(theClient.getLocalPort())+"crashsync returning:"+(seat.seatValueToString()+"/"+timestampString));

				returnByte = ("ackSync "+seat.seatValueToString()+" "+timestampString).getBytes();
				pout.println(new String(returnByte));
				pout.flush();
				break;
			case "ackSync":
				String []seatList = commandList[1].split("-");
				String []timestamps = commandList[2].substring(0, commandList[2].length()).split("/");
				for (int i = 1; i<=seatNumber; i++) seat.syncSeats(i, seatList[i-1]);
				for (int i = 0; i<timestamps.length; i++) requestq.add(new Timestamp(Integer.parseInt(timestamps[i].split("-")[0]), Integer.parseInt(timestamps[i].split("-")[1])));
				
				System.out.println(Integer.toString(theClient.getLocalPort())+"acksync");
				break;
			default:
				System.out.println(Integer.toString(theClient.getLocalPort())+"default");
				break;
			}			
			System.out.println(Integer.toString(theClient.getLocalPort())+"  executed current command: "+command);
			theClient.close();
			//
		}catch(IOException e){
			System.out.println(e);
		}
	}
}