import java.util.Random;

/**
 * Generator for boards filled with tiles. These tiles all have unique sizes
 * and are created randomly.
 * @author Jelle van Assema
 */
public class TileBoardGenerator {
	/**
	 * Generate a board with dimensions width, height and randomly put tiles
	 * on the board, until none of the tiles can be cut down into two smaller
	 * portions.
	 * @param width
	 * @param height
	 * @return board
	 */
	public static TileBoard generate(int width, int height) {
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
	public static TileBoard generate(int width, int height, int nTiles) {
		return generate(width, height, nTiles, new Random());
	}
	
	/**
	 * Generate a board with dimensions width, height and randomly put tiles
	 * on the board, until none of the tiles can be cut down into two smaller
	 * portions or the limit nTiles is reached. Seeds the random generator.
	 * @param width
	 * @param height
	 * @param nTiles
	 * @return board
	 */
	public static TileBoard generate(int width, int height, int nTiles, long seed) {
		return generate(width, height, nTiles, new Random(seed));
	}
	
	/**
	 * Generate a board with dimensions width, height and randomly put tiles
	 * on the board, until none of the tiles can be cut down into two smaller
	 * portions or the limit nTiles is reached. Uses the random generator supplied.
	 * @param width
	 * @param height
	 * @param nTiles
	 * @return board
	 */
	public static TileBoard generate(int width, int height, int nTiles, Random rand) {
		TileBoard field = new TileBoard(width, height, rand);
				
		while(field.getNumTiles() < nTiles && !field.isFinished())
			field.splitRandomTile();
		
		return field;
	}
	
	public static void main(String[] args) {
		// Create board with dimensions 10, 10 with a maximum of 10 tiles.
		TileBoard field = TileBoardGenerator.generate(10, 10, 10);
		field.print();
	}
}