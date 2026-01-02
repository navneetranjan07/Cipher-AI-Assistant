package com.example.demo.command;

public class CommandProcessor {

    public static CommandResponse process(String text) {

        if (text.contains("open chrome"))
            return new CommandResponse("chrome", "Opening Chrome");

        if (text.contains("open notepad"))
            return new CommandResponse("notepad", "Opening Notepad");

        if (text.contains("open calculator"))
            return new CommandResponse("calculator", "Opening Calculator");

        if (text.contains("open camera"))
            return new CommandResponse("camera", "Opening Camera");

        if (text.contains("time"))
            return new CommandResponse("time", "Telling you the time");

        return new CommandResponse("unknown", "Sorry boss, I don't understand");
    }
}
