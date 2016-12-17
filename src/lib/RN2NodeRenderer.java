package lib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

class RN2NodeRenderer {
	public ArrayList<RN2Node> renderingLineup = new ArrayList<RN2Node>();
	private RN2Scene scene;
	private RN2CameraNode cam;
	public RN2NodeRenderer() {
		
	}
	public void setScene(RN2Scene s) {
		this.scene = s;
		this.cam = s.camera;
	}
	public void scheduleNodeForRendering(RN2Node n) {
		// Insert the node at the right location in the rendering list
		// The highest zPositon goes in the front
//		insertNodeAtRightPositionInRenderingLineup(n);
		double[] allZs = new double[renderingLineup.size()];
		for(int i=0; i<allZs.length; i++) {
			allZs[i] = renderingLineup.get(i).getAbsoluteZPosition();
		}
		int insertAtIndex = getInsertionIndexForZPos(n.getAbsoluteZPosition(), allZs);
		renderingLineup.add(insertAtIndex, n);
		if(n.children.size() > 0) {
			for(RN2Node child : n.children) {
				scheduleNodeForRendering(child);
			}
		}
	}
	
	/**
	 * Given a zPosition variable and an array of other z positions,
	 * this method will return the index that the given z should be inserted
	 * at so that the highest values are at the end of the array. Recursion power!
	 * Note: this method doesn't actually insert anything, it just returns a "hypothetical"
	 * index to insert at.
	 * @param zPos the z position value  that is to be inserted
	 * @param arr the array that the new value will be inserted into
	 * @return the insertion index
	 */
	private int getInsertionIndexForZPos(double zPos, double[] arr) {
		if(arr.length == 0) {
			return 0;
		} else if(arr.length == 1) {
			if(zPos >= arr[0]) {
				return 1;
			} else {
				return 0;
			}
		} else {
			// Divide the array into two and recall the method but with the half
			// that must contain the new the z value
			double middleValue = arr[arr.length/2];
			if(zPos>=middleValue) {
				return arr.length/2 + getInsertionIndexForZPos(zPos, 
						Arrays.copyOfRange(arr, arr.length/2, arr.length));
			} else {
				return getInsertionIndexForZPos(zPos, Arrays.copyOfRange(arr, 0, arr.length/2));
			}
		}
	}
	
	int i = 0;
	public void renderAllNodes(Graphics g) {
		
		if(i < 1) {
		String allNodes = "[";
		for(RN2Node n: renderingLineup) {
			allNodes+=n.getAbsoluteZPosition()+", ";
		}
		allNodes+="]";
		System.out.println("Node rendererer: "+allNodes);
		i++;
		}
//		
		for(RN2Node n: renderingLineup) {
			if(n instanceof RN2PolygonNode) {
				RN2PolygonNode p = (RN2PolygonNode)n;
				int[] renderXVert = new int[p.vertices.length];
				int[] renderYVert = new int[p.vertices.length];
				for(int i=0; i<p.vertices.length; i++) {
					RN2Point vertPoint = new RN2Point(p.vertices[i]);
					RN2Point absPtPos = n.convertPointToNode(vertPoint, scene.camera);
					renderXVert[i] = (int)(absPtPos.x + scene.width*scene.anchorX);
					renderYVert[i] = (int)(absPtPos.y + scene.height*scene.anchorY);
				}
				
				int alpha = (int)(p.getAbsoluteOpacity() * 255);
				g.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
				g.fillPolygon(renderXVert, renderYVert, p.vertices.length);
			}
		}
		renderingLineup.clear();
	}
}
