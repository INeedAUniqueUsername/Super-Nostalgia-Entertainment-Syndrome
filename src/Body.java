import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class Body {
	private ArrayList<Polygon> shapes;
	public Body() {
		resetShapes();
	}
	public Body(ArrayList<Polygon> ss) {
		setShapes(ss);
	}
	public final void resetShapes() {
		setShapes(new ArrayList<Polygon>());
	}
	public final void setShapes(ArrayList<Polygon> ss) {
		shapes = ss;
	}
	public final void addShape(Polygon s) {
		shapes.add(s);
	}
	public void updateShapes() {
	}
	public final void draw(Graphics g) {
		for(Polygon s : shapes) {
			g.drawPolygon(s);
		}
	}
}