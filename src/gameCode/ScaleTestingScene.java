package gameCode;

import java.awt.Color;
import java.awt.event.KeyEvent;

import lib.*;

public class ScaleTestingScene extends RN2Scene {

	RN2Node containerNode;
	RN2PolygonNode containedPolygon;
	
	public ScaleTestingScene(double width, double height) {
		super(width, height);
	}

	@Override
	public void initialize() {
		containerNode = new RN2Node();
		this.addChild(containerNode);
		
		containedPolygon = new RN2PolygonNode(new double[]{-50, 50, 50, -50}, 
				new double[]{50, 50, -50, -50}, 4);
		//containerNode.addChild(containedPolygon);
		
		RN2PolygonNode speck1 = 
				RN2PolygonNode.RectBuilder.builder().withWidth(60).withHeight(60).build();
		this.addChild(speck1);
		speck1.zPosition = 1;
		speck1.color = Color.BLUE;
	}

	@Override
	public void update(double deltaTime) {
		containedPolygon.zRotation += Math.PI*3 * deltaTime;
		containerNode.yScale += 0.5 * deltaTime;
		containerNode.zRotation += Math.PI /2 * deltaTime;
	}

	@Override
	public void keyDown(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
