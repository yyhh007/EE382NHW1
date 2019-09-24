package Question3;

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
	public static void main (String[] args) {

		Scanner sc = new Scanner(System.in);
		int myID = sc.nextInt();
		int numServer = sc.nextInt();
		int numSeat = sc.nextInt();
		
		
		for (int i = 0; i < numServer; i++) {
		  // TODO: parse inputs to get the ips and ports of servers
		}
		
		// TODO: handle request from clients
	}
}