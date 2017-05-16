package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import game.GameWindow;
import helpers.SpaceHelper;
import interfaces.GameObject;
import override.Polygon2;

public class ScreenDamage implements GameObject {
	/*
	private static ArrayList<Polygon2> screenSections;
	static {
		screenSections = new ArrayList<Polygon2>();
		Polygon2 screen = new Polygon2();
		int width = GameWindow.SCREEN_WIDTH;
		int height = GameWindow.SCREEN_HEIGHT;
		int widthInterval = width/10;
		int heightInterval = height/10;
		
		//Add points along the borders of the screen
		for(int x = 0; x <= width; x += widthInterval) {
			screen.addPoint(x, 0);
		}
		for(int y = 0; y <= height; y += heightInterval) {
			screen.addPoint(width, y);
		}
		for(int x = width; x >= 0; x -= widthInterval) {
			screen.addPoint(x, height);
		}
		for(int y = height; y >= 0; y -= heightInterval) {
			screen.addPoint(0, y);
		}
		screenSections.add(screen);
		for(int i = 0; i < 10; i++) {
			screenSections = dividePolygons(screenSections);
		}
	}
	public static ArrayList<Polygon2> dividePolygons(ArrayList<Polygon2> polygons) {
		ArrayList<Polygon2> dividedSections = new ArrayList<Polygon2>();
		for(Polygon section : polygons) {
			int[] xPoints = section.xpoints;
			int[] yPoints = section.ypoints;
			int nPoints = section.npoints;
			int startIndex = (int) Helper.random(section.npoints);
			int startX = xPoints[startIndex];
			int startY = yPoints[startIndex];
			int endIndex = (int) Helper.random(section.npoints);
			int endX = xPoints[endIndex];
			int endY = yPoints[endIndex];
			Polygon2 left = new Polygon2();
			Polygon2 right = new Polygon2();
			
			//Both sides contain the starting point
			left.addPoint(startX, startY);
			right.addPoint(startX, startY);
			
			//Begin at the point directly after the starting point and go forward
			for(int i = 0; i < section.npoints; i++) {
				int x = xPoints[(i + startIndex + 1) % nPoints];
				int y = yPoints[(i + startIndex + 1) % nPoints];
				double compare = (endX - startX)*(y - startY) - (endY - startY)*(x - startX);
				if(compare > 0) {
					right.addPoint(x, y);
				} else if(compare < 0) {
					left.addPoint(x, y);
				} else {
					//If this point is along the line, then both sides should have it
					right.addPoint(x, y);
					left.addPoint(x, y);
				}
			}
			//Add the end point
			left.addPoint(endX, endY);
			right.addPoint(endX, endY);
			
			//Close the polygons and add points along their edges
			for(Polygon2 side : new Polygon2[] {left, right}) {
				System.out.println("Closing polygon");
				side.addPoint(side.getPoint(0));
				
				side.insertPointsAlongEdges((int) Helper.random(250) + 250);
			}
			dividedSections.add(left);
			dividedSections.add(right);
		}
		return dividedSections;
	}
	*/
	private static final TexturePaint[] snow;
	static {
		int count = 10;
		snow = new TexturePaint[count];
		for(int i = 0; i < count; i++) {
			BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			int[] pixels = ( (DataBufferInt) image.getRaster().getDataBuffer() ).getData();
			for(int j = 0; j < pixels.length; j++) {
				pixels[j] = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), 255).getRGB();
			}
			System.out.println(image);
			snow[i] = new TexturePaint(image, new Rectangle(0, 0, 16, 16));
		}
	}
	ArrayList<Point2D> points;
	ArrayList<Shape> shapes;
	BufferedImage effect;
	public ScreenDamage() {
		points = new ArrayList<Point2D>();
		shapes = new ArrayList<Shape>();
		effect = null;
	}
	public ScreenDamage(Point2D origin) {
		this();
		points.add(origin);
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < 10; i++) {
			points.add(new Point2D.Double(GameWindow.randomGameWidth(), GameWindow.randomGameHeight()));
		}
		Polygon nextEffect = new Polygon();
		Point2D first = SpaceHelper.random(points);
		nextEffect.addPoint((int) first.getX(), (int) first.getY());
		for(Point2D p : points) {
			if(first.distance(p) < 200 && Math.random() < 1) {
				nextEffect.addPoint((int) p.getX(), (int) p.getY());
			}
		}
		nextEffect.addPoint((int) first.getX(), (int) first.getY());
		//nextEffect.addPoint(nextEffect.xpoints[0], nextEffect.ypoints[0]);
		shapes.add(nextEffect);
		for(int i = 0; i < 5; i++) {
			Point2D p1 = SpaceHelper.random(points);
			Point2D p2 = SpaceHelper.random(points);
			shapes.add(new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		}
		
		effect = new BufferedImage(GameWindow.SCREEN_WIDTH, GameWindow.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = effect.createGraphics();
		for(Shape s : shapes) {
			g2D.setPaint(SpaceHelper.random(snow));
			g2D.draw(s);
			g2D.fill(s);
		}
	}
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(effect, 0, 0, (ImageObserver) null);
		
	}
}
