package view 

import scala.swing._
import scala.math._
import misc._

class TiledGraphicViewport[T](tgv: TiledGraphicView[T]) extends Component with ComponentUtils {

	var leftmostX: Int = 0
	var topmostY: Int = 0
	private var _width: Int = tgv.model.width
	private var _height: Int = tgv.model.height
	
	def width: Int = _width
	def width_=(w: Int) {
		_width = w
//		refreshPreferredSize(width = w * tgv.tileWidth)
	}
	
	def height: Int = _height
	def height_=(h: Int) {
		_height = h
//		refreshPreferredSize(height = h * tgv.tileHeight)
	}
	
	def centerAt(col: Int, row: Int) {
		leftmostX = col - (width / 2) 
		topmostY = row - (height / 2)
	}            
	
	def anchorAt(col: Int, row: Int) {
		leftmostX = col
		topmostY = row
	}
	
	def moveTo(dir: Direction) = dir match {
		case North => topmostY -= 1
		case South => topmostY += 1
		case East => leftmostX -= 1
		case West => leftmostX += 1
	}
	
	private def drawTiles(g: Graphics2D) {
		for (
		    drawCol <- 0 until width;
		    drawRow <- 0 until height
		) {
			val col = drawCol + leftmostX
			val row = drawRow + topmostY
			for (tile <- tgv.model(col, row))
//				tgv.drawTileAt(g, tile, drawCol * tgv.tileWidth, drawRow * tgv.tileHeight)
			  println();
		}
	}
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		drawTiles(g)
	}
  
}