package com.example.demo.voice;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class TextToSpeech {

    private static MaryInterface mary;

    private static void init() {
        if (mary != null) return;
        try {
            mary = new LocalMaryInterface();
            mary.setVoice("cmu-slt-hsmm");
        } catch (Exception e) {
            e.printStackTrace();
            mary = null;
        }
    }

    public static void speak(String text) {
        init();
        if (mary == null) return;

        try {
            AudioInputStream audio = mary.generateAudio(text);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
