import org.jbox2d.testbed.tests.*;
import org.jbox2d.testbed.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.p5.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.common.*;
import org.jbox2d.util.blob.*;
import org.jbox2d.collision.*;
import org.jbox2d.testbed.timingTests.*;
import org.jbox2d.dynamics.joints.*;

// To work around threading problems with size
boolean sizeSet;
int count = 0;

// Number of links in chain
int numLinks = 10;

// Physics things we must store
Physics physics;
Body body, body2;

void setup() {
  size(640,480,P3D);
  frameRate(60);
  sizeSet = false;
}

void draw() {
  background(0);

//This is a hack to make sure that the sketch size is
//fully initialized before initializing the scene,
//which depends on the width and height values.
//Once Processing 1.0 is released, this should no longer
//be necessary.
  ++count;
  if ( (!sizeSet && count > 10) ) {
    initScene();
  }

  if (mousePressed) {
    //Create a body
    float x0 = mouseX;
    float y0 = mouseY;
    Body randomBod = physics.createCircle(x0, y0, random(5.0f,15f));
    Vec2 vel = new Vec2(random(-30.0f,30.0f),random(-30.0f,30.0f));
    randomBod.setLinearVelocity(vel);

  }

  if (keyPressed) {

    //physics.setCustomRenderingMethod(physics, "defaultDraw");

    //Reset everything
    physics.destroy();
    body = body2 = null;
    initScene();
  }

}

void initScene() {
  physics = new Physics(this, width, height);
  physics.setDensity(1.0f);
  Body b1 = null;
  Body b2 = null;

  // Make a chain of bodies
  for (int i=0; i<numLinks; ++i) {
    body = body2; //bookkeeping, for neighbor connection

    body2 = physics.createRect(100+25*i, 10, 120+25*i, 30);

    // Add a hanging thingy to each body, connect it
    // with a prismatic joint (like a piston)
    Body body3 = physics.createCircle(110+25*i,35,5.0f);
    PrismaticJoint pj = physics.createPrismaticJoint(body2, body3, 0.0f, 1.0f);
    pj.m_enableLimit = true;
    pj.setLimits(-1.0f, 1.0f);

    if (i==0) b1 = body2; // for pulley joint later
    if (i==numLinks-1) b2 = body2;

    if (body == null) {
      // No previous body, so continue without adding joint
      body = body2;
      continue;
    }
    // Connect the neighbors
    physics.createRevoluteJoint(body, body2, 100+25*i, 20);

  }

  // Make a pulley joint
  float groundAnchorAx = 100;
  float groundAnchorAy = 150;
  float groundAnchorBx = 540;
  float groundAnchorBy = 150;
  float anchorAx = 100;
  float anchorAy = 20;
  float anchorBx = 120+(numLinks-1)*25;
  float anchorBy = 20;
  float ratio = 1.0f;

  physics.createPulleyJoint(b1, b2, 
    groundAnchorAx, groundAnchorAy, 
    groundAnchorBx, groundAnchorBy, 
    anchorAx, anchorAy, 
    anchorBx, anchorBy, 
    ratio);
  sizeSet = true;
}
