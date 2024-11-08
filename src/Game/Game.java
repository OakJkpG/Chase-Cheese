package Game;

import Element.*;
import Character.*;
import Event.Event;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel implements KeyListener {

    private int speed = 10;
    static int pSize = 100, waveHeight = 80, base = 500, xStart = 1200;
    public long point = 0;
    private long lastPress = 0;
    private long startTime;
    private Timer gameTimer;
    static Display display;

    private Player p1 = new Player(100, base - 80);

    private Wave[] waveSet;
    
    private Environment[] envSet;
    private final Environment building;
    private final Environment building2;

    private final Sound sound = new Sound();
    private final Event eventHandler;
    
    private long lastMessageTime = 10;
    private boolean act = false;

    public Game() {
        this.setBounds(getWidth(), getHeight(), 1280, 720);
        this.addKeyListener(this);
        this.setLayout(null);
        this.setFocusable(true);
        eventHandler = new Event(this);
        waveSet = eventHandler.makeWave(4);
        envSet = makeEnv(6, Environment.CLOUD);
        building = new Environment(xStart - 1200, base - 150, this, Environment.BUILDING, 2);
        building2 = new Environment(xStart + 780, base - 150, this, Environment.NIGHT, 2);
        startTimer();
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, e -> {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsedTime >= lastMessageTime) {
                
                act = true;
            }
            repaint();
        });
        gameTimer.start();
    }
    public int getSpeed() {
        return speed;
    }

    public int getBase() {
        return base;
    }

    public int getPSize() {
        return pSize;
    }

    public int getWaveHeight() {
        return waveHeight;
    }

    public Player getPlayer() {
        return p1;
    }

    public long getPoint() {
        return point;
    }

    public Sound getSound() {
        return sound;
    }

    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            this.drawBackground(g2);
            //---POINT----
            g2.setFont(Element.getFont(30));
            g2.setColor(Color.white);
            g2.drawString("Point : " + point, 1100, 40);
            //---TIMER----
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            g2.drawString("Time : " + elapsedTime + "s", 1100, 70);
            //--- mouse --
            g2.setColor(Color.RED);
            drawHealthBar(g2);
            g2.drawImage(p1.getImage(), p1.x, p1.y, pSize, pSize, null);
            //----HP----
            g2.setColor(Color.white);
            g2.drawString(p1.health / 5 + "%", 520, 40);
            //----Wave----
            for (Wave item : waveSet) {
                eventHandler.drawWave(item, g2);
            }
            //----Message----
            if (act) {
                g2.setColor(Color.YELLOW);
                g2.drawString("The game speed has changed!", 400, 200);
                if(elapsedTime - lastMessageTime == 3){
                    act = false;
                    lastMessageTime += 10;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawBackground(Graphics2D g2) throws IOException {
        g2.drawImage(building.getImage(), building.x, building.y - 350, 2000, 600, null);
        g2.drawImage(building2.getImage(), building2.x, building2.y - 350, 2000, 600, null);
        g2.drawImage(ImageIO.read(new File("resources/img/floor.jpg")), 0, base + 10, 2000, 220, null);
        for (Environment item : envSet) {
            g2.drawImage(item.getImage(), item.x, item.y, 500, 160, null);
        }
    }

    private void drawHealthBar(Graphics2D g2) {
        try {
            g2.drawImage(ImageIO.read(new File("resources/img/heart.png")), 10, 15, 30, 30, null);
            g2.setStroke(new BasicStroke(18.0f));
            g2.setColor(new Color(241, 98, 69));
            g2.drawLine(60, 30, p1.health, 30);
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(6.0f));
            g2.drawRect(50, 20, 460, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Environment[] makeEnv(int size, int eType) {
        Environment[] envSet = new Environment[size];
        int far = 0;
        for (int i = 0; i < size; i++) {
            envSet[i] = new Environment(xStart + far, 20, this, eType, 2);
            far += 600;
        }
        return envSet;
    }

    public void restartGame() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        display.endGame(point, elapsedTime);
        removeKeyListener(this); 
        resetGameElements();
        startTime = System.currentTimeMillis();
        gameTimer.restart();
        point = 0;
        lastPress = 0;
        display.stopSound();
        sound.playGameOverSound();
        addKeyListener(this);  
        repaint();
    }
    
    private void resetGameElements() {
        p1 = new Player(100, base - 80);
        waveSet = eventHandler.makeWave(4);
        envSet = makeEnv(2, Environment.CLOUD);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (System.currentTimeMillis() - lastPress > 100) {
            if (e.getKeyCode() == 32 || e.getKeyCode() == 38) {
                p1.jump(this);
                lastPress = System.currentTimeMillis();
                sound.playJumpSound();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] arg) {
        display = new Display();
    }
}