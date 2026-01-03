package com.example.demo.voice;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Locale;

public class TextToSpeech {

    private static MaryInterface mary;

    static {
        try {
            System.out.println("🔊 Initializing MaryTTS...");

            mary = new LocalMaryInterface();

            // ✅ VERY IMPORTANT ORDER
            mary.setLocale(Locale.US);          // MUST come first
            mary.setVoice("cmu-slt-hsmm");      // Then voice

            System.out.println("✅ MaryTTS ready (English voice loaded)");
        } catch (Exception e) {
            System.err.println("❌ MaryTTS init failed");
            e.printStackTrace();
            mary = null;
        }
    }

    public static void speak(String text) {
        if (mary == null) {
            System.err.println("❌ MaryTTS is NULL, cannot speak");
            return;
        }

        try {
            System.out.println("🗣 Prime says: " + text);

            AudioInputStream audio = mary.generateAudio(text);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
