package gameCode;

import java.awt.Color;

import javax.swing.JFrame;

import lib.GamePanel;
import lib.RN2Scene;

public class Driver {
	static final double framePause = 1.0/60.0;
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Renegade 2D!");
        frame.setSize(800, 600);
        GamePanel gameView = new GamePanel();
        frame.add(gameView);
        gameView.initialize();
        frame.addKeyListener(gameView);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Can set properties here
        RN2Scene theScene = new GameScene((double)gameView.getWidth(), 
        		(double)gameView.getHeight());
        theScene.backgroundColor = Color.WHITE;
        gameView.setScene(theScene);
        
        while(true) {
        	gameView.repaint();
        	gameView.update(framePause);
            Thread.sleep((long)(framePause*1000));
        }

	}
	

}
