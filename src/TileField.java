import java.util.Random;

/**
 * Representation of a field of tiles. Contains functions for splitting
 * tiles, and keeps the dimensions of all tiles unique.
 * @author Jelle van Assema
 */
public class TileField {
	private Tile[][] field;
	private int width, height;
	private DivisibleTileCollection tiles;
	Random rand;

	public TileField(int width, int height) {
		this(width, height, new Random());
	}
	
	public TileField(int width, int height, long seed) {
		this(width, height, new Random(seed));
	}
	
	public TileField(int width, int height, Random rand) {
		this.rand = rand;
		this.width = width;
		this.height = height;
		
		this.tiles = new DivisibleTileCollection(rand);
		Tile tile = new Tile(width, height);
		this.tiles.add(tile);
		
		this.field = new Tile[width][height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				this.field[i][j] = tile;
	}
	
	/**
	 * Get location as (x,y) of tile.
	 * @param tile
	 * @return location as (x,y)
	 */
	public int[] getLocation(Tile tile) {
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				if (this.field[i][j] == tile)
					return new int[] {i, j};
		
		return null;
	}
	
	public void removeTile(Tile tile) {
		this.tiles.remove(tile);
		
		int[] loc = this.getLocation(tile);
		
		for (int i = loc[0]; i < loc[0] + tile.getWidth(); i++)
			for (int j = loc[1]; j < loc[1] + tile.getHeight(); j++)
				if (this.field[i][j] == tile)
					this.field[i][j] = null;
	}
	
	/**
	 * Put tile on the field at location x,y
	 * @param tile
	 * @param x
	 * @param y
	 * @return true if successful, false otherwise
	 */
	public boolean addTile(Tile tile, int x, int y) {
		if (!this.tiles.contains(tile)) {
			this.tiles.add(tile);
			
			for (int i = x; i < x + tile.getWidth(); i++)
				for (int j = y; j < y + tile.getHeight(); j++)
					this.field[i][j] = tile;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Perform a split operation on a random tile. Where this split occurs
	 * is random.
	 * @return true if success, false otherwise
	 */
	public boolean splitRandomTile() {
		return splitTile(this.tiles.getAndRemoveRandomTile());
	}
	
	/**
	 * Perform a split operation on tile. Where this split occurs is random.
	 * @param tile
	 * @return true if success, false otherwise
	 */
	public boolean splitTile(Tile tile) {
		if (!this.tiles.isDivisible(tile))
			return false;
			
		int[] locTile = this.getLocation(tile);
		this.removeTile(tile);
		
		int[][] dimensions = this.tiles.getDimensionsAfterSplit(tile);
		int widthNewTile1 = dimensions[0][0];
		int heightNewTile1 = dimensions[0][1];
		int widthNewTile2 = dimensions[1][0];
		int heightNewTile2 = dimensions[1][1];
		
		Tile newTile1 = new Tile(widthNewTile1, heightNewTile1);
		this.addTile(newTile1, locTile[0], locTile[1]);
			
		Tile newTile2 = new Tile(widthNewTile2, heightNewTile2);
		
		int[] locNewTile2 = {locTile[0], locTile[1]};
		if (tile.getWidth() == widthNewTile2)
			locNewTile2[1] += heightNewTile1;
		else
			locNewTile2[0] += widthNewTile1;
		
		this.addTile(newTile2, locNewTile2[0], locNewTile2[1]);
		
		return true;
	}
	
	public void print() {
		int maxNumDigits = getNumDigits(Tile.getNumTiles());
		
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				Tile tile = this.field[i][j];
				if (tile != null) {
					int id = tile.getId();
					int nDigits = getNumDigits(id);
					for (int k = nDigits; k < maxNumDigits; k++) {
						System.out.print(" ");
					}
					System.out.print(id + ",");
				} else {
					System.out.print("-,");
				}
			}
			
			System.out.println();
		}
		
		System.out.println("\n" + this.tiles);
	}

	public int getNumTiles() {
		return this.tiles.size();
	}
	
	/**
	 * Check if there are tiles left, that one can perform a split operation on.
	 * @return true if no tiles left, false otherwise.
	 */
	public boolean isFinished() {
		return tiles.isEmpty();
	}

	/**
	 * Helper function, returns the number of digits in n.
	 * @param n
	 * @return number of digits in n.
	 */
	private int getNumDigits(int n) {
		int nDigits = 0;
		for (int i = n; i > 0; i /= 10, nDigits++);
		return nDigits;
	}
}
