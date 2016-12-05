package lib;

import java.awt.Color;
import java.awt.event.KeyEvent;

abstract public class RN2Scene extends RN2Node{
	public double width, height;
	public double anchorX = 0.5, anchorY = 0.5;
	
	public RN2Scene(double width, double height) {
		this.width = width; this.height = height;
	}
	
	public Color backgroundColor = Color.BLACK;
	public RN2CameraNode camera = new RN2CameraNode();
	
	abstract public void initialize();
	abstract public void update(double deltaTime);
	abstract public void keyDown(KeyEvent e);
	abstract public void keyReleased(KeyEvent e);
	
}
