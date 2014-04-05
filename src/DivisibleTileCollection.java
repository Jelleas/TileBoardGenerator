import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * A collection of tiles, where no tiles share the same dimension. Keeps track
 * of tiles that can still be split into smaller tiles, thus the two tiles
 * created from such a split do not share dimensions with an existing tile.
 * @author Jelle van Assema
 */
public class DivisibleTileCollection {
	private ArrayList<Tile> divisibleTiles;
	private ArrayList<Tile> undivisibleTiles;
	
	// Dimensions taken by the tiles in the structure.
	// Representation is a string as such: "largestDimension,smallestDimension"
	private HashSet<String> takenDimensions; 
	private Random rand;
	
	public DivisibleTileCollection() {
		this(new Random());
	}
	
	public DivisibleTileCollection(long seed) {
		this(new Random(seed));
	}
	
	public DivisibleTileCollection(Random rand) {
		divisibleTiles = new ArrayList<Tile>();
		undivisibleTiles = new ArrayList<Tile>();
		takenDimensions = new HashSet<String>();
		this.rand = rand;
	}
	
	/**
	 * Get a tile, and remove it from the structure.
	 * @return tile, null if empty
	 */
	public Tile getAndRemoveRandomTile() {
		if (isEmpty())
			return null;
		
		int index = rand.nextInt(divisibleTiles.size());
		Tile tile = divisibleTiles.get(index);
		remove(tile);
		
		return tile;
	}
	
	/**
	 * Remove tile from the structure, once tile is removed,
	 * checks if any existing tile becomes divisible again and updates
	 * accordingly.
	 * @param tile
	 * @return true if success, false otherwise.
	 */
	public boolean remove(Tile tile) {
		if (contains(tile)) {
			try {
				divisibleTiles.remove(tile);
			} catch (Exception e) {
				undivisibleTiles.remove(tile);
			}
			
			takenDimensions.remove(tile.getLargestDimension() + "," + tile.getSmallestDimension());
			
			for (int i = 0; i < undivisibleTiles.size(); i++) {
				Tile undivisibleTile = undivisibleTiles.get(i);
				
				if (isDivisible(undivisibleTile)) {
					undivisibleTiles.remove(undivisibleTile);
					divisibleTiles.add(undivisibleTile);
					i--;
				}
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * Add tile to structure, only if no other tile with dimensions exist.
	 * Categorizes the tile on whether it is divisible, also updates the
	 * existing tiles in the structure.
	 * @param tile 
	 * @return true if success, false otherwise.
	 */
	public boolean add(Tile tile) {
		if (contains(tile))
			return false;
			
		if (isDivisible(tile))
			divisibleTiles.add(tile);
		else
			undivisibleTiles.add(tile);
		
		takenDimensions.add(tile.getLargestDimension() + "," + tile.getSmallestDimension());
		
		for (int i = 0; i < divisibleTiles.size(); i++) {
			Tile divisibleTile = divisibleTiles.get(i);
			
			if (!isDivisible(divisibleTile)) {
				divisibleTiles.remove(divisibleTile);
				undivisibleTiles.add(divisibleTile);
				i--;
			}
		}
		
		return true;
	}
	
	/**
	 * Randomly picks a split that can be performed on the tile, if at all
	 * possible. Returns the dimensions of the two tiles after such a split.
	 * @param tile
	 * @return dimensions of newly created tiles after split, null if not possible.
	 */
	public int[][] getDimensionsAfterSplit(Tile tile) {
		int[] tileDimensions = {tile.getWidth(), tile.getHeight()};
		
		// Find all possible dimensions after split
		ArrayList<int[][]> dimensions = new ArrayList<int[][]>();
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j < tileDimensions[i] / 2; j++) {
				int[] tile1Dimensions = {tileDimensions[0], tileDimensions[1]};
				tile1Dimensions[i] -= j;
				int[] tile2Dimensions = {tileDimensions[0], tileDimensions[1]};
				tile2Dimensions[i] -= tile1Dimensions[i];
				
				if (tile1Dimensions[i] != tile2Dimensions[i]) {
					int[][] dimension = {tile1Dimensions, tile2Dimensions};
					dimensions.add(dimension);
				}
			}
		}
		
		// Shuffle dimensions in order to pick a random split.
		Collections.shuffle(dimensions);
		
		// Return first set of dimensions that is possible.
		for (int[][] dimension : dimensions)
			if (!contains(dimension[0][0], dimension[0][1]) &&
					!contains(dimension[1][0], dimension[1][1]))
				return dimension;
		
		return null;
	}
	
	public boolean contains(Tile tile) {
		return contains(tile.getWidth(), tile.getHeight());
	}
	
	public boolean contains(int width, int height) {
		if (height > width)
			return takenDimensions.contains(height + "," + width);
		else
			return takenDimensions.contains(width + "," + height);	
	}
	
	public boolean isDivisible(Tile tile) {
		return getDimensionsAfterSplit(tile) != null;
	}
	
	public boolean isEmpty() {
		return divisibleTiles.size() == 0;
	}
	
	public String toString() {
		String repr = "";
		for (Tile tile : divisibleTiles)
			repr += tile + " [DIVISIBLE]\n";
		
		for (Tile tile : undivisibleTiles)
			repr += tile + " [UNDIVISIBLE]\n";

		return repr;
	}
	
	public int size() {
		return divisibleTiles.size() + undivisibleTiles.size();
	}
}
