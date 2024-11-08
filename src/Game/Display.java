package Game;

import Element.Button;
import Element.Label;
import Element.Resource;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Display extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static Clip clip;

    public Display() {
        setupFrame();
        setIconImage(loadImage("resources/img/cheese.png"));
        playSound("resources/sound/main.wav", true);
        switchToPanel(new StartGame(this));
    }

    private void setupFrame() {
        setTitle("Chase & Cheese");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void switchToPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
        panel.requestFocusInWindow();
    }

    private Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        stopSound();
        switch (command) {
            case "Start", "Restart" -> {
                playSound("resources/sound/run.wav", true);
                switchToPanel(new Game());
            }
            case "Main Menu" -> {
                playSound("resources/sound/main.wav", true);
                switchToPanel(new StartGame(this));
            }
        }
    }

    public void endGame(long score, long time) {
        switchToPanel(new EndGame(score, time, this));
    }

    private void playSound(String soundPath, boolean loop) {
        try {
            if (clip != null && clip.isRunning()) clip.stop();
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(soundPath)));
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSound() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    // StartGame panel class
    static class StartGame extends JPanel {
        private final BufferedImage imgBg = Resource.getImage("resources/img/main.jpg");

        public StartGame(ActionListener main) {
            setLayout(null);
            setBounds(0, 0, 1280, 720);
            add(createButton("Start", main, 520, 350));
            add(createLabel("Chase & Cheese", Color.YELLOW, 80, 320, 190));
            add(createLabel("Chase & Cheese", Color.BLACK, 80, 329, 190));
        }

        private Button createButton(String text, ActionListener listener, int x, int y) {
            Button button = new Button(text, 30, x, y, 200, 50);
            button.addActionListener(listener);
            return button;
        }

        private Label createLabel(String text, Color color, int fontSize, int x, int y) {
            Label label = new Label(text, fontSize, x, y, 1000, 100);
            label.setForeground(color);
            return label;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgBg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // EndGame panel class
    static class EndGame extends JPanel {
        private final BufferedImage imgBg = Resource.getImage("resources/img/main.jpg");

        public EndGame(long score, long time, ActionListener main) {
            setLayout(null);
            setBounds(0, 0, 1280, 720);
            add(createLabel("Game Over!", Color.WHITE, 40, 515, 180));
            add(createLabel("Score: " + score, Color.WHITE, 30, 570, 240));
            add(createLabel("Time: " + time, Color.WHITE, 30, 570, 300));
            add(createButton("Restart", main, 540, 380));
            add(createButton("Main Menu", main, 540, 440));
        }

        private Label createLabel(String text, Color color, int fontSize, int x, int y) {
            Label label = new Label(text, fontSize, x, y, 300, 100);
            label.setForeground(color);
            return label;
        }

        private Button createButton(String text, ActionListener listener, int x, int y) {
            Button button = new Button(text, 15, x, y, 200, 50);
            button.addActionListener(listener);
            return button;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgBg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
