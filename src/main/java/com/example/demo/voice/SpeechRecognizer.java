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

    private boolean awake = false;
    private String lastText = "";

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
              "open calculator",
              "open notepad",
              "open camera",
              "what time is it"
            ]
            """;

            Recognizer recognizer = new Recognizer(model, 16000, grammar);

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            TargetDataLine mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            System.out.println("🛌 Prime sleeping…");

            byte[] buffer = new byte[4096];

            while (true) {

                int bytesRead = mic.read(buffer, 0, buffer.length);
                if (!recognizer.acceptWaveForm(buffer, bytesRead)) continue;

                String json = recognizer.getResult();
                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                String text = obj.get("text").getAsString().trim();

                if (text.isEmpty()) continue;
                if (text.equals(lastText)) continue;
                lastText = text;

                System.out.println("🗣 Heard: " + text);

                // 💤 WAKE WORD ONLY
                if (!awake && isWakeWord(text)) {
                    awake = true;
                    VoiceFeedback.beep();
                    VoiceFeedback.speak("Yes boss");
                    continue;
                }

                // ⛔ Ignore everything until woken
                if (!awake) continue;

                // 🎯 EXECUTE ONE COMMAND ONLY
                CommandResponse response = CommandProcessor.process(text);

                if (!"unknown".equals(response.getCommand())) {
                    VoiceFeedback.speak(response.getMessage());
                    CommandExecutor.execute(response);
                } else {
                    VoiceFeedback.speak("Sorry boss, I didn't understand");
                }


                // 😴 GO BACK TO SLEEP
                awake = false;
                System.out.println("🛌 Prime sleeping…");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWakeWord(String text) {
        return text.equals("prime")
                || text.equals("hello prime")
                || text.equals("hey prime")
                || text.equals("hi prime");
    }
}
