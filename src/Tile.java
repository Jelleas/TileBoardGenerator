/**
 * Representation of a tile. The tile contains a unique id, and its
 * dimensions. It does not contain a location of the tile!
 * @author Jelle van Assema
 */
public class Tile {
	private static int nTiles = 0; // used for generation of unique id
	private int width, height;
	private int id; // unique id
	
	public Tile(int width, int height) {
		this.width = width;
		this.height = height;
		this.id = nTiles;
		nTiles++;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean hasSameDimensions(Tile tile) {
		int tileWidth = tile.getWidth();
		int tileHeight = tile.getHeight();
		return (width == tileWidth && height == tileHeight) ||
				(width == tileHeight && height == tileWidth);
	}
	
	public String toString() {
		return "id: " + id + ", width: " + width + ", height: " + height;
	}
	
	public int getLargestDimension() {
		return height > width ? height : width;
	}
	
	public int getSmallestDimension() {
		return height < width ? height : width;
	}
	
	public static int getNumTiles() {
		return nTiles;
	}
}
