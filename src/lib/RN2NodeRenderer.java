package lib;

import java.awt.Graphics;
import java.util.ArrayList;

class RN2NodeRenderer {
	private ArrayList<RN2Node> renderingLineup = new ArrayList<RN2Node>();
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
		insertNodeAtRightPositionInRenderingLineup(n);
		if(n.children.size() > 0) {
			for(RN2Node child : n.children) {
				scheduleNodeForRendering(child);
			}
		}
	}
	
	/**
	 * As suggested in the name, this method inserts a given node in the right 
	 * position for rendering (based on absolute zPosition) in the renderingLineup array
	 * @param n the node to be inserted
	 */
	private void insertNodeAtRightPositionInRenderingLineup(RN2Node n) {
		// Note to self: in the future, it might be better for the sake of efficiency to
		// use a binary search algorithm instead of linearly traversing the entire array.
		double absZ = n.getAbsoluteZPosition();
		if(renderingLineup.size()==0) {
			renderingLineup.add(n);
			return;
		} else {
			for(int i=0; i<renderingLineup.size(); i++) {
				double otherAbsZ = renderingLineup.get(i).getAbsoluteZPosition();
				if(i==0 && otherAbsZ > absZ) {
					renderingLineup.add(0, n);
					break;
				} else if(i==renderingLineup.size()-1) {
					renderingLineup.add(n);
					break;
				} else if(otherAbsZ > absZ && 
						renderingLineup.get(i-1).getAbsoluteZPosition() <= absZ){
					renderingLineup.add(i, n);
					break;
				}
			}
		}
	}
	
	public void renderAllNodes(Graphics g) {
		
		for(RN2Node n: renderingLineup) {
			if(n instanceof RN2PolygonNode) {
				RN2PolygonNode p = (RN2PolygonNode)n;
				int[] renderXVert = new int[p.numPoints];
				int[] renderYVert = new int[p.numPoints];
				for(int i=0; i<p.numPoints; i++) {
					RN2Point vertPoint = new RN2Point(p.vertices[i]);
					RN2Point absPtPos = n.convertPointToNode(vertPoint, scene);
					renderXVert[i] = (int)(absPtPos.x - cam.position.x + scene.width*scene.anchorX);
					renderYVert[i] = (int)(absPtPos.y - cam.position.y + scene.height*scene.anchorY);
				}

				g.setColor(p.color);
				g.fillPolygon(renderXVert, renderYVert, p.numPoints);
			}
		}
		renderingLineup.clear();
	}
	
	private RN2Point rotatePointAboutOrigin(RN2Point pt, double rad) {
		double rotX = pt.x, rotY = pt.y;
		double centerX = 0; double centerY = 0;
		double newX = centerX + (rotX-centerX)*Math.cos(rad) - (rotY-centerY)*Math.sin(rad);
		double newY = centerY + (rotX-centerX)*Math.sin(rad) + (rotY-centerY)*Math.cos(rad);
		
		return new RN2Point(newX, newY);
	}
}
