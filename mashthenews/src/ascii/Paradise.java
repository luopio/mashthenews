package ascii;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import processing.core.*;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import oscP5.OscMessage;

public class Paradise extends PApplet implements OSCListener {

	public static int COLUMNS = 50;
	public static int ROWS = 30;
	Word words[];
	Vec2 scale;
	
	AABB aabb;
	World world;
	PFont font;
	
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
		size(800, 600);
		int w = 800; int h = 600;
		
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

		oscReceiver = new OSCReceiver(7000);
		oscReceiver.addListener(this, "/attractionpoints");
	}
	
	public World getWorld() { return world; }
	
	public void draw() {
		background(0, 0, 0);
		world.step(1.0f/60, 6);
		Vec2 mousePos = new Vec2((int)((float)mouseX/width*COLUMNS), (int)((float)mouseY/height*ROWS));
		text("X", (int)mousePos.x*scale.x, (int)mousePos.y*scale.y);
		synchronized(attractionPoints) {
			//attractionPoints.add(mousePos);
			for (Vec2 v : attractionPoints) {
				text("Y", (int)v.x*scale.x, (int)v.y*scale.y);
				for(int i = 0; i < words.length; i++) {	
					words[i].addAttraction(v);
				}
			}
			for(int i = 0; i < words.length; i++) {	
				//words[i].addAttraction(mousePos);
				words[i].draw();
			}
		}

		
		// make stuff float around randomly for now
		// world.setGravity( new Vec2(random(-10.5f, 10.5f), random(-10.5f, 10.5f)) );
		//LinkedList<Vec2> soonToBeRemoved = new LinkedList<Vec2>(); 
	}
	

	public Word[] getWords() {
		return words;
	}	

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "ascii.Paradise" });
	}
	
	public void attractAll(Vec2 point) {
		for (int i=0; i < words.length; i++) { 
			words[i].addAttraction(point);
		}
	}

	@Override
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
					//for (int w=0; w < words.length; w++) { 
					//	words[w].addAttraction(val);
					//}
					i += 3;
				}
			}
		}
	}
}
