package Question3;


//sample code from garg github
public class LamportClock {
	int c;
	
	//everyone starts with 1
	public LamportClock() {
		c = 1;
	}
	
	//return clock value
	public int getValue() {
		return c;
	}
	
	public void clockTick() {
		c+=1;
	}
	
	public void sendAction(){
		c+=1;
	}
	
	public void receiveAction(int sentValue) {
		c = Math.max(c, sentValue)+1;
	}
	
}
