/**
 * Generator for boards filled with tiles. These tiles all have unique sizes
 * and are created randomly.
 * @author Jelle van Assema
 */
public class TileFieldGenerator {
	/**
	 * Generate a board with dimensions width, height and randomly put tiles
	 * on the board, until none of the tiles can be cut down into two smaller
	 * portions.
	 * @param width
	 * @param height
	 * @return board
	 */
	public static TileField generate(int width, int height) {
		return generate(width, height, Integer.MAX_VALUE);
	}
	
	/**
	 * Generate a board with dimensions width, height and randomly put tiles
	 * on the board, until none of the tiles can be cut down into two smaller
	 * portions or the limit nTiles is reached.
	 * @param width
	 * @param height
	 * @param nTiles
	 * @return board
	 */
	public static TileField generate(int width, int height, int nTiles) {
		TileField field = new TileField(width, height);
		
		while(field.getNumTiles() < nTiles && !field.isFinished())
			field.splitRandomTile();
		
		return field;
	}
	
	public static void main(String[] args) {
		// Create board with dimensions 10, 10 with a maximum of 10 tiles.
		TileField field = TileFieldGenerator.generate(10, 10, 10);
		field.print();
	}
}