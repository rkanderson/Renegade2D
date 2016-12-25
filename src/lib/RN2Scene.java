package lib;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

abstract public class RN2Scene extends RN2Node{
	protected WeakReference<RN2GamePanel> gamePanel;
	public double width, height;
	public double anchorX = 0.5, anchorY = 0.5;
	public Color backgroundColor = Color.BLACK;
	public RN2CameraNode camera;
	public ArrayList<RN2Action> runningActions = new ArrayList<RN2Action>();
	
	public RN2Scene(RN2GamePanel gamePanel, double width, double height) {
		this.gamePanel = new WeakReference<RN2GamePanel>(gamePanel);
		this.width = width; this.height = height;
		camera = new RN2CameraNode();
		addChild(camera);
	}
	
	public void runAction(RN2Action action) {
		runningActions.add(action);
	}
	
	public void runAllActionBlocks(double deltaTime) {
		for(int i = runningActions.size()-1; i >= 0; i--) {
			RN2Action a = runningActions.get(i);
			a.timeElapsed += deltaTime;
			if(a.timeElapsed > a.duration) {
				a.timeElapsed = a.duration;
			}
			a.runActionBlock();
			if(a.timeElapsed == a.duration) {
				runningActions.remove(a);
			}
		}
	}
	
	abstract public void initialize();
	abstract public void update(double deltaTime);
	
	abstract public void keyDown(KeyEvent e);
	abstract public void keyReleased(KeyEvent e);
	abstract public void mouseDown(RN2Click c);
	abstract public void mouseReleased(RN2Click c);
	abstract public void mouseMoved(RN2Click c);
	abstract public void mouseDragged(RN2Click c);


}
