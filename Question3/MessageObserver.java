package Question3;

public class MessageObserver extends Observer{

	private CSMessageObserver CSmessageObserver;

	public MessageObserver(CSMessageObserver o) {
		this.CSmessageObserver = o;
		this.CSmessageObserver.attach((java.util.Observer) this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		return;
	}

	
}
