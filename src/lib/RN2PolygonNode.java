package lib;

import java.awt.Color;

public class RN2PolygonNode extends RN2Node {
	double[] xVertices, yVertices;
	public Color color = Color.RED;
	int numPoints;
	public RN2PolygonNode(double[] xv, double[] yv, int numPoints) {
		xVertices = xv;
		yVertices = yv;
		this.numPoints= numPoints;
	}
}
