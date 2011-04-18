package ascii;

import org.jbox2d.common.Vec2;

public class Word {
	Letter[] letters;
	int r, g, b;
	int row, column;
	
	public Word(Paradise parent, String word, int row, int column) {
		letters = new Letter[word.length()];
		r = g = b = 255;
		this.row = row;
		this.column = column;
		for(int i = 0; i < word.length(); i++) {
			char rndChar = word.charAt(i);
			letters[i] = new Letter(parent, rndChar);
		}
	}
	
	/**
	 * 
	 * @param point - Coordinates in ROWS/COLUMNS - notation
	 */
	public void addAttraction(Vec2 point) {
		for(int i = 0; i < letters.length; i++) {
			letters[i].addAttraction(point);
		}
	}
	
	public void draw() {
		for(int i = 0; i < letters.length; i++) {
			letters[i].draw();
		}
	}
	
}
