package Question4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TCPServerThread extends Thread {

	Inventory inven;
	Socket theClient;
	
	public TCPServerThread(Inventory i, Socket s) {
		this.inven=i;
		theClient = s;
	}
	
	public void run() {
		try {
			Scanner s = new Scanner(theClient.getInputStream());
			PrintWriter pout = new PrintWriter(theClient.getOutputStream());
			
			//System.out.println("Recived command: "+s.nextLine());
			pout.println("Recived command: "+s.nextLine());
			pout.flush();
			theClient.close();
			//
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	
}
