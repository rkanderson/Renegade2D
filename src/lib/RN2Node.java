package lib;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RN2Node {
	// A generic class to represent any game object. Visible or invisible
		// Should contain basic universal properties such as coordinates, rotation,
		// and scale
		public String name = "unnamed";
		public RN2Point position = new RN2Point(0, 0);
		public double zPosition = 0;
		public double zRotation = 0; // in radians
		public double xScale = 1, yScale = 1;
		
		public ArrayList<RN2Node> children = new ArrayList<RN2Node>();
		public WeakReference<RN2Node> parent;
		public RN2Node() {
			
		}
		
		public boolean addChild(RN2Node newNode) {
			if(newNode.parent == null) {
				children.add(newNode);
				newNode.parent = new WeakReference<RN2Node>(this);
				return true;
			} else {
				System.out.println("Cannot add a child that already has a parent.");
				return false;
			}
		}
		
		public double getAbsoluteZPosition() {
			// returns the zPosition from the ultimate perspective (ie relative to the highest parent)
			if(parent==null) {
				return zPosition;
			} else {
				return zPosition + parent.get().getAbsoluteZPosition();
			}
		}
		
		public double getAbsoluteZRotation() {
			// returns the zPosition from the ultimate perspective (ie relative to the highest parent)
			if(parent==null) {
				return zRotation;
			} else {
				return zRotation + parent.get().getAbsoluteZRotation();
			}
		}
		
		public RN2Point getAbsolutePosition() {
			if(parent==null) {
				return new RN2Point(this.position); // return a dummy object
			} else {
				RN2Point pos = new RN2Point(this.position);
				RN2Point parentAbs = parent.get().getAbsolutePosition();
				pos.x += parentAbs.x;
				pos.y += parentAbs.y;
				pos.rotateAboutOtherPoint(parentAbs, parent.get().getAbsoluteZRotation());
				return pos;
			}
		}
		
}
