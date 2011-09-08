package misc

import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

class Tileset[T](path: String, tileWidth: Int, tileHeight: Int) {
  
	private val image: BufferedImage = ImageIO.read(new File(path))
  
	def apply(col: Int, row: Int): Image = {
	  	image.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight)
	}
  
}