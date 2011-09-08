package misc

import scala.swing._
import scala.swing.event._

case class MovedTo(dir: Direction) extends Event

// Wraps a key event publisher, republishing the appropriate KeyPresseds as MoveTos
class MovementPublisher(comp: Component) extends Publisher {
	
	val keyToDirection = Map(
	    Key.Up -> North,
	    Key.Down -> South,
	    Key.Left -> West,
	    Key.Right -> East
	)
  
	listenTo(comp.keys)
	reactions += {
		case KeyPressed(_, key, _, _) => keyToDirection.get(key) match {
			case None => ()
			case Some(dir) => publish(MovedTo(dir))
		}	
	}
}