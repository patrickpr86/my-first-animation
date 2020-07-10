package graficos;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	

	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int width = 240;
	private final int height = 160;
	private final int scale = 3;
	
	private BufferedImage image;
	
	private Spritesheet sheet;
	
	private BufferedImage[] player;
	private int frames = 0;
	private int maxFrames = 10;
	private int curAnimation = 0, maxAnimation = 3;
	
	
	public Game() {
		sheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[4] ;
		player[0] = sheet.getSprite(0, 0, 16, 16);
		player[1] = sheet.getSprite(16,0,16,16);
		player[2] = sheet.getSprite(32,0,16,16);
		player[3] = sheet.getSprite(48,0,16,16);


		setPreferredSize(new Dimension(width*scale, height*scale));
		initFrame();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
	}
	
	
	
	
	public void initFrame() {
		
		frame = new JFrame("Game1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void update() {
		frames++;
		if(frames > maxFrames) {
			frames =0;
			curAnimation++;
			if(curAnimation > maxAnimation) {
				curAnimation = 0;
			}
		}
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(19,19,19));
		graphics.fillRect(0, 0, width, height); // renderizar retangulo por exemplo 
		
		/*Renderização do jogo */
		
		Graphics2D g2 = (Graphics2D) graphics;
		
		//g2.rotate(Math.toRadians(45),90+8,90+8);
		g2.drawImage(player[curAnimation],90,90,null);
		

		
		/**/
		graphics.dispose();
		graphics.setFont(new Font("Arial", Font.BOLD, 20)); // renderizar fontes
		graphics.setColor(Color.white);
		graphics.drawString("olá", 19, 19);
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, width*scale, height*scale, null);
		bs.show();
		
	}

	@Override
	public void run() { // gamelooping feito profissionalmente
		long lastTime = System.nanoTime();
		double amountOfUpdates = 60.0;
		double ns = 1000000000 / amountOfUpdates;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now  = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
				
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames =0;
				timer += 1000;
			}
			
		}
		
		stop();
	}

}
