package ascii;

import java.awt.geom.*;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import processing.core.PApplet;

public class Letter {
	
	public PolygonDef polygonDef;
	public BodyDef bodyDef;
	public Body body;

	Paradise parent;
	char letter;
	
	float distanceScaler = (float)0.03;
	
	public Letter(Paradise parent, char letter) {
		this(parent, letter, 0, 0);
	}
	
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
		bodyDef.linearDamping = 1.0f;
		
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
		//PApplet.println(pixelX + " " + pixelY);
		parent.text(letter, (int)pixelX, (int)pixelY);
	}
	
	/**
	 * 
	 * @param attractionPoint - in COLUMNS/ROWS space
	 */
	void addAttraction(Vec2 attractionPoint) {
		//Vec2 pos = body.getPosition();
		//Vec2 point = new Vec2(parent.mouseX / this.parent.scale.x, parent.mouseY / this.parent.scale.y);
		//Vec2 force = pos;

		//b2Vec2 P(pt.x/OFX_BOX2D_SCALE, pt.y/OFX_BOX2D_SCALE);
		// EDIT: // Gyl täi nyt toimii, sanoisin, hiiren arvot menee kolumni/rivi-akselille...
		//Vec2 P = new Vec2(	parent.mouseX / this.parent.scale.x, 
		//					parent.mouseY / this.parent.scale.y); 
		
		Vec2 P = new Vec2(	(float)attractionPoint.x , 
							(float)attractionPoint.y ); 
		//PApplet.println(attractionPoint);

        //b2Vec2 D = P - body->GetPosition();
        Vec2 D = P;
        //D.sub(body.getPosition()); // Tämä rivi ei tee mitään, minkä todistaa alla oleva println!!!
        D.x = D.x - body.getPosition().x;
        D.y = D.y - body.getPosition().y;
        //PApplet.println(D + " " + P + " " + body.getPosition());
        
        float xdis = body.getPosition().x - attractionPoint.x;
        float ydis = body.getPosition().y - attractionPoint.y;
        P.normalize();
        
        double distance = Math.sqrt(xdis * xdis + ydis * ydis); 
        Vec2 F = D.mul((float)(distance / distanceScaler));

        // PApplet.println(distance);
        
        body.applyForce(F, P);
        // body.setLinearVelocity(F);
        
        /*P.Normalize();
        b2Vec2 F = amt * D;
        body->ApplyForce(F, P);*/
		
        /*force.sub(point);
		Vec2 rndForce = new Vec2(parent.random(-1.5f, 1.5f), parent.random(-1.5f, 1.5f));
		body.applyForce(force, pos);*/
	}
}
