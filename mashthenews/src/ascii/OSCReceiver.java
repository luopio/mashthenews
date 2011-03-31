package ascii;

import java.util.LinkedList;
import java.util.List;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import processing.core.PApplet;

public class OSCReceiver implements OscEventListener {

	private class OSCListenerSlot {
		
		private String addPattern;
		private OSCListener oscListener;
		
		public OSCListenerSlot(String addPattern, OSCListener oscListener) {
			this.addPattern = addPattern;
			this.oscListener = oscListener;
		}
		
		OSCListener getOSCListener() {
			return oscListener;
		}
		
		String getAddPattern() {
			return addPattern;
		}
		
	}
	
	private OscP5 oscP5;
	List<OSCListenerSlot> oscListeners;
	
	public OSCReceiver(int port) {
		oscP5 = new OscP5(this,port);
		oscListeners = new LinkedList<OSCListenerSlot>();
	}
	
	public void addListener(OSCListener l, String addressPattern) {
		oscListeners.add(new OSCListenerSlot(addressPattern,l));
	}
		
	public void oscEvent(OscMessage theOscMessage) {
		for (OSCListenerSlot s: oscListeners) {
			if (s.getAddPattern().equals(theOscMessage.addrPattern())) {
				s.getOSCListener().oscMessageReceived(theOscMessage);
			}
		}
	}

	@Override
	public void oscStatus(OscStatus arg0) {
		// TODO Auto-generated method stub		
	}

}
