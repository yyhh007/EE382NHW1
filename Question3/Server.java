package Question3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


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

		//Scanner sc = new Scanner(System.in);
		//int myID = sc.nextInt();
		//int numServer = sc.nextInt();
		//int numSeat = sc.nextInt();
		
		
		new Thread(new Runnable() {
	    	public void run() {
	    		System.out.println("tcp server on port 8025 started:");	
	    		try {
	    			ServerSocket listener  = new ServerSocket(8025);
	    			Socket s;
	    			
	    			while ((s = listener.accept())!= null) {
	    				Thread t = new TCPServerThread(10, s, 1, new int [] {8030});
	    				t.start();
	    			}
	    		}catch(IOException e) {
	    			System.out.println(e);
	    		}
	    	}
	    }).start();
		
		new Thread(new Runnable() {
	    	public void run() {
	    		System.out.println("tcp server on port 8030 started:");	
	    		try {
	    			ServerSocket listener  = new ServerSocket(8030);
	    			Socket s;
	    			
	    			while ((s = listener.accept())!= null) {
	    				Thread t = new TCPServerThread(10, s, 1, new int [] {8025});
	    				t.start();
	    			}
	    		}catch(IOException e) {
	    			System.out.println(e);
	    		}
	    	}
	    }).start();
		
		/*for (int i = 0; i < numServer; i++) {
		  // TODO: parse inputs to get the ips and ports of servers
			String serverIP = sc.nextLine();
			
			
			
			
			new Thread(new Runnable() {
		    	public void run() {
		    		System.out.println("tcp server on port 8030 started:");	
		    		try {
		    			ServerSocket listener  = new ServerSocket(8030);
		    			Socket s;
		    			
		    			while ((s = listener.accept())!= null) {
		    				//Thread t = new TCPServerThread(s);
		    				//t.start();
		    			}
		    		}catch(IOException e) {
		    			System.out.println(e);
		    		}
		    	}
		    }).start();
		}*/
		
		// TODO: handle request from clients
	}
}