package Character;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Wave implements Runnable {
    public int speed, x, y, x2, y2;
    private JPanel page;
    private boolean running = true;
    private static final int MIN_DISTANCE = 10;
    private Thread waveThread;

    public Wave(int x, int y, int speed, JPanel page) {
        this.x = x;
        this.y = y;
        this.x2 = x + 300;
        this.y2 = y;
        this.speed = speed;
        this.page = page;
        start(); // Start the movement thread
    }

    public void start() {
        if (waveThread == null || !waveThread.isAlive()) {
            waveThread = new Thread(this);
            waveThread.start();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            move();
            try { Thread.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
    
    private int temp_x = 0;
    
    
    private void move() {
        // Reset wave positions when they move off-screen
        if (x <= -50) {
            x = 3000 + (int)Math.random()*1000;
        }

        if (x2 <= -50) { 
            x2 = 2000 + (int)Math.random()*1000;
        }
        
        if (Math.abs(x - x2) < 100){
            y2 = y - 100;
        }

        // Move both wave objects
        x -= speed;
        x2 -= speed;

        page.repaint();
    }



    public BufferedImage getImage() {
        return loadImage("resources/img/cat.png");
    }

    public BufferedImage getImage2() {
        return loadImage("resources/img/cheese.png");
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
