package misc;

import scala.swing.Component
import java.awt.Dimension

trait ComponentUtils extends Component {

	protected def refreshPreferredSize(
		width: Int = preferredSize.width,
		height: Int = preferredSize.height
	) = {
		preferredSize = new Dimension(width, height)
	}
	
}