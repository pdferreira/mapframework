package view

import java.awt.Shape
import java.awt.Rectangle
import java.awt.Polygon

trait TileTesselation {
	// the Shapes returned by different calls may be the same object
	def shape(col: Int, row: Int): Shape
	
	def deltaX(col: Int, row: Int): Int
	
	def deltaY(col: Int, row: Int): Int
	
}

class SquareTesselation(val side: Int) extends TileTesselation {
  
	private val squareShape: Shape = new Rectangle(side - 1, side - 1)
  
	def shape(col: Int, row: Int): Shape = squareShape
	
	def deltaX(col: Int, row: Int): Int = col * side
	
	def deltaY(col: Int, row: Int): Int = row * side
	
}

class HexagonTesselation(val side: Int) extends TileTesselation {
    
	private val alpha: Double = math.toRadians(60)
  
	private val xMargin: Int = (side * math.sin(alpha)).asInstanceOf[Int]
	
	private val yMargin: Int = (side * math.cos(alpha)).asInstanceOf[Int]
	
	private val hexagonShape: Shape = {
		val xs = Array(
			0, 1, xMargin - 2, xMargin - 1,
			xMargin, xMargin + 1, 2 * xMargin - 2, 2 * xMargin - 1,
			2 * xMargin - 1, 2 * xMargin - 2, xMargin + 1, xMargin,
			xMargin - 1, xMargin - 2, 1, 0
		)
		val ys = Array(
			yMargin, yMargin - 1, 1, 0,
			0, 1, yMargin - 1, yMargin,
			yMargin + side, yMargin + side + 1, 2 * yMargin + side - 1, 2 * yMargin + side,
			2 * yMargin + side, 2 * yMargin + side - 1, yMargin + side + 1, yMargin + side 
		)
		new Polygon(xs, ys, xs.length)
	}
	
	def shape(col: Int, row: Int): Shape = hexagonShape
	
    def deltaX(col: Int, row: Int): Int = col * 2 * xMargin + (row % 2) * xMargin
    	
    def deltaY(col: Int, row: Int): Int = row * (yMargin + side + 1)
      	
}

class TriangleTesselation(side: Int) extends TileTesselation {

	private val alpha: Double = math.toRadians(60)
  
	private val height: Int = (side * math.sin(alpha)).asInstanceOf[Int]
  
	private val pointingDown: Shape = {
		val xs = Array(0, side - 1, side / 2)
		val ys = Array(0, 0, height)
		new Polygon(xs, ys, xs.length)
	}
	
	private val pointingUp: Shape = {
		val xs = Array(side / 2 - 1, side - 1, 0)
		val ys = Array(0, height, height)
		new Polygon(xs, ys, xs.length)
	}
	
	def shape(col: Int, row: Int): Shape = {
		if ((col + row) % 2 == 0)
			pointingDown
		else
		    pointingUp
	}
	
	def deltaX(col: Int, row: Int): Int = col * (side / 2) + (col + ((row + 1) % 2)) / 2
	
	def deltaY(col: Int, row: Int): Int = row * (height + 1)
  
}