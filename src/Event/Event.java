package Event;

import Game.Game;
import Character.Player;
import Character.Wave;
import java.awt.Graphics2D;

public class Event {
    private Game game;

    public Event(Game game) {
        this.game = game;
    }

    public Wave[] makeWave(int size) {
        Wave[] waveSet = new Wave[size];
        int x = 2000;

        for (int i = 0; i < size; i++) {
            waveSet[i] = new Wave(x, game.getBase(), game.getSpeed(), game);
            x += 500;
        }
        return waveSet;
    }

    public boolean check(Player p1, int pSize, int waveX, int waveY, int waveHeight) {
        return p1.x + pSize > waveX && p1.x < waveX && p1.y + pSize >= waveY - waveHeight;
    }

    public void drawWave(Wave wave, Graphics2D g2) {
        g2.drawImage(wave.getImage(), wave.x, (wave.y - game.getWaveHeight()), 100, game.getWaveHeight() + 10, null);
        g2.drawImage(wave.getImage2(), wave.x2, (wave.y2 - game.getWaveHeight()), 100, game.getWaveHeight() + 10, null);

        if (check(game.getPlayer(), game.getPSize(), wave.x, wave.y, game.getWaveHeight())) {
            g2.drawImage(game.getPlayer().getImage2(), game.getPlayer().x, game.getPlayer().y, game.getPSize(), game.getPSize(), null);
            game.getSound().playHitSound();
            if (game.getPlayer().health >= 60) {
                game.getPlayer().health -= 10;
            } else {
                game.restartGame();
            }
        }

        if (check(game.getPlayer(), game.getPSize(), wave.x2, wave.y2, game.getWaveHeight())) {
            game.getSound().playGetSound();
            wave.x2 = -100;
            game.point += 1;
            if (game.getPlayer().health < 300) {
                game.getPlayer().health += 10;
            }
        }
    }
}