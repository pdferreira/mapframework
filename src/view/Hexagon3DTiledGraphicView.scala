package view

import java.awt.Paint
import java.awt.Polygon
import java.awt.RenderingHints
import scala.swing._
import misc._
import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.Shape

abstract class Hexagon3DTiledGraphicView[T](val model: TiledLayer3D[T]) extends Component with ComponentUtils {
	
	def tileToImage(tile: Tile[T]): Option[Image]
  
	private var side: Int = _
	private var alpha: Double = math.toRadians(60)
	private var xMargin: Int = _
	private var yMargin: Int = _
	
	// row = horizontal, col = / diag, plane = \ diag
	
	def hexagonSide: Int = side
	def hexagonSide_=(hs: Int) {
		implicit def doubleToInt(d: Double): Int = math.round(d).asInstanceOf[Int]
		side = hs
		xMargin = side * math.sin(alpha)
		yMargin = side * math.cos(alpha)
		val newWidth = xMargin * 2 * model.width + xMargin
		val newHeight = (yMargin + side + 1) * model.height + yMargin
		refreshPreferredSize(newWidth, newHeight)
	}
	
//	val colors = Array(Color.GREEN.darker(), Color.GREEN.darker().darker(), Color.GREEN.darker().darker().darker())

	def drawTileAt(g: Graphics2D, tile: Tile[T], baseX: Int, baseY: Int) {
		if (tile == null)
			return
			
		val tileWidth = 2 * xMargin
		val tileHeight = 2 * yMargin + side
		
		for (img <- tileToImage(tile)) {
			val clipHex = createClipHexagon()
			clipHex.translate(baseX, baseY)
			g.setClip(clipHex)
			g.drawImage(img, baseX, baseY, tileWidth, tileHeight, null)
			g.setClip(null)
		}
			
		drawHexagon(g, baseX, baseY)
	}
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		for (
		    plane <- 0 until model.depth;
		    row <- 0 until model.height;
		    col <- 0 until model.width
		) {
//			val baseX = col * (xMargin * 2) + (row % 2) * xMargin
//			val baseY = row * (yMargin + side + 1)
			val baseX = (row - plane) * xMargin + col * (2 * xMargin)
			val baseY = (row - plane) * (yMargin + side + 1) 
			for (tile <- model(col, row, plane)) {
//				drawTileAt(g, tile, baseX, baseY)
				drawHexagon(g, baseX, baseY)
				g.drawString("(%d,%d,%d)".format(col, row, plane), baseX+side, baseY+side)
			}
		}
	}
	
	def drawHexagon(g: Graphics2D, baseX: Int, baseY: Int) {
		import java.awt.{RenderingHints => RH}
		val hexagon = createHexagon()
		hexagon.translate(baseX, baseY)
//		g.setRenderingHint(RH.KEY_ANTIALIASING, RH.VALUE_ANTIALIAS_ON)
//		g.fillPolygon(hexagon)
		g.drawPolygon(hexagon)
	}
	
	private def hexagonAxisX = Array(
	    0, 1, xMargin - 2, xMargin - 1,
	    xMargin, xMargin + 1, 2 * xMargin - 2, 2 * xMargin - 1,
	    2 * xMargin - 1, 2 * xMargin - 2, xMargin + 1, xMargin,
	    xMargin - 1, xMargin - 2, 1, 0
	)
	
	private def hexagonAxisY = Array(
	    yMargin, yMargin - 1, 1, 0,
	    0, 1, yMargin - 1, yMargin,
	    yMargin + side, yMargin + side + 1, 2 * yMargin + side - 1, 2 * yMargin + side,
	    2 * yMargin + side, 2 * yMargin + side - 1, yMargin + side + 1, yMargin + side 
	)
	
	private def createHexagon() = new Polygon(hexagonAxisX, hexagonAxisY, 16)
	
	// TODO: Improve clip accuracy~
	private def clipAxisX = {
		val original = hexagonAxisX
		for (i <- (0 to 1) union (14 to 15))
			original(i) -= 1
		for (i <- 6 to 9)
			original(i) += 1
		original
	}
	
	private def clipAxisY = {
		val original = hexagonAxisY
		for (i <- 2 to 5)
			original(i) -= 1
		for (i <- 10 to 13)
			original(i) += 1
		original
	}
	
	private def createClipHexagon() = new Polygon(clipAxisX, clipAxisY, 16)
	
}