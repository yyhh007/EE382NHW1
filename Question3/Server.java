package Question3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Arrays;

/*logic
if client 
	sent to all server and client, 
	
	onreceive all acknowlege, add to queue, add a server sync to queue as well
	onrelease let everyone know
	tell server to sync what just happened
if server
	
*/
public class Server {
	public static void main (String[] args) throws IOException{

		
		Scanner sc = new Scanner(System.in);
		int myID = sc.nextInt();
		int numServer = sc.nextInt();
		int numSeat = sc.nextInt();
		int [] serverList =new int[numServer];
		
		
		for (int i = 0; i < numServer; i++) {
			serverList[i]= sc.nextInt();
		}
		
		for (int i = 0, k=0; i < numServer; i++) {
		  	// TODO: parse inputs to get the ips and ports of servers
			int portNumber = serverList[i];
			int [] otherPortList= new int[numServer-1];
			for (int j = 0; j<numServer-1;j++) {
				if (i==j) continue;
				else otherPortList[k++]=serverList[i];
			}
			
			int pid = myID+i;
			new Thread(new Runnable() {
		    	public void run() {
		    		System.out.println("tcp server on port "+Integer.toString(portNumber)+" started:");	
		    		try {
		    			ServerSocket listener  = new ServerSocket(portNumber);
		    			Socket s;
		    			LamportClock c = new LamportClock();
		    			Seats seat = new Seats(numSeat);
		    			Queue <Timestamp>requestq = requestq = new PriorityQueue<Timestamp>(numServer, new Comparator<Timestamp>() {
		    				public int compare(Timestamp a, Timestamp b) {return Timestamp.compare(a, b);}	
		    			});
		    			while ((s = listener.accept())!= null) {
		    				Thread t = new TCPServerThread(numSeat, pid, c, s, requestq, seat, 2, otherPortList, false);
		    				//((TCPServerThread) t).initSeats();
		    				t.start();
		    			}
		    		}catch(IOException e) {
		    			System.out.println(e);
		    		}
		    	}
		    }).start();
			
			
		}		
	}
}