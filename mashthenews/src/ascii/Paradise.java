package ascii;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import processing.core.*;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

import oscP5.OscMessage;

public class Paradise extends PApplet implements OSCListener {

	public static final int COLUMNS = 50;
	public static final int ROWS = 30;
	Word words[];
	Vec2 scale;
	
	AABB aabb;
	World world;
	PFont font;
	
	boolean debug = true;
	List<Vec2> debugPoints;
	boolean useMouse = debug;
	
	String sw = "                       /~\\    " + 
				"                      |oo )   " +                          
				"                       \\=/_   " +                          
				"       ___            /  _  \\  " +         
				"     / ()\\          //|/.\\|\\\\ " +                       
                "   _|_____|_        \\ \\_\\/  ||" +                       
                "  | | === | |        \\|\\ /| ||" +                       
                "  |_|  O  |_|        # _ _/ # " +                       
                "   ||  O  ||          | | |   " +                       
                "   ||__*__||          | | |   " +                        
                "  |~ \\___/ ~|         []|[]   " +                         
                "  /=\\ /=\\ /=\\         | | |   " +                         
                "  [_]_[_]_[_]________/_]_[_\\  ";
	
	private OSCReceiver oscReceiver;
	
	private List<Vec2> attractionPoints = Collections.synchronizedList(new LinkedList<Vec2>());
	
	public void setup() {
		//size(800, 600);
		int w = 1280; int h = 800;
		
		background(0,0,0);
		
		// box2D stuff
		aabb = new AABB();
		//aabb.lowerBound = new Vec2(-(COLUMNS/2), -(ROWS/2));
		//aabb.upperBound = new Vec2((COLUMNS/2), (ROWS/2));
		aabb.lowerBound = new Vec2(0.0f, 0.0f);
		aabb.upperBound = new Vec2(COLUMNS, ROWS);
		
		Vec2 gravity = new Vec2(0.0f, 0.0f);
		world = new World(aabb, gravity, true);
		world.setContinuousPhysics(true);
		
		// Create the ground.
		// PolygonDef groundPolyDef = new PolygonDef();
		// groundPolyDef.setAsBox(50.0f, 10.0f);
		// groundPolyDef.density = 0.0f;
		
		// BodyDef groundBodyDef = new BodyDef();
		// groundBodyDef.position.set(0.0f, -10.0f);
		
		// Body groundBody = world.createBody(groundBodyDef);
		// groundBody.createShape(groundPolyDef);
		
		scale = new Vec2();
		scale.x = w/COLUMNS;
		scale.y = h/ROWS;
		
		words = new Word[5];
		words[0] = new Word(this, "mummo", 		(int)random(ROWS), (int)random(COLUMNS));
		words[1] = new Word(this, "hanke", 		(int)random(ROWS), (int)random(COLUMNS));
		words[2] = new Word(this, "value", 		(int)random(ROWS), (int)random(COLUMNS));
		words[3] = new Word(this, "kastanja", 	(int)random(ROWS), (int)random(COLUMNS));
		words[4] = new Word(this, "perus", 		(int)random(ROWS), (int)random(COLUMNS));
		
		font = this.loadFont("Arcade-48.vlw");
		textFont(font);
		textAlign(LEFT,CENTER);
		//noCursor();
		size(w, h);
		debugPoints = new LinkedList();

		oscReceiver = new OSCReceiver(7000);
		oscReceiver.addListener(this, "/attractionpoints");
	}
	
	public World getWorld() { return world; }
	
	public void draw() {
		background(0, 0, 0);
		world.step(1.0f/60, 6);
		synchronized(attractionPoints) {
			//attractionPoints.add(mousePos);
			for (Vec2 v : attractionPoints) {
				// text("Y", (int)v.x*scale.x, (int)v.y*scale.y);
				if(debug) {
					noStroke();
					fill(60, 60, 60);
					rect((int)v.x*scale.x-scale.x/2, (int)v.y*scale.y-scale.y/2, scale.x, scale.y);
				}
				for(int i = 0; i < words.length; i++) {	
					words[i].addAttraction(v);
				}
			}
			for(int i = 0; i < words.length; i++) {	
				words[i].draw();
			}
		}
		
		if(useMouse) {
			text("mouse", 10, this.height - 20);
			Vec2 mousePos = new Vec2((int)((float)mouseX/width*COLUMNS), (int)((float)mouseY/height*ROWS));
			text("X", (int)mousePos.x*scale.x, (int)mousePos.y*scale.y);
			for(int i = 0; i < words.length; i++) {	
				words[i].addAttraction(mousePos);
			}
		}
		
		// make stuff float around randomly for now
		// world.setGravity( new Vec2(random(-10.5f, 10.5f), random(-10.5f, 10.5f)) );
		//LinkedList<Vec2> soonToBeRemoved = new LinkedList<Vec2>(); 
	}
	

	public Word[] getWords() {
		return words;
	}	
	
	public void keyPressed() {
		if(key == 'm') {
			useMouse = !useMouse;
		}
	}
	
	public void mousePressed() {
		for(Vec2 point : debugPoints) {
			if(abs(point.x - mouseX/scale.x) < 10 && abs(point.y - mouseX/scale.y) < 10) {
				
			}
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "ascii.Paradise" });
	}
	
	public void attractAll(Vec2 point) {
		for (int i=0; i < words.length; i++) { 
			words[i].addAttraction(point);
		}
	}

	/**
	 * Processes OSC message contains an array of 3-dimensional coordinates. Always removes old values from memory.
	 * Meaning that attraction points are used as long as new ones are received.
	 */
	public void oscMessageReceived(OscMessage m) {
		if (m.arguments().length%3!=0) {
			PApplet.println("/attractionpoint received but the number of arguments was " + m.arguments().length + " should have been 3!");
		} else {
			int i = 0;
			synchronized(attractionPoints) {
				attractionPoints.clear();
				//println("pointteja tuli " +  m.arguments().length/3);
				while (i < m.arguments().length) {
					Vec2 val = new Vec2((int)(m.get(i).floatValue()*COLUMNS),(int)(m.get(i+1).floatValue()*ROWS));
					attractionPoints.add(val);
					i += 3;
				}
			}
		}
	}
}
