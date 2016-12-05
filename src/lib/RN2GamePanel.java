package lib;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class RN2GamePanel extends JPanel implements KeyListener {
	
	private RN2Scene scene;
	private RN2NodeRenderer renderer;
	private BufferedImage backBuffer;
//	private Insets insets;
	
	public RN2GamePanel() {
		super();
	}
	
	public void initialize() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.setResizable(false);
		renderer = new RN2NodeRenderer();
		backBuffer = new BufferedImage(topFrame.getWidth(), topFrame.getHeight(), BufferedImage.TYPE_INT_RGB);
	}
	
	public void setScene(RN2Scene s) {
		scene = s;
		s.initialize();
		renderer.setScene(s);
	}
	
	public void update(double deltaTime) {
		scene.update(deltaTime);
	}
	
	
	@Override
	public void paint(Graphics g) {
		// To maximize speed, all nodes will be rendered on a buffer image first,
		// and finally, after all the nodes have been rendered on that one image,
		// that buffer image will be the one thing rendered onto the actual Graphics g
		
		// First, we flip the transform of the backBuffer graphics so we get an origin
		// in the lower left corner and y-up = positive
		Graphics2D backBufferG2 = (Graphics2D)backBuffer.getGraphics();
		backBufferG2.translate(0, scene.height);
		backBufferG2.scale(1, -1);
		
		// Clear the buffer image before drawing stuff onto it
		backBufferG2.setColor(scene.backgroundColor);
		backBufferG2.fillRect(0, 0, getWidth(), getHeight());
		
		// Now we render the entire scene!
		renderer.scheduleNodeForRendering(scene); // Render scene + all it's children!
		renderer.scheduleNodeForRendering(scene.camera); //Need to specify camera individually 
													//because camera isn't in the scene's children array
		renderer.renderAllNodes(backBufferG2);
		
		// Finally we draw the buffer image onto the main Graphics object
		g.drawImage(backBuffer, 0 , 0, this);
	}

	
	public ArrayList<Integer> keysDown = new ArrayList<Integer>();
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(keysDown.contains(e.getKeyCode())) {
			return;
		} else {
			keysDown.add(e.getKeyCode());
			scene.keyDown(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keysDown.contains(e.getKeyCode())) {
			scene.keyReleased(e);
			keysDown.remove(new Integer(e.getKeyCode())); 
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
