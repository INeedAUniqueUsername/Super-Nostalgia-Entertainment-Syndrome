package Space;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public interface IBody {

	public abstract void setShapes(ArrayList<Polygon> ss);

	public abstract void setShapes(Polygon... pp);

	public abstract ArrayList<Polygon> getShapes();

	public abstract void addShape(Polygon s);

	public abstract void updateShapes();

	public abstract void draw(Graphics g);

	public abstract void drawWrapClones(Graphics g, Polygon p);

	public abstract void drawTranslate(Graphics g, Polygon p, int x, int y);

}