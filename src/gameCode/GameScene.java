package gameCode;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.*;

public class GameScene extends RN2Scene {

	RN2PolygonNode pn;
	public GameScene(RN2GamePanel gamePanel, double width, double height) {
		super(gamePanel, width, height);
	}

	@Override
	public void initialize() {
//		pn = RN2PolygonNode.RectBuilder.builder().withAnchorX(0.5).withAnchorY(0).build();
		pn = RN2PolygonNode.RegularPolygonBuilder.builder().withNumOfSides(3).withRadius(100).build();
		addChild(pn);
		pn.position.x = 0;
		pn.zPosition = 2;
		pn.color = Color.GREEN;
		
		RN2PolygonNode speck = RN2PolygonNode.RectBuilder.builder().withWidth(50).withHeight(50).build();
		speck.color = new Color(254, 164, 196);
		speck.zPosition = 1;
		pn.addChild(speck);
		
		RN2PolygonNode speck2 = speck.duplicate();
		speck2.color = Color.RED;
		speck2.position.x = 60;
		speck2.position.y = 70;
		speck2.zPosition = 1;
		speck.addChild(speck2);
		
		
		RN2PolygonNode otherTriangleThing = pn.duplicate();
		otherTriangleThing.position.x = -120;
		otherTriangleThing.setOpacity(0.5);
		addChild(otherTriangleThing);
		
		RN2PolygonNode isoSpeck = speck.duplicate();
		isoSpeck.position.y = -100;
		addChild(isoSpeck);
		
		
		for(int i=0; i<200; i++) {
			RN2PolygonNode x = RN2PolygonNode.RectBuilder.builder().withWidth(20).withHeight(30).build();
			this.addChild(x);
			x.zPosition = 0;
			x.position.x = 10*i;
			x.position.y = 1*i;
			x.setOpacity(i/500.0);
		}
		
//		pn = RN2PolygonNode.RegularPolygonBuilder.builder().withNumOfSides(3).withRadius(100).build();
//		pn.addChild();
//		RN2PolygonNode p2 = pn.duplicate();
//		p2.position.x = 0;
//		p2.zPosition = 5;
//		
//		addChild(p2);
		
	}

	@Override
	public void update(double deltaTime) {
		ArrayList<Integer> keysDown = gamePanel.get().keysDown;
		if(keysDown.contains(KeyEvent.VK_UP)) {
			camera.position.y += 50*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_DOWN)) {
			camera.position.y -= 50*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_LEFT)) {
			camera.zRotation += 3.14*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_RIGHT)) {
			camera.zRotation -= 3.14*deltaTime;
		}
//		camera.xScale += 0.5*deltaTime;
//		camera.yScale += 0.5*deltaTime;

		
		pn.zRotation += Math.PI / 2 * deltaTime;
//		pn.position.y += 100 * deltaTime;
//  		pn.position.x += 100 * deltaTime;
//		pn.xScale += 0.5*deltaTime;
//		pn.yScale += 0.5*deltaTime;
//		try {
//			pn.incrementOpacity(-deltaTime);
//		} catch(IllegalStateException e) {
//			
//		}
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
