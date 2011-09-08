package view

import scala.swing._
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import misc._

abstract class SquareTiledGraphicView[T](val model: TiledLayer[T]) extends Component with ComponentUtils {
	
	def tileToImage(tile: Tile[T]): Option[Image]
  
	private var tileW: Int = _
	private var tileH: Int = _
	var tileAngle: Double = _
	
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
	
	def drawTileAt(g: Graphics2D, tile: Tile[T], baseX: Double, baseY: Double) {
		if (tile == null)
			return
		
		val at = new AffineTransform
		at.translate(tileHeight * math.sin(tileAngle) + baseX, baseY)
		at.rotate(tileAngle)
		
		for (img <- tileToImage(tile))
			g.drawImage(img, at, null)
			
		drawTileContour(g, at, baseX, baseY)
	}
	
	// debug
	private def drawTileContour(g: Graphics2D, at: AffineTransform, baseX: Double, baseY: Double) {
		val r = new Rectangle(0, 0, tileWidth - 1, tileHeight - 1)
		g.draw(at.createTransformedShape(r))
	}
	
	private def drawTiles(g: Graphics2D) {
		val dxCol = tileWidth * math.cos(tileAngle)
		val dyCol = tileWidth * math.sin(tileAngle)
		val dxRow = - tileHeight * math.sin(tileAngle)
		val dyRow = tileHeight * math.cos(tileAngle)
		val baseX = (model.height - 1) * -dxRow
		
		for (
			col <- 0 until model.width;
			row <- 0 until model.height
		) {	
			for (tile <- model(col, row))
				drawTileAt(g, tile, baseX + col * dxCol + row * dxRow, col * dyCol + row * dyRow)
		}
	}
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		drawTiles(g)
	}
	
}