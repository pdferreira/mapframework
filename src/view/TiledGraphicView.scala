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
  	
	val tesselation: TileTesselation
	
	def init() {
		val width: Int = (0 until model.height).map { (row: Int) =>
			val dx = tesselation.deltaX(model.width - 1, row)
		    val shapeW = tesselation.shape(model.width - 1, row).getBounds().getWidth()
			dx + shapeW
		}.max.asInstanceOf[Int]
		val height: Int = (0 until model.width).map { (col: Int) =>
		  	val dy = tesselation.deltaY(col, model.height - 1)
		  	val shapeH = tesselation.shape(col, model.height - 1).getBounds().getHeight()
		  	dy + shapeH
		}.max.asInstanceOf[Int]
		refreshPreferredSize(width + 1, height + 1)
	}
		
	private def drawTileAt(g: Graphics2D, tile: Tile[T], col: Int, row: Int) {
//		if (tile == null)
//			return
//		
//		for (img <- tileToImage(tile)) {
		    val shape = tesselation.shape(col, row)
			val bounds = shape.getBounds()
			val x = tesselation.deltaX(col, row)
			val y = tesselation.deltaY(col, row)
			val af = AffineTransform.getTranslateInstance(x, y)
//			if ((col + row) % 2 == 0)
//			  g.setColor(Color.RED)
//			else
//			  g.setColor(Color.BLACK)
			g.draw(af.createTransformedShape(shape))
//			g.setClip(af.createTransformedShape(shape))
//			g.drawImage(img, x, y, bounds.width, bounds.height, null)
//		}
//		drawTileContour(g, x, y)
//		drawTileText(g, tile, x, y)
	}
	
	// debug
//	private def drawTileContour(g: Graphics2D, baseX: Int, baseY: Int) {
//		g.drawRect(baseX, baseY, tileWidth - 1, tileHeight - 1)
//	}
//	
//	// debug
//	private def drawTileText(g: Graphics2D, tile: Tile[T], baseX: Int, baseY: Int) {
//		val text = tile.ttype.toString
//		val font = g.getFont
//		val textBounds = font.getStringBounds(text, g.getFontRenderContext)
//		val centerX = baseX + (tileWidth >> 1)
//		val centerY = baseY + (tileHeight >> 1)
//
//		g.drawString(
//		  text,
//		  centerX - (math.round(textBounds.getWidth) >> 1),
//		  centerY + (math.round(textBounds.getHeight) >> 1)
//		)
//	}
//	
//	// debug
//	private def drawGrid(g: Graphics2D) {
//		for (
//			col <- 0 until model.width;
//			row <- 0 until model.height
//		) {
//			g.drawRect(
//				col * tileWidth,
//				row * tileHeight,
//				tileWidth,
//				tileHeight
//			)
//		}
//	}
//	
	private def drawTiles(g: Graphics2D) {
		for (
			col <- 0 until model.width;
			row <- 0 until model.height
		) {
			for (tile <- model(col, row))
				drawTileAt(g, tile, col, row)
		}
	}
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		drawTiles(g)
		//drawGrid(g)
	}
	
}

