package examples

import scala.swing._
import javax.imageio.ImageIO
import java.io.File
import scala.swing.event.KeyPressed
import view._
import misc._

object simpleViewportTest extends App {
	val map = new TiledLayer[Char](5, 5)
	map(0,0) = new FreeTile('a')
	map(1,2) = new FreeTile('a')
	map(0,2) = new FreeTile('#')
	
	val view = new TiledTextView(map, '_')
	view.draw()
	
	val tileset = new Tileset("res/tiles/tileset01.png", 50, 50)
	
	val view2 = new TiledGraphicView(map) {
		tileWidth = 50
		tileHeight = 50
			
		private val charToImage: Map[Char, Image] = Map(
			'a' -> tileset(1, 0),
			'#' -> tileset(0, 0)
		) 
			
		def tileToImage(tile: Tile[Char]): Option[Image] = charToImage.get(tile.ttype)
	}
	
	val frame = new MainFrame {
		title = "Map"
		contents = view2
		visible = true
	}
	
	val dialog = new Dialog {
		title = "Diag"
		contents = new TiledGraphicViewport(view2) {
			width = 3
			height = 3
			centerAt (0, 0)
			
			listenTo(new MovementPublisher(this))
			reactions += {
				case MovedTo(dir) =>
					moveTo(dir)
					repaint()
			}
			focusable = true	// won't receive key events otherwise
		}
		visible = false
	}
	
}