package lib;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class RN2GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	
	private RN2Scene scene;
	private RN2NodeRenderer renderer;
	private BufferedImage backBuffer;
//	private Insets insets;
	
	public RN2GamePanel() {
		super();
	}
	
	public void initialize() throws IllegalStateException {
		JFrame myFrame;
		try {
			myFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		} catch(ClassCastException e) {
			throw new IllegalStateException("The RN2GamePanel object must to be "
					+ "added to a JFrame object. The code died cuz the parent "
					+ "couldn't be cast to JFrame :(");
		}
		if(myFrame==null) {
			throw new IllegalStateException("The RN2GamePanel object needs to be "
					+ "added to a JFrame before initialize() is called.");
		}
		myFrame.setResizable(false);
		renderer = new RN2NodeRenderer();
		backBuffer = new BufferedImage(myFrame.getWidth(), myFrame.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		myFrame.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void presentScene(RN2Scene s) {
		scene = s;
		s.initialize();
//		System.out.println("GP initialized called");

		renderer.setScene(s);
	}
	
	public void update(double deltaTime) {
		scene.update(deltaTime);
		scene.runAllActionsForSelfAndChildren(deltaTime);
		
		//Input stuff
		for(KeyEvent e: keyPressedEvents) {
			scene.keyDown(e);
		} keyPressedEvents.clear();
		for(KeyEvent e: keyReleasedEvents) {
			scene.keyReleased(e);
		} keyReleasedEvents.clear();
		for(RN2Click c: mousePressedEvents) {
			scene.mouseDown(c);
		} mousePressedEvents.clear();
		for(RN2Click c: mouseReleasedEvents) {
			scene.mouseReleased(c);
		} mouseReleasedEvents.clear();
		for(RN2Click c: mouseMovedEvents) {
			scene.mouseMoved(c);
		} mouseMovedEvents.clear();
		for(RN2Click c: mouseDraggedEvents) {
			scene.mouseDragged(c);
		} mouseDraggedEvents.clear();
	}
	
	@Override
	public void paint(Graphics g) {
		if(scene==null) return;
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
		
		renderer.renderAllNodes(backBufferG2);

		
		// Finally we draw the buffer image onto the main Graphics object
		g.drawImage(backBuffer, 0 , 0, this);
	}

	
	/*
	 * (non-Javadoc)
	 * In order to avoid any multi-threading in scene objects, I don't call
	 * scene input events directly from the listener methods. Instead, I append
	 * the events into an ArrayList and invoke the scene input events during the
	 * game panel's update method.
	 */
	
	public ArrayList<Integer> keysDown = new ArrayList<Integer>();
	
	private CopyOnWriteArrayList<KeyEvent> keyPressedEvents = new CopyOnWriteArrayList<KeyEvent>();
	@Override
	public void keyPressed(KeyEvent e) {
		if(keysDown.contains(e.getKeyCode())) {
			return;
		} else {
			keysDown.add(e.getKeyCode());
			keyPressedEvents.add(e);
		}
	}

	private CopyOnWriteArrayList<KeyEvent> keyReleasedEvents = new CopyOnWriteArrayList<KeyEvent>();
	@Override
	public void keyReleased(KeyEvent e) {
		if(keysDown.contains(e.getKeyCode())) {
			keysDown.remove(new Integer(e.getKeyCode()));
			keyReleasedEvents.add(e);
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {} //BLAH

	@Override
	public void mouseClicked(MouseEvent e) {} //BLAH

	CopyOnWriteArrayList<RN2Click> mousePressedEvents = new CopyOnWriteArrayList<RN2Click>();
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressedEvents.add(new RN2Click(e.getX(), e.getY()));
	}

	CopyOnWriteArrayList<RN2Click> mouseReleasedEvents = new CopyOnWriteArrayList<RN2Click>();
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseReleasedEvents.add(new RN2Click(e.getX(), e.getY()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {} //BLAH

	@Override
	public void mouseExited(MouseEvent e) {} //BLAH

	CopyOnWriteArrayList<RN2Click> mouseDraggedEvents = new CopyOnWriteArrayList<RN2Click>();
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDraggedEvents.add(new RN2Click(e.getX(), e.getY()));

	}

	CopyOnWriteArrayList<RN2Click> mouseMovedEvents = new CopyOnWriteArrayList<RN2Click>();
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMovedEvents.add(new RN2Click(e.getX(), e.getY()));
	}
	
}
