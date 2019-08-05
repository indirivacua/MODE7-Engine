package window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MODE7Engine extends JFrame { // The class itself is a JFrame
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Mode {
		MOVE, SET;
	}

	private Mode mode = Mode.SET;
	private final int sHeight = 240;
	private final int sWidth = 320;
	private double x, y, z;
	private float depth, scale, x_offset, y_offset, angle, speed;
	boolean up = false, down = false, left = false, right = false;
	private BufferedImage bg;

	public MODE7Engine() {
		depth = sHeight;
		scale = Resources.background.getHeight();
		x_offset = 0.0f;
		y_offset = 0.0f;
		angle = 0.0f;
		speed = 1.0f;

		setTitle("MODE-7");
		setSize(sWidth, sHeight);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Sets the location of the JFrame relative to center of display screen
		setFocusable(true); // Enable mouse and keyboard events
		setFocusTraversalKeysEnabled(false); // Allow us to use keys like TAB as key event
		addKeyListener(new KeybordEventHandler());
		setVisible(true);
	}

	public void paint(Graphics g) {
		bg = new BufferedImage(sWidth, sHeight, BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < sHeight; j++) {
			for (int i = 0; i < sWidth; i++) {
				z = depth + j;

				y = ((sWidth / 2 - i) * Math.sin(Math.toRadians(angle)) + j * Math.cos(Math.toRadians(angle))) / z;
				y *= scale;
				y += y_offset;
				y %= Resources.background.getHeight();

				x = ((sWidth / 2 - i) * Math.cos(Math.toRadians(angle)) - j * Math.sin(Math.toRadians(angle))) / z;
				x *= scale;
				x += x_offset;
				x %= Resources.background.getWidth();
				bg.setRGB(i, j, Resources.background.getRGB((int) Math.abs(x), (int) Math.abs(y)));
			}
		}

		Graphics bg_g = bg.getGraphics();
		
		// Sky
		bg_g.setColor(Color.CYAN);
		bg_g.fillRect(0, 0, sWidth, sHeight / 2);

		// Debug Text
		bg_g.setColor(Color.BLACK);
		bg_g.drawString(mode.toString(), 2, 36);

		// Movement
		Controller();

		g.drawImage(bg, 0, 0, null);
	}

	private void Controller() {
		if (up) {
			x_offset += -(float) (speed * Math.sin(Math.toRadians(angle)));
			y_offset += (float) (speed * Math.cos(Math.toRadians(angle)));
		}
		if (down) {
			x_offset -= -(float) (speed * Math.sin(Math.toRadians(angle)));
			y_offset -= (float) (speed * Math.cos(Math.toRadians(angle)));
		}
		if (left) {
			angle -= speed * 4;
		}
		if (right) {
			angle += speed * 4;
		}
	}

	public class KeybordEventHandler extends KeyAdapter {
		public void keyPressed(KeyEvent evt) {
			int ckey = evt.getKeyCode();
			if (ckey == KeyEvent.VK_TAB)
				if (mode == Mode.SET)
					mode = Mode.MOVE;
				else
					mode = Mode.SET;
			switch (mode) {
			case MOVE:
				if (ckey == KeyEvent.VK_UP) {
					up = true;
				}
				if (ckey == KeyEvent.VK_DOWN) {
					down = true;
				}
				if (ckey == KeyEvent.VK_LEFT) {
					left = true;
				}
				if (ckey == KeyEvent.VK_RIGHT) {
					right = true;
				}
				if (ckey == KeyEvent.VK_ADD) { // + (Numpad)
					speed++;
				}
				if (ckey == KeyEvent.VK_SUBTRACT) { // - (Numpad)
					speed--;
				}
				break;
			case SET:
				if (ckey == KeyEvent.VK_UP) {
					depth += 10;
				}
				if (ckey == KeyEvent.VK_DOWN) {
					depth -= 10;
				}
				if (ckey == KeyEvent.VK_ADD) { // + (Numpad)
					scale += 10;
				}
				if (ckey == KeyEvent.VK_SUBTRACT) { // - (Numpad)
					scale -= 10;
				}
				if (ckey == KeyEvent.VK_M) { // Show correct 'M'ap
					depth = -sHeight / 2.0f;
					scale = Resources.background.getHeight() / 16.0f;
					angle = 0.0f;
					x_offset = 0.0f;
					y_offset = 0.0f;
				}
				if (ckey == KeyEvent.VK_R) { // 'R'eset to origin
					depth = sHeight;
					scale = Resources.background.getHeight();
					angle = 0.0f;
					x_offset = 0.0f;
					y_offset = 0.0f;
					speed = 1.0f;
				}
				break;
			default:
				break;
			}
			if (ckey == KeyEvent.VK_P) // 'P'rint status
				System.out.println(
						"mode: " + mode.toString() + " | depth: " + depth + " | scale: " + scale + " | angle: " + angle
								+ " | speed: " + speed + " | x_offset: " + x_offset + " | y_offset: " + y_offset);
		}

		public void keyReleased(KeyEvent evt) {
			int ckey = evt.getKeyCode();
			if (ckey == KeyEvent.VK_UP) {
				up = false;
			}
			if (ckey == KeyEvent.VK_DOWN) {
				down = false;
			}
			if (ckey == KeyEvent.VK_LEFT) {
				left = false;
			}
			if (ckey == KeyEvent.VK_RIGHT) {
				right = false;
			}
		}
	}
}
