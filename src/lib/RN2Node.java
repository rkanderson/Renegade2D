package lib;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RN2Node {
	/**
	 * A generic class to represent any game object at all. Visible or invisible.
	 * Contains basic properties such as position, scale, rotation, and opcity.
	 */
	
	public ArrayList<RN2Node> children = new ArrayList<RN2Node>();
	public WeakReference<RN2Node> parent;
//	private int rank = 0; // Indicates the level of organization in the hierarchy this node is at
//	private int[] address = {}; //The length and the elements themselves 
//							   //indicate the exact position in the hierarchy.
	
	public String name = "unnamed";
	public RN2Point position = new RN2Point(0, 0);
	public double zPosition = 0;
	public double zRotation = 0; // in radians
	public double xScale = 1, yScale = 1;
	public double opacity = 1;
	
	public RN2Node() {
		
	}
	
	public void addChild(RN2Node newNode) throws IllegalStateException {
		if(newNode.parent == null) {
			children.add(newNode);
			newNode.parent = new WeakReference<RN2Node>(this);
		} else {
			throw new IllegalStateException("Cannot add a child that already has a parent.");
		}
	}
	
	public void removeChild(RN2Node child) throws IllegalStateException {
		if(children.contains(child)) {
			children.remove(child);
		}
		else {
			throw new IllegalStateException("The child isn't in the "
					+ "children array and therfore can't be removed.");
		}
	}
	
	/**
	 * The address is an array of integers that functions like a map to get to 
	 * this node starting from the highest ancestor. The number of elements in
	 * the array indicates which generation the node is in. The numbers themselves
	 * are indices in children arrays. The final number in the address is what index
	 * this node is at in its parent's children array. If this node itself has no parent,
	 * the array is empty.
	 * @return the node's address.
	 */
	public int[] getAddress() {
		if(parent==null) {
			return new int[]{};
		} else if(parent.get().parent==null) {
			return new int[]{parent.get().children.indexOf(this)};
		} else {
			return ArrayUtils.concatenate(parent.get().getAddress(), 
					new int[]{parent.get().children.indexOf(this)});
		}
	}
	
	/**
	 * Converts a given point from the reference frame of this node to 
	 * the reference frame of a destination node, taking into account position,
	 * zRotation, and x/y scales.
	 * @param point the point to be converted
	 * @param destinationNode the final node the point should be converted to
	 * @return the converted point
	 * @throws IllegalStateException if the conversion is impossible or unnecessary
	 */
	public RN2Point convertPointToNode(RN2Point point, RN2Node destinationNode) throws IllegalStateException {
		if(destinationNode==this) { //A super derp exception
			throw new IllegalStateException("Why would you need to convert the point?"
					+ " Your destination node is the same as your starting node.");
		}
		
		// Base case: If the destinationNode is a direct child of this node
		if(children.contains(destinationNode)) {
			return convertPointToBeRelativeToChild(point, destinationNode);
		}
		
		// Base case: If the destination node is this node's parent
		if(parent != null && parent.get() == destinationNode) {
			return convertPointToBeRelativeToParent(point);
		}
				
		// If none of the base cases are satisfied, we must compute the next move
		// using addresses. Remember that these addresses are like maps that tell you
		// which turns to take and in which order. So if we took a wrong turn earlier, 
		// it means we need to go back up.
		int[] destinationAddress = destinationNode.getAddress();
		int[] myAddress = this.getAddress();
		
		// I know that if this node's generation number is greater or equal than the destination 
		// node, I have to go back up. No matter what.
		if(myAddress.length >= destinationAddress.length) {
			return parent.get().convertPointToNode(
					convertPointToBeRelativeToParent(point), destinationNode);
		}
		
		// MAYBE the destination node is a direct descendant of this node. We can tell 
		// because the first part of the destinationAddress will be the exact same as
		// this node's address.
		else if(ArrayUtils.arraysEqual(myAddress, 
				Arrays.copyOfRange(destinationAddress, 0, myAddress.length))) {
			RN2Node rightPathChild = children.get(destinationAddress[myAddress.length]);
			return rightPathChild.convertPointToNode(convertPointToBeRelativeToChild(point, rightPathChild), destinationNode);
		} 
		
		// Not a direct ancestor. O well. GO up!
		else {
			return parent.get().convertPointToNode(
					convertPointToBeRelativeToParent(point), destinationNode);
		}
		
	}
	
	private RN2Point convertPointToBeRelativeToParent(RN2Point point) {
		RN2Point pt = new RN2Point(point); // Dummy point
		// Just a few steps.
		// -Multiply pt by this node's scale values
		// -Rotate point by this node's zRotation
		// -Add this node's position
		pt.x *= this.xScale;
		pt.y *= this.yScale;
		pt.rotateAboutOriginByRad(this.zRotation);
		pt.x += this.position.x;
		pt.y += this.position.y;
		return pt;
	}
	
	private RN2Point convertPointToBeRelativeToChild(RN2Point point, RN2Node child) {
		// The same as the previous method, but the OPPOSITE
		RN2Point pt = new RN2Point(point);
		pt.x -= child.position.x;
		pt.y -= child.position.y;
		pt.rotateAboutOriginByRad(-child.zRotation);
		pt.x /= child.xScale;
		pt.y /= child.yScale;
		return pt;
	}
	
	
	/**
	 * @return the zPosition from the ultimate perspective (ie relative to the highest parent)
	 */
	public double getAbsoluteZPosition() {
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
	
	//public RN2Point getAbsolutePosition() {
//		if(parent==null) {
//			return new RN2Point(this.position); // return a dummy object
//		} else {
//						
//			RN2Point pos = new RN2Point(this.position);
//
//			pos.rotateAboutOriginByRad(parent.get().getAbsoluteZRotation());
//			
//			RN2Vector parentAbsScale = parent.get().getAbsoluteScale();
//			pos.x *= parentAbsScale.dx;
//			pos.y *= parentAbsScale.dy;
//			
//			RN2Point parentAbsPos = parent.get().getAbsolutePosition();
//			pos.x += parentAbsPos.x;
//			pos.y += parentAbsPos.y;
//			
//			return pos;
//		}
		
	//}
	
//	public RN2Vector getAbsoluteScale() {
//		if(parent==null) {
//			return new RN2Vector(xScale, yScale);
//		} else  {
//			RN2Vector parentScale = parent.get().getAbsoluteScale();
//			return new RN2Vector(xScale * parentScale.dx, yScale * parentScale.dy);
//		}
//	}
//	
	/**
	 * @return the opacity the node should be rendered with
	 */
	public double getAbsoluteOpacity() {
		if(parent==null) {
			return opacity;
		} else  {
			return this.opacity * parent.get().opacity;
		}
	}
		
}
