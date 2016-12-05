package gameCode;

import java.awt.Color;
import java.awt.event.KeyEvent;

import lib.*;

public class GameScene extends RN2Scene {

	RN2PolygonNode pn, pn2, pn3;
	public GameScene(double width, double height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		pn = new RN2PolygonNode(new double[]{-50, 50, 50, -50}, new double[]{50, 50, -50, -50}, 4);
		addChild(pn);
		pn.position.x = 0;
		pn.color = Color.GREEN;
		pn.name = "green";
		
		pn2 = new RN2PolygonNode(new double[]{-50, 50, 50, -50}, new double[]{50, 50, -50, -50}, 4);
		pn.addChild(pn2);
		pn2.color = Color.RED;
		pn2.position.x = 50;
		pn2.zPosition = -1;
		pn2.name = "red";
		
		pn3 = new RN2PolygonNode(new double[]{-50, 50, 50, -50}, new double[]{50, 50, -50, -50}, 4);
		pn2.addChild(pn3);
		pn3.position.x = 100;
		pn3.zPosition = 10;
		pn3.color = Color.BLUE;
		
		RN2PolygonNode pn4 = new RN2PolygonNode(new double[]{-50, 50, 50, -50}, new double[]{50, 50, -50, -50}, 4);
		pn2.addChild(pn4);
		pn4.position.x = -100;
		pn4.zPosition = 10;
		pn4.color = Color.BLUE;
		
	}

	@Override
	public void update(double deltaTime) {
		// TODO Auto-generated method stub
		pn.zRotation += Math.PI / 2 * deltaTime;
//		pn.position.y += 100 * deltaTime;
		pn2.zRotation += Math.PI*4 * deltaTime;
//		pn.position.x += 100 * deltaTime;
		
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
