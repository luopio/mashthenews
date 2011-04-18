package ascii;

import processing.core.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class Word {
	Letter[] letters;
	int r, g, b;
	int row, column;
	Paradise parent;
	
	public Word(Paradise parent, String word, int row, int column) {
		letters = new Letter[word.length()];
		r = (int)(100 + parent.random(155));
		g = (int)(100 + parent.random(155));
		b = (int)(100 + parent.random(155));
		this.row = row;
		this.column = column;
		this.parent = parent;
		Letter prevLetter = null;
		for(int i = 0; i < word.length(); i++) {
			char rndChar = word.charAt(i);
			letters[i] = new Letter(parent, rndChar, column + i * 2, row);
			if(prevLetter != null) {
				DistanceJointDef j = new DistanceJointDef();
				j.initialize(	prevLetter.body, letters[i].body, 
								prevLetter.body.getWorldCenter(), letters[i].body.getWorldCenter());
				// j.body1 = prevLetter.body;
				// j.body2 = letters[i].body;
				
				parent.world.createJoint(j);
			}
			prevLetter = letters[i];
		}
	}
	
	public void addAttraction(Vec2 point) {
		for(int i = 0; i < letters.length; i++) {
			letters[i].addAttraction(point);
		}
	}
	
	public void draw() {
		this.parent.fill(r, g, b);
		for(int i = 0; i < letters.length; i++) {
			letters[i].draw();
		}
	}
	
}
