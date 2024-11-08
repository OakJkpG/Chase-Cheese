package Character;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.io.File;

public class Player implements Runnable {
    public int x, y, health = 500;
    private static final int JUMP_HEIGHT = 250, JUMP_STEPS = 250;
    private boolean isJumping = false;
    private JPanel page;

    public Player(int x, int y) { this.x = x; this.y = y; }

    public void jump(JPanel page) {
        if (!isJumping) {
            this.page = page;
            isJumping = true;
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < JUMP_STEPS * 2; i++) {
            y += (i < JUMP_STEPS) ? -JUMP_HEIGHT / JUMP_STEPS : JUMP_HEIGHT / JUMP_STEPS;
            page.repaint();
            try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        isJumping = false;
    }

    public BufferedImage getImage() { return loadImage("resources/img/mouse.png"); }
    public BufferedImage getImage2() { return loadImage("resources/img/mouse(damage).png"); }

    private BufferedImage loadImage(String path) {
        try { return ImageIO.read(new File(path)); } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
