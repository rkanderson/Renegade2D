package lib;

import java.awt.Color;

public class RN2PolygonNode extends RN2Node {
	RN2Point[] vertices;
	public Color color = Color.RED;
	int numPoints;
	public RN2PolygonNode(double[] xv, double[] yv, int numPoints) {
		vertices = new RN2Point[numPoints];
		for(int i=0; i<numPoints; i++) {
			vertices[i] = new RN2Point(xv[i], yv[i]);
		}
		this.numPoints= numPoints;
	}
	public RN2PolygonNode(RN2Point[] vertices) {
		this.vertices = vertices;
	}
	
	// Builders added for convenience
	public static class RectBuilder {
		private double width=100, height=100;
		private double anchorX = 0.5, anchorY = 0.5;
		public static RectBuilder builder() {
			return new RectBuilder();
		}
		public RectBuilder withWidth(double w) {width = w; return this;}
		public RectBuilder withHeight(double h) {height = h; return this;}
		public RectBuilder withAnchorX(double ax) {anchorX = ax; return this;}
		public RectBuilder withAnchorY(double ay) {anchorY = ay; return this;}
		public RN2PolygonNode build() {
			RN2Point[] vertices = new RN2Point[4];
			vertices[0] = new RN2Point(width*anchorX, height*anchorY);
			vertices[1] = new RN2Point(width*anchorX, height*(1-anchorY));
			vertices[2] = new RN2Point(width*(1-anchorX), height*(1-anchorY));
			vertices[3] = new RN2Point(width*(1-anchorX), height*anchorY);
			return new RN2PolygonNode(vertices);
		}
	}
	public static class RegularPolygonBuilder {
		private int numSides = 3;
		private double radius = 50;
		public static RegularPolygonBuilder builder() {
			return new RegularPolygonBuilder();
		}
		public RegularPolygonBuilder withNumOfSides(int n) {numSides = n; return this;}
		public RegularPolygonBuilder withRadius(double r) {radius = r; return this;}
		public RN2PolygonNode build() {
			RN2Point[] vertices = new RN2Point[numSides];
			double deltaAngle = 2*Math.PI/numSides;
			for(int i=0; i<numSides; i++) {
				vertices[i] = new RN2Point(radius*Math.cos(i*deltaAngle), 
						radius*Math.sin(i*deltaAngle));
			}
			return new RN2PolygonNode(vertices);
		}
	}
}
