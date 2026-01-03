package com.example.demo.command;

import java.util.*;

public class CommandProcessor {

    // 🔑 Words that indicate OPEN intent
    private static final List<String> OPEN_KEYWORDS =
            List.of("open", "start", "launch", "run");

    // ❌ Words to ignore
    private static final List<String> NOISE_WORDS =
            List.of("please", "can", "you", "hey", "prime", "the", "a", "an");

    // 🎯 App aliases → canonical command
    private static final Map<String, String> APP_ALIASES = Map.ofEntries(
            Map.entry("calculator", "calculator"),
            Map.entry("calc", "calculator"),

            Map.entry("notepad", "notepad"),

            Map.entry("chrome", "chrome"),
            Map.entry("google chrome", "chrome"),

            Map.entry("camera", "camera"),

            Map.entry("spotify", "spotify"),
            Map.entry("music", "spotify"),

            Map.entry("whatsapp", "whatsapp"),
            Map.entry("what's app", "whatsapp"),

            Map.entry("vscode", "vscode"),
            Map.entry("vs code", "vscode"),
            Map.entry("visual studio", "vscode"),
            Map.entry("visual studio code", "vscode"),

            Map.entry("telegram", "telegram"),

            Map.entry("settings", "settings")
    );

    public static CommandResponse process(String text) {

        if (text == null || text.isBlank())
            return new CommandResponse("unknown", "");

        text = text.toLowerCase().trim();

        // 🧹 Remove noise words
        for (String noise : NOISE_WORDS) {
            text = text.replace(" " + noise + " ", " ");
        }

        // 🔍 Check if OPEN intent exists
        boolean openIntent = OPEN_KEYWORDS.stream().anyMatch(text::contains);
        if (!openIntent)
            return new CommandResponse("unknown", "");

        // 🎯 Match app alias
        for (Map.Entry<String, String> entry : APP_ALIASES.entrySet()) {
            if (text.contains(entry.getKey())) {
                String app = entry.getValue();
                return new CommandResponse(app, "Opening " + app);
            }
        }

        return new CommandResponse("unknown", "");
    }
}
