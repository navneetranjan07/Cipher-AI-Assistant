package com.example.demo.voice;

import com.example.demo.command.CommandExecutor;
import com.example.demo.command.CommandProcessor;
import com.example.demo.command.CommandResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.File;

public class SpeechRecognizer {

    private static final String MODEL_PATH =
            "src/main/resources/models/vosk-model-small-en-us-0.15";

    private static final String WAKE_WORD = "prime";

    private boolean active = false;
    private long lastActionTime = 0;
    private static final long COOLDOWN = 2500;

    public void startListening() {

        try {
            Model model = new Model(new File(MODEL_PATH).getAbsolutePath());

            String grammar = """
            [
              "prime",
              "hello prime",
              "hey prime",
              "hi prime",
              "open chrome",
              "open notepad",
              "open calculator",
              "open camera",
              "what time is it"
            ]
            """;

            Recognizer recognizer = new Recognizer(model, 16000, grammar);

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            TargetDataLine mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            System.out.println("🛌 Sleeping… say 'hello prime'");

            byte[] buffer = new byte[4096];

            while (true) {

                int bytesRead = mic.read(buffer, 0, buffer.length);
                if (!recognizer.acceptWaveForm(buffer, bytesRead)) continue;

                String json = recognizer.getResult();
                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                String text = obj.get("text").getAsString().trim();

                if (text.isEmpty()) continue;

                long now = System.currentTimeMillis();
                if (now - lastActionTime < COOLDOWN) continue;

                System.out.println("🗣 Heard: " + text);

                // 🔔 WAKE WORD (STRICT)
                if (!active && (
                        text.equals("prime") ||
                                text.equals("hello prime") ||
                                text.equals("hey prime") ||
                                text.equals("hi prime")
                )) {
                    active = true;
                    lastActionTime = now;

                    BeepPlayer.wake();        // 🔔 Wake beep
                    TextToSpeech.speak("Yes boss");

                    System.out.println("🟢 Prime activated");
                    continue;
                }

                // 🚫 Ignore commands until wake
                if (!active) continue;

                // 🎯 PROCESS COMMAND
                CommandResponse response = CommandProcessor.process(text);

                if (response.getCommand() == null) {
                    BeepPlayer.error();       // ❌ error beep
                    TextToSpeech.speak(response.getSpeech());
                } else {
                    BeepPlayer.success();    // ✅ success beep
                    CommandExecutor.execute(response);
                }

                // 😴 Sleep again
                active = false;
                lastActionTime = now;
                BeepPlayer.sleepBeep();
                System.out.println("🛌 Prime sleeping…");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
