package ascii;

public class Physics {

	OSCReceiver oscReceiver;
	
	public Physics(int oscReceiverPort) {
		oscReceiver = new OSCReceiver(oscReceiverPort);
	}
	
	
}
