package ascii;

import processing.core.*;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class Paradise extends PApplet {

	public static int COLUMNS = 80;
	public static int ROWS = 60;
	Letter letters[];
	Vec2 scale;
	
	AABB aabb;
	World world;
	
	public void setup() {
		// box2D stuff
		aabb = new AABB();
		//aabb.lowerBound = new Vec2(-(COLUMNS/2), -(ROWS/2));
		//aabb.upperBound = new Vec2((COLUMNS/2), (ROWS/2));
		aabb.lowerBound = new Vec2(0.0f, 0.0f);
		aabb.upperBound = new Vec2(COLUMNS, ROWS);
		
		Vec2 gravity = new Vec2(0.0f, 0.1f);
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
		
		int w = 800; int h = 600;
		scale = new Vec2();
		scale.x = w/COLUMNS;
		scale.y = h/ROWS;
		
		letters = new Letter[25];
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i < letters.length; i++) {
			char rndChar = alphabet.charAt( (int)random(alphabet.length()) );
			float r = random(ROWS);
			float c = random(COLUMNS);
			letters[i] = new Letter(this, rndChar, c, r);
		}
		
		size(w, h);
	}
	
	public World getWorld() { return world; }
	
	public void draw() {
		background(0, 0, 0);
		world.step(1.0f/60, 10);
		
		for(int i = 0; i < letters.length; i++) {
			letters[i].draw();
		}
		world.setGravity( new Vec2(random(-10.5f, 10.5f), random(-10.5f, 10.5f)) );
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "Jaegermaister" });
	}
}
