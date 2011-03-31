package ascii;

import oscP5.OscMessage;

public interface OSCListener {

	public void oscMessageReceived(OscMessage m);
	
}
