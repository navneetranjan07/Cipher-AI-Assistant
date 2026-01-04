package com.example.demo.command;

import java.util.Map;

public class CommandProcessor {

    private static final Map<String, String> MAP = Map.ofEntries(
            Map.entry("open chrome", "chrome"),
            Map.entry("open notepad", "notepad"),
            Map.entry("open calculator", "calculator"),
            Map.entry("open camera", "camera"),
            Map.entry("open spotify", "spotify"),
            Map.entry("open whatsapp", "whatsapp"),
            Map.entry("open vscode", "vscode"),
            Map.entry("open telegram", "telegram"),
            Map.entry("open settings", "settings")
    );

    public static CommandResponse process(String text) {
        if (text == null || text.isBlank())
            return new CommandResponse("unknown", "");

        text = text.toLowerCase().trim();

        for (var e : MAP.entrySet()) {
            if (text.equals(e.getKey())) {
                return new CommandResponse(e.getValue(), "Opening " + e.getValue());
            }
        }
        return new CommandResponse("unknown", "");
    }
}
