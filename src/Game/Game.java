package Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import Character.*;
import Element.Element;
import javax.sound.sampled.*;

public class Game extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1L;
    private int speed = 10;
    private static int pSize = 100, waveHeight = 80,base = 500, xStart = 1200;
    private long point = 0, lastPress = 0;
    private long startTime; // To track the game start time
    private Timer gameTimer; // Timer for updating the score duration

    private Player p1 = new Player(100, base - 80);
    static Display display;

    private Wave[] waveSet = makeWave(4);

    private Environment[] envSet = makeEnv(2, Environment.CLOUD);
    private Environment building = new Environment(xStart - 1200, base - 150, this, Environment.BUILDING, 4);
    private Environment building2 = new Environment(xStart + 780, base - 150, this, Environment.BUILDING, 4);

    private static Clip hitClip;
    private static Clip getClip;
    private static Clip jumpClip;
    private static Clip gameOverClip;

    public Game() {
        this.setBounds(getWidth(), getHeight(), 1280, 720);
        this.addKeyListener(this);
        this.setLayout(null);
        this.setFocusable(true);
        loadSounds();
        startTimer();
    }

    private void startTimer() {
        startTime = System.currentTimeMillis(); // Record the start time
        gameTimer = new Timer(1000, e -> repaint()); // Update every second
        gameTimer.start(); // Start the timer
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
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Calculate elapsed time in seconds
            g2.drawString("Time : " + elapsedTime + "s", 1100, 70); // Display elapsed time
            //--- mouse --
            g2.setColor(Color.RED);
            drawHealthBar(g2);
            g2.drawImage(p1.getImage(), p1.x, p1.y, pSize, pSize, null);
            //----HP----
            g2.setColor(Color.white);
            g2.drawString(p1.health / 5 + "%", 520, 40);
            //----Wave----
            for (Wave item : waveSet) {
                drawWave(item, g2);
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

    private Wave[] makeWave(int size) {
        Wave[] waveSet = new Wave[size];
        int x = 2000; // Starting position for the first wave

        for (int i = 0; i < size; i++) {
            waveSet[i] = new Wave(x, base, speed, this);
            x += 500;
        }
        return waveSet;
    }

    private boolean check(Player p1, int pSize, int waveX, int waveY, int waveHeight) {
        return p1.x + pSize > waveX && p1.x < waveX && p1.y + pSize >= waveY - waveHeight;
    }

    private void drawWave(Wave wave, Graphics2D g2) {
        g2.drawImage(wave.getImage(), wave.x, (wave.y - waveHeight), 100, waveHeight + 10, null);
        g2.drawImage(wave.getImage2(), wave.x2, (wave.y2 - waveHeight), 100, waveHeight + 10, null);
        
        if (check(p1, pSize, wave.x, wave.y, waveHeight)) {
            g2.drawImage(p1.getImage2(), p1.x, p1.y, pSize, pSize, null);
            playHitSound(); // Play collision sound
            if (p1.health >= 60) {
                p1.health -= 10;
            } else {
                restartGame();
            }
        }

        if (check(p1, pSize, wave.x2, wave.y2, waveHeight)) {
            playGetSound(); // Play getting sound
            wave.x2 = -100;
            this.point += 1;
            if (p1.health < 300) {
                p1.health += 10;
            }
        }
      
    }

    private Environment[] makeEnv(int size, int eType) {
        Environment[] envSet = new Environment[size];
        int far = 0;
        for (int i = 0; i < size; i++) {
            envSet[i] = new Environment(xStart + far, 20, this, eType, 10);
            far += 600;
        }
        return envSet;
    }

    public void restartGame() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        display.endGame(point, elapsedTime);
        removeKeyListener(this); 
        resetGameElements();  // Calls methods to reset player, waves, environment, etc.
        startTime = System.currentTimeMillis();  // Reset start time for the timer
        gameTimer.restart();  // Restart timer instead of creating a new one
        point = 0;
        lastPress = 0;

        display.stopSound();
        playGameOverSound();
        addKeyListener(this);  
        repaint();
    }
    
    private void resetGameElements() {
        p1 = new Player(100, base - 80);  
        waveSet = makeWave(4);  
        envSet = makeEnv(2, Environment.CLOUD);  
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (System.currentTimeMillis() - lastPress > 0) {
            if (e.getKeyCode() == 32 || e.getKeyCode() == 38) {
                p1.jump(this);
                lastPress = System.currentTimeMillis();
                playJumpSound();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] arg) {
        display = new Display();
    }

    private void loadSounds() {
        try {
            hitClip = AudioSystem.getClip();
            hitClip.open(AudioSystem.getAudioInputStream(new File("resources/sound/hit.wav")));
            
            getClip = AudioSystem.getClip();
            getClip.open(AudioSystem.getAudioInputStream(new File("resources/sound/get.wav")));

            jumpClip = AudioSystem.getClip();
            jumpClip.open(AudioSystem.getAudioInputStream(new File("resources/sound/jump.wav")));

            gameOverClip = AudioSystem.getClip();
            gameOverClip.open(AudioSystem.getAudioInputStream(new File("resources/sound/gameover.wav")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playHitSound() {
        if (hitClip.isRunning()) {
            hitClip.stop(); // Stop the current sound if it's still playing
        }
        hitClip.setFramePosition(0); // Rewind to the beginning
        hitClip.start(); // Play the sound
    }
    
    public void playGetSound() {
        if (getClip.isRunning()) {
            getClip.stop(); // Stop the current sound if it's still playing
        }
        getClip.setFramePosition(0); // Rewind to the beginning
        getClip.start(); // Play the sound
    }
    
    public void playJumpSound() {
        if (jumpClip.isRunning()) {
            jumpClip.stop(); // Stop the current sound if it's still playing
        }
        jumpClip.setFramePosition(0); // Rewind to the beginning
        jumpClip.start(); // Play the sound
    }

    public void playGameOverSound() {
        if (gameOverClip.isRunning()) {
            gameOverClip.stop(); // Stop the current sound if it's still playing
        }
        gameOverClip.setFramePosition(0); // Rewind to the beginning
        gameOverClip.start(); // Play the sound
    }

}