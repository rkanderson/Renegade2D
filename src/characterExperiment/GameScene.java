package characterExperiment;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.*;

public class GameScene extends RN2Scene {

	Character player;
	public GameScene(RN2GamePanel gamePanel, double width, double height) {
		super(gamePanel, width, height);
	}

	@Override
	public void initialize() {
		player = new Character();
		addChild(player);
		RN2Action seq = RN2Action.sequence(new RN2Action[] {
				RN2Action.moveByVectorOverDuration(new RN2Vector(-20, -20), 0.5),
				RN2Action.moveByVectorOverDuration(new RN2Vector(20, 20), 0.5),
				RN2Action.rotateByValueOverDuration(Math.PI/4, 0.5),
				RN2Action.addScaleValueOverDuration(1, 1, 0.5)
				});
		
		player.runActionWithCompletionBlock(seq, new RN2Action.CompletionBlock() {
			
			@Override
			public void run() {
				System.out.println("Action complete!");
			}
		});
	}

	@Override
	public void update(double deltaTime) {
		ArrayList<Integer> keysDown = gamePanel.get().keysDown;
		

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
		// TODO Auto-generated method stub
		
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
