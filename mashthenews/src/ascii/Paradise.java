package ascii;

import processing.core.PApplet;
import processing.core.*;

public class Paradise extends PApplet {

	public static int ROWS = 200;
	public static int COLUMNS = 200;
	Letter letters[];
	
	public void setup() {
		letters = new Letter[50];
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i < letters.length; i++) {
			char rndChar = alphabet.charAt( (int)random(alphabet.length()) );
			letters[i] = new Letter(this, rndChar);
			println("comon");
			int r = (int)random(ROWS);
			int c = (int)random(COLUMNS);
			letters[i].position.setLocation(c, r);
			letters[i].speed.setLocation(0, 0);
		}
	}
	
	public void draw() {
		for(int i = 0; i < letters.length; i++) {
			letters[i].draw();
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "Jaegermaister" });
	}
}
