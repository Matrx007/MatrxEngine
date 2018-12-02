package com.engine.libs.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sound {
    String name;
    float volume;

    public CopyOnWriteArrayList<Clip> clips;

    public Sound(String name, float volume) {
        this.name = name;
        this.volume = volume;
        clips = new CopyOnWriteArrayList<>();
    }

    public void play() {/*
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(name)));
            clip.setFramePosition(0);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // Reduce volume by 10 decibels.
            clips.add(clip);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        new Thread(() -> {
            Clip clip;
            try {
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(name)));
                clip.setFramePosition(0);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}