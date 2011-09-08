package misc

trait Tile[T] {
	def ttype: T
	def walkable: Boolean
}

class FreeTile[T](t: T) extends Tile[T] {
	def ttype = t
	def walkable = true
}