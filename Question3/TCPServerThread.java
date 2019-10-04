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


public abstract class TCPServerThread extends Thread implements Lock{
	Seats seat;
	Socket theClient;
	Queue <Timestamp>requestq;
	LamportClock c;
	int csAccepted = 0;
	
	Socket server;
	Scanner din;
	PrintStream pout;
	//open tcp socket
	public void getSocket(String hostAddress, int port) throws IOException{
		
		server = new Socket(hostAddress, port);
		din = new Scanner(server.getInputStream());
		pout = new PrintStream(server.getOutputStream());
	}
	
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
	
	public TCPServerThread(int seatNumber, Socket s, int tcpPortNumbers) {
		this.seat= new Seats();
		seat.initSeats(seatNumber);
		theClient = s;
		
		//we got a queue that has max size the number of servers, since each server can request once only
		requestq = new PriorityQueue<Timestamp>(tcpPortNumbers, new Comparator<Timestamp>() {
			public int compare(Timestamp a, Timestamp b) {return Timestamp.compare(a, b);}
			
		});
	}
	
	public synchronized void requestCS(int pid) {
		c.clockTick();
		requestq.add(new Timestamp(c.getValue(), pid));
	}
	
	public synchronized void releaseCS() {
		requestq.remove();
		
		
	}
	
	public synchronized void handleRequest() {
		
		
	}
	
	//may not be right, should be in cs to change seats
	public synchronized void synchSeats(Seats newSeat) {
		
		this.seat = newSeat;
	}
	//System.currentTimeMillis()
	public void run() {
		try {
			Scanner s = new Scanner(theClient.getInputStream());
			PrintWriter pout = new PrintWriter(theClient.getOutputStream());
			
			String [] commandList = s.nextLine().split(" ");
			
			byte [] returnByte = null;
			switch (commandList[0]) {
			case "reserve":
				returnByte = seat.reserveSeat(commandList[1]).getBytes();
				break;
			case "booSeat":
				returnByte = seat.bookSeat(commandList).getBytes();
				break;
			case "delete":
				returnByte = seat.delete(commandList[1]).getBytes();
				break;
			case "search":
				returnByte = seat.searchName(commandList[1]).getBytes();
				break;
			
			}			
			//System.out.println("Recived command: "+s.nextLine());
			pout.println(new String(returnByte));
			pout.flush();
			theClient.close();
			//
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	
}