package lib;

import java.awt.Color;

public class RN2LineNode extends RN2Node {
	/**
	 * A node that looks like an ordinary line. Because it IS an ordinary line!
	 * The only parts of this node that will be affected by transforms are the start
	 * and end points.
	 */
	public RN2Point pointA, pointB;
	public double thickness = 10;
	public Color color = Color.RED;
	public RN2LineNode(RN2Point pointA, RN2Point pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
	}
	public RN2LineNode(double x1, double y1, double x2, double y2) {
		this(new RN2Point(x1, y1), new RN2Point(x2, y2));
	}
	
	@Override
	public RN2LineNode duplicate() {
		RN2LineNode clone = new RN2LineNode(0, 0, 0, 0);
		this.copyLineNodeClassLevelAttributesToOther(clone);
		return clone;
	}
	
	protected void copyLineNodeClassLevelAttributesToOther(RN2LineNode other) {
		super.copyNodeClassLevelAttributesToOther(other); //<-do for parent
		other.pointA = new RN2Point(this.pointA);
		other.pointB = new RN2Point(this.pointB);
		other.color = color;
		other.thickness = thickness;
	}
}
