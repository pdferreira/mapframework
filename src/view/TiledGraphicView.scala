package view

import scala.swing._
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import misc._
import java.awt.Shape
import java.awt.Polygon

abstract class TiledGraphicView[T](val model: TiledLayer[T]) extends Component with ComponentUtils {
	
	def tileToImage(tile: Tile[T]): Option[Image]
  
	private var tileW: Int = _
	private var tileH: Int = _
	
	def tileWidth: Int = tileW
	def tileWidth_=(tw: Int) {
		tileW = tw
		refreshPreferredSize(width = tileW * model.width)
	}
	
	def tileHeight: Int = tileH
	def tileHeight_=(th: Int) {
		tileH = th
		refreshPreferredSize(height = tileH * model.height)
	}
	
	def drawTileAt(g: Graphics2D, tile: Tile[T], x: Int, y: Int) {
		if (tile == null)
			return
		
		for (img <- tileToImage(tile))
			g.drawImage(img, x, y, tileWidth, tileHeight, null)
		drawTileContour(g, x, y)
//		drawTileText(g, tile, x, y)
	}
	
	// debug
	private def drawTileContour(g: Graphics2D, baseX: Int, baseY: Int) {
		g.drawRect(baseX, baseY, tileWidth - 1, tileHeight - 1)
	}
	
	// debug
	private def drawTileText(g: Graphics2D, tile: Tile[T], baseX: Int, baseY: Int) {
		val text = tile.ttype.toString
		val font = g.getFont
		val textBounds = font.getStringBounds(text, g.getFontRenderContext)
		val centerX = baseX + (tileWidth >> 1)
		val centerY = baseY + (tileHeight >> 1)

		g.drawString(
		  text,
		  centerX - (math.round(textBounds.getWidth) >> 1),
		  centerY + (math.round(textBounds.getHeight) >> 1)
		)
	}
	
	// debug
	private def drawGrid(g: Graphics2D) {
		for (
			col <- 0 until model.width;
			row <- 0 until model.height
		) {
			g.drawRect(
				col * tileWidth,
				row * tileHeight,
				tileWidth,
				tileHeight
			)
		}
	}
	
	private def drawTiles(g: Graphics2D) {
		for (
			col <- 0 until model.width;
			row <- 0 until model.height
		) {
			for (tile <- model(col, row))
				drawTileAt(g, tile, col * tileWidth, row * tileHeight)
		}
	}
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		drawTiles(g)
		//drawGrid(g)
	}
	
}

trait TileShape {
	val shape: Shape
	def deltaX(col: Int, row: Int): Int
	def deltaY(col: Int, row: Int): Int
}

trait SquareShape extends TileShape {
	val side: Int
	val shape: Shape = new Rectangle(side, side)
	def deltaX(col: Int, row: Int): Int = col * side
	def deltaY(col: Int, row: Int): Int = row * side
}

trait HexagonShape extends TileShape {
    val side: Int
    val xs: Array[Int] // TODO
    val ys: Array[Int] // TODO
    val xMargin: Int // TODO
    val yMargin: Int // TODO
	val shape: Shape = {
		new Polygon(xs, ys, 16)
	}
    def deltaX(col: Int, row: Int): Int =
    	col * 2 * xMargin + (row % 2) * xMargin
    def deltaY(col: Int, row: Int): Int =
      	row * (yMargin + side)
}