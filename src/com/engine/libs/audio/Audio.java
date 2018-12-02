package com.engine.libs.audio;

import com.engine.libs.exceptions.PrintError;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Audio {
    private Clip clip;

    public Audio(final String file) {
        PrintError.printError("Message: Class Audio is currently not supported but will be updated soon");
        // The wrapper thread is unnecessary, unless it blocks on the
        // Clip finishing; see comments.
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Audio.class.getResourceAsStream(file));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public boolean isRunning() {
        return clip.isRunning();
    }
}
