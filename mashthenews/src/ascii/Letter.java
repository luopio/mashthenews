package ascii;

import java.awt.geom.*;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class Letter {
	
	private PolygonDef polygonDef;
	private BodyDef bodyDef;
	private Body body;

	Paradise parent;
	char letter;
	
	public Letter(Paradise parent, char letter, float x, float y) {
		this.parent = parent;
		this.letter = letter;
	
		// Dynamic body
		polygonDef = new PolygonDef();
		polygonDef.setAsBox(1.0f, 1.0f);
		polygonDef.density = 1.0f;
		polygonDef.friction = 0.3f;
  
		bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		
		body = parent.getWorld().createBody(bodyDef);
		body.createShape(polygonDef);
		body.setMassFromShapes();
	
	}
	
	void draw() {
		//double pixelX = this.parent.width / Paradise.COLUMNS
		//		* this.position.getX();
		//double pixelY = this.parent.height / Paradise.ROWS
		//		* this.position.getY();
		Vec2 pos = body.getPosition();
		float pixelX = (int)pos.x * this.parent.scale.x;
		float pixelY = (int)pos.y * this.parent.scale.y;
		parent.text(letter, (int)pixelX, (int)pixelY);
	}
	
	void addAttraction(Vec2 attractionPoint) {
		Vec2 pos = body.getPosition();
		Vec2 rndForce = new Vec2(parent.random(-1.5f, 1.5f), parent.random(-1.5f, 1.5f));
		body.applyForce(rndForce, new Vec2(50, 10));
	}
}
