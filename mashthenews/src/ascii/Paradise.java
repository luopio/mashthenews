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

	public static int COLUMNS = 60;
	public static int ROWS = 40;
	Letter letters[];
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
		
		letters = new Letter[25];//+sw.length()+2];
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i < letters.length; i++) {
			char rndChar = alphabet.charAt( (int)random(alphabet.length()) );
			float r = random(ROWS);
			float c = random(COLUMNS);
			letters[i] = new Letter(this, rndChar, c, r);
			//letters[i] = new Letter(this, (char)59, c, r);
		}
		/*float r = 0;
		float c = 0;
		for (int i = 0; i < sw.length()/2;i++) {
			if (sw.charAt(i)=='\n') {
				r++;
				c = 0;
			}
			letters[25+i] = new Letter(this, sw.charAt(i),r, c);
			println(sw.charAt(i));
			c++;
		}*/
		
		font = this.loadFont("Arcade-48.vlw");
		textFont(font);
		size(w, h);

		oscReceiver = new OSCReceiver(7000);
		oscReceiver.addListener(this, "/attractionpoint");
	}
	
	public World getWorld() { return world; }
	
	public void draw() {
		background(0, 0, 0);
		world.step(1.0f/60, 6);
		
		for(int i = 0; i < letters.length; i++) {
			letters[i].addAttraction(new Vec2(0, 0));
			letters[i].draw();
		}
		// make stuff float around randomly for now
		// world.setGravity( new Vec2(random(-10.5f, 10.5f), random(-10.5f, 10.5f)) );
		LinkedList<Vec2> soonToBeRemoved = new LinkedList<Vec2>(); 
		synchronized(attractionPoints) {
			for (Vec2 v : attractionPoints) {
				text("X", (int)v.x*scale.x, (int)v.y*scale.y);
				soonToBeRemoved.add(v);
			}
			attractionPoints.removeAll(soonToBeRemoved);
		}
	}
	
	public Letter[] getLetters() {
		return letters;
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "ascii.Paradise" });
	}

	@Override
	public void oscMessageReceived(OscMessage m) {
		if (m.arguments().length!=3) {
			PApplet.println("/attractionpoint received but the number of arguments was " + m.arguments().length + " should have been 3!");
		} else {
			//println("" + attractionPoints.size());
			synchronized(attractionPoints) {
				Vec2 val = new Vec2((int)(m.get(0).floatValue()*COLUMNS),(int)(m.get(1).floatValue()*ROWS));
				println(val);
				attractionPoints.add(val);
			}
			for (int i=0; i < letters.length; i++) { 
				letters[i].addAttraction(new Vec2(m.get(0).floatValue()*COLUMNS,m.get(1).floatValue()*ROWS));
			}
		}
	}
}
