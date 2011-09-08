package misc

import scala.swing._
import javax.imageio.ImageIO
import java.io.File
import scala.swing.event.KeyPressed
import view._

object Test extends App {
	val map = new TiledLayer3D[Char](5, 5, 5)
	map(0,0,0) = new FreeTile('a')
	map(0,1,0) = new FreeTile('#')
	map(1,2,0) = new FreeTile('a')
	map(0,2,0) = new FreeTile('#')
	
	val tileset = new Tileset("res/tiles/tileset01.png", 50, 50)
	
	val view = new Hexagon3DTiledGraphicView(map) {
		hexagonSide = 50
			
		private val charToImage: Map[Char, Image] = Map(
			'a' -> tileset(1, 0),
			'#' -> tileset(0, 0)
		) 
			
		def tileToImage(tile: Tile[Char]): Option[Image] = charToImage.get(tile.ttype)
	}
	
	val frame = new MainFrame {
		title = "Map"
		contents = view
		visible = true
	}
	
}