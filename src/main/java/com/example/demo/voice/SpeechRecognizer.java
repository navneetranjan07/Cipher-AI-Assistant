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

    private static final float SAMPLE_RATE = 16000f;
    private static final String WAKE_WORD = "prime";

    // Strict command grammar (Phase 1)
    private static final String COMMAND_GRAMMAR = """
    [
      "open chrome",
      "open notepad",
      "open calculator",
      "open camera",
      "open spotify",
      "open whatsapp",
      "open vscode",
      "open telegram",
      "open settings"
    ]
    """;

    private boolean awake = false;

    public void startListening() {
        try {
            Model model = new Model(new File(MODEL_PATH).getAbsolutePath());

            // Wake grammar ONLY
            String wakeGrammar = """
            [
              "prime",
              "hey prime",
              "hello prime"
            ]
            """;

            Recognizer wakeRec = new Recognizer(model, SAMPLE_RATE, wakeGrammar);
            Recognizer cmdRec  = new Recognizer(model, SAMPLE_RATE, COMMAND_GRAMMAR);

            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            TargetDataLine mic = AudioSystem.getTargetDataLine(format);
            mic.open(format);
            mic.start();

            System.out.println("🛌 Prime sleeping… (say 'prime')");

            byte[] buffer = new byte[4096];

            while (true) {
                int n = mic.read(buffer, 0, buffer.length);
                if (n <= 0) continue;

                if (!awake) {
                    if (wakeRec.acceptWaveForm(buffer, n)) {
                        String text = extractText(wakeRec.getResult());
                        if (isWake(text)) {
                            awake = true;
                            BeepPlayer.wake();
                            wakeRec.reset();
                            cmdRec.reset();
                            System.out.println("👂 Prime awake — say a command");
                        }
                    }
                    continue;
                }

                // Awake → listen ONLY for one command
                if (cmdRec.acceptWaveForm(buffer, n)) {
                    String text = extractText(cmdRec.getResult());
                    if (!text.isEmpty()) {
                        System.out.println("🗣 Command heard: " + text);
                        CommandResponse res = CommandProcessor.process(text);
                        if (!"unknown".equals(res.getCommand())) {
                            BeepPlayer.success();
                            CommandExecutor.execute(res);
                        } else {
                            BeepPlayer.error();
                            System.out.println("❌ Unknown command");
                        }
                        // go back to sleep after ONE command
                        awake = false;
                        wakeRec.reset();
                        cmdRec.reset();
                        BeepPlayer.sleepBeep();
                        System.out.println("🛌 Prime sleeping…");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWake(String text) {
        return text.equals("prime") || text.endsWith(" prime");
    }

    private String extractText(String json) {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            return obj.get("text").getAsString().trim().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }
}
