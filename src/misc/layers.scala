package misc

trait Layer

class TiledLayer[T](val width: Int, val height: Int) extends Layer {

	private val tiles = Array.ofDim[Tile[T]](height, width)
	
	def coordsAreValid(col: Int, row: Int) = {
		col >= 0 &&
		col < width &&
		row >= 0 &&
		row < height
	}
	
	def apply(col: Int, row: Int) = {
		if (coordsAreValid(col, row))
			Some(tiles(row)(col))
		else
			None
	}
	
	def update(col: Int, row: Int, tile: Tile[T]) = { 
		if (coordsAreValid(col, row)) {
			tiles(row)(col) = tile
		}
	}
	
}

class TiledLayer3D[T](val width: Int, val height: Int, val depth: Int) extends Layer {
  
	private val tiles = Array.ofDim[Tile[T]](depth, height, width)
  
	def coordsAreValid(col: Int, row: Int, plane: Int) = {
		col >= 0 &&
		col < width &&
		row >= 0 &&
		row < height
		plane >= 0 &&
		plane < depth
	}
  
	def apply(col: Int, row: Int, plane: Int) = {
		if (coordsAreValid(col, row, plane))
			Some(tiles(plane)(row)(col))
		else
			None
	}
	
	def update(col: Int, row: Int, plane: Int, tile: Tile[T]) {
		if (coordsAreValid(col, row, plane)) {
			tiles(plane)(row)(col) = tile
		}
	}
	
}