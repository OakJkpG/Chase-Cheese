package Element;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private static Clip hitClip;
    private static Clip getClip;
    private static Clip jumpClip;
    private static Clip gameOverClip;

    public Sound() {
        loadSounds();
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
        playSound(hitClip);
    }

    public void playGetSound() {
        playSound(getClip);
    }

    public void playJumpSound() {
        playSound(jumpClip);
    }

    public void playGameOverSound() {
        playSound(gameOverClip);
    }

    private void playSound(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
}
