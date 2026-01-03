package com.example.demo.command;

public class CommandProcessor {

    public static CommandResponse process(String text) {

        if (text == null || text.isBlank())
            return new CommandResponse("unknown", "");

        text = text.toLowerCase();

        if (text.contains("calculator"))
            return new CommandResponse("calculator", "Opening Calculator");

        if (text.contains("notepad"))
            return new CommandResponse("notepad", "Opening Notepad");

        if (text.contains("chrome"))
            return new CommandResponse("chrome", "Opening Chrome");

        if (text.contains("camera"))
            return new CommandResponse("camera", "Opening Camera");

        if (text.contains("spotify"))
            return new CommandResponse("spotify", "Opening Spotify");

        if (text.contains("whatsapp"))
            return new CommandResponse("whatsapp", "Opening WhatsApp");

        if (text.contains("vscode") || text.contains("visual studio"))
            return new CommandResponse("vscode", "Opening VS Code");

        if (text.contains("telegram"))
            return new CommandResponse("telegram", "Opening Telegram");

        if (text.contains("settings"))
            return new CommandResponse("settings", "Opening Settings");

        return new CommandResponse("unknown", "");
    }
}
