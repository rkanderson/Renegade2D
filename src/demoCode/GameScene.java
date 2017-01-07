package demoCode;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.*;

public class GameScene extends RN2Scene {
	
	RN2PolygonNode ball;
	
	public GameScene(RN2GamePanel gamePanel, double width, double height) {
		super(gamePanel, width, height);
	}

	@Override
	public void initialize() {
		ball = new RN2PolygonNode.RegularPolygonBuilder()
				.withNumOfSides(50).withRadius(80).build();
		ball.color = Color.CYAN;
		ball.position.x = 50;
		ball.position.y = -100;
		this.addChild(ball);
	}

	@Override
	public void update(double deltaTime) {
		ArrayList<Integer> keysDown = gamePanel.get().keysDown;
		if(keysDown.contains(KeyEvent.VK_UP)) {
			ball.position.y += 100*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_DOWN)) {
			ball.position.y += -100*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_LEFT)) {
			ball.position.x += -100*deltaTime;
		}
		if(keysDown.contains(KeyEvent.VK_RIGHT)) {
			ball.position.x += 100*deltaTime;
		}
	}

	@Override
	public void keyDown(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(RN2Click c) {
		// Spawn an epic, spinny triangle where the user clicks!
		RN2PolygonNode triangle = new RN2PolygonNode.RegularPolygonBuilder()
				.withNumOfSides(3).withRadius(50).build();
		triangle.color = Color.GREEN;
		triangle.position = c.getPositionRelativeToNode(this);
		this.addChild(triangle);
		
		triangle.runAction(RN2Action.rotateByValueOverDuration(Math.PI*4, 2));
		triangle.runAction(RN2Action.fadeOutWithDuration(2));
		triangle.runAction(RN2Action.addScaleValueOverDuration(2, 2, 2));
		
	}

	@Override
	public void mouseReleased(RN2Click c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(RN2Click c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(RN2Click c) {
		// TODO Auto-generated method stub
		
	}

}
