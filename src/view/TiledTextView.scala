package view

import misc._

class TiledTextView[T](model: TiledLayer[T], empty: T) {

	def drawTile(t: Tile[T]) {
		print(Option(t).map(_.ttype).getOrElse(empty))
	}
	
	def draw() {
		for (row <- 0 until model.height) {
			for (col <- 0 until model.width) {
				model(col, row) map drawTile
			}
			println()
		}
	}
}