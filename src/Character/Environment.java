package Character;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;

public class Environment implements Runnable {
    public int x, y, speed, eType;
    private JPanel page;

    public static final int CLOUD = 0, BUILDING = 1, NIGHT = 2;

    public Environment(int x, int y, JPanel page, int eType, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.eType = eType;
        this.page = page;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            move();
            page.repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void move() {
        x = (x < -2000) ? 1970 : x - speed;
    }

    public BufferedImage getImage() {
        String[] imageNames = {"cloud.png", "town.jpg", "night.jpg"};
        try { return ImageIO.read(new File("resources/img/" + imageNames[eType])); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
