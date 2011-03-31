package ascii;

import java.awt.geom.*;

public class Letter {
	
	Point2D.Double speed;
	Point2D.Double acceleration;
	Point2D.Double position;
	
	Paradise parent;
	char letter;
	
	public Letter(Paradise parent, char letter) {
		this.parent = parent;
		this.letter = letter;
		this.speed = new Point2D.Double();
		this.acceleration = new Point2D.Double();
		this.position = new Point2D.Double();
	}
	
	void draw() {
		double pixelX = this.parent.width / Paradise.COLUMNS
				* this.position.getX();
		double pixelY = this.parent.height / Paradise.ROWS
				* this.position.getY();
		parent.text(letter, (int)pixelX, (int)pixelY);
	}
}
