package test;
import java.io.BufferedReader;
import java.util.ArrayList;
//import java.util.TreeMap;

import processing.core.*;

/*
import org.jbox2d.util.nonconvex.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.testbed.*;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.p5.*;
import org.jbox2d.dynamics.*;
*/

import oscP5.*;
import netP5.*;

public class Testi extends PApplet {	 	 
	
	private OscP5 oscP5;
	
	String inString;
	int charvalue;

	// The serial stuff, change the portname according to your settings
	String portname = "/dev/tty.juhon"; 
	//String portname = "COM3";
	Serial myPort;

	String buf="";

	//int endMsg = 35;
	int lf = 10;
	//String endMsg = "#";
	String temp = "";

	// the Network stuff
	int port = 5333; 
	Server myServer; 

	//variables for sending 
	byte eol = 0; 
	
	
	public void setup() {
	 
		oscP5 = new OscP5(this,12000);
		size(800,600,P2D);
		smooth();
		frameRate(60);		
		
		myServer = new Server(this, port);
		myPort = new Serial(this, portname, 115200);
		println("set up");	  
	}

	 
	
	public void draw() {
		println("hello666");
		delay(2000);

	}
	
	public void oscEvent(OscMessage theOscMessage) {
	  /* print the address pattern and the typetag of the received OscMessage */
	 
	  print("### received an osc message.");
	  print(" addrpattern: "+theOscMessage.addrPattern());
	  println(" typetag: "+theOscMessage.typetag());	  
	}

	public void serialEvent(Serial p) {  
	    inString = (myPort.readString());
	    charvalue = inString.charAt(0);
	  if(charvalue != lf) {
	    buf += inString;
	
	  } else {
	   
	   if (buf.length() > 1) {
	   
	    println(buf);    
	    
	    myServer.write(buf);
	    endTransmission();     
	    
	    // Clear the value of "buf"
	    buf = "";
	    
	   } 
	  } 
	}

	public void endTransmission() {
		myServer.write(eol);   
	}
}
