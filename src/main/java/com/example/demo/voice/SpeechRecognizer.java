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
    private long lastCommandTime = 0;
    private static final long COOLDOWN_MS = 1500;

    public void startListening() {

        try {
            Model model = new Model(new File(MODEL_PATH).getAbsolutePath());

            // 🔹 Wake word grammar (loose)
            String wakeGrammar = """
            [
              "prime",
              "hello prime",
              "hey prime",
              "hi prime"
            ]
            """;

            // 🔹 Command grammar (strict)
            String commandGrammar = """
            [
              "open chrome",
              "open calculator",
              "open notepad",
              "open camera",
              "open spotify",
              "open whatsapp",
              "open vscode",
              "open telegram",
              "open settings"
            ]
            """;

            Recognizer wakeRecognizer = new Recognizer(model, 16000, wakeGrammar);
            Recognizer commandRecognizer = new Recognizer(model, 16000, commandGrammar);

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            TargetDataLine mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            System.out.println("🛌 Prime sleeping…");

            byte[] buffer = new byte[4096];

            while (true) {

                int bytesRead = mic.read(buffer, 0, buffer.length);
                if (bytesRead <= 0) continue;

                // 💤 WAKE MODE
                if (!awake) {
                    if (wakeRecognizer.acceptWaveForm(buffer, bytesRead)) {
                        String text = extractText(wakeRecognizer.getResult());
                        if (isWakeWord(text)) {
                            awake = true;
                            VoiceFeedback.beep();
                            VoiceFeedback.speak("Yes boss");
                            System.out.println("👂 Prime awake");
                        }
                    }
                    continue;
                }

                // ⏱ Cooldown
                if (System.currentTimeMillis() - lastCommandTime < COOLDOWN_MS)
                    continue;

                // 🎯 COMMAND MODE
                if (commandRecognizer.acceptWaveForm(buffer, bytesRead)) {

                    String text = extractText(commandRecognizer.getResult());
                    if (text.isEmpty()) continue;

                    System.out.println("🗣 Command: " + text);

                    CommandResponse response = CommandProcessor.process(text);

                    if ("unknown".equals(response.getCommand())) {
                        VoiceFeedback.speak("Sorry boss, I didn't understand");
                    } else {
                        VoiceFeedback.speak(response.getMessage());
                        CommandExecutor.execute(response);
                    }

                    lastCommandTime = System.currentTimeMillis();
                    awake = false;
                    System.out.println("🛌 Prime sleeping…");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔧 Extract text safely from JSON
    private String extractText(String json) {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            return obj.get("text").getAsString().trim().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isWakeWord(String text) {
        return text.equals("prime")
                || text.equals("hello prime")
                || text.equals("hey prime")
                || text.equals("hi prime");
    }
}
