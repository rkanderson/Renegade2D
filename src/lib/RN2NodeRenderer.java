package lib;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
		//TODO implement
		renderingLineup.add(n);
		if(n.children.size() > 0) {
			for(RN2Node child : n.children) {
				scheduleNodeForRendering(child);
			}
		}
	}
	public void renderAllNodes(Graphics g) {
		
		for(RN2Node n: renderingLineup) {
			// Calculate generic render coordinates
			int renderX, renderY;
			renderX = (int)(n.getAbsolutePosition().x + scene.width*scene.anchorX);
			renderY = (int)(n.getAbsolutePosition().y + scene.height*scene.anchorY);
			if(cam != null) {
				renderX -= cam.getAbsolutePosition().x;
				renderY -= cam.getAbsolutePosition().y;
			}
			//System.out.println("rendering node at "+renderX+", "+renderY);
			
			// Case specific rendering
			if(n instanceof RN2PolygonNode) {
				RN2PolygonNode p = (RN2PolygonNode)n;
				int[] renderXVert = new int[p.numPoints];
				int[] renderYVert = new int[p.numPoints];
				for(int i=0; i<p.numPoints; i++) {
					RN2Point rotatedPoint = rotatePointAboutOrigin(
							new RN2Point(p.xVertices[i], p.yVertices[i]), n.getAbsoluteZRotation());
					renderXVert[i] = (int)(renderX + rotatedPoint.x);
					renderYVert[i] = (int)(renderY + rotatedPoint.y);
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
