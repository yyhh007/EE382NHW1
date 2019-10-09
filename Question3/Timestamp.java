package Question3;

public class Timestamp {

	int clock, pid;
	
	public String timestampToString() {		
		return Integer.toString(clock)+"_"+Integer.toString(pid);
	}
	
	public Timestamp(int clock, int pid) {
		super();
		this.clock = clock;
		this.pid = pid;
	}
	
	public static int compare(Timestamp a, Timestamp b) {
		
		if(a.clock>b.clock) return 1;
		if(b.clock>a.clock) return 0;
		
		if(a.pid<b.pid) return 1;
		
		return 0;
	}
	
	public int getClock() { return clock;}
	public int getpid() {return pid;}
}
