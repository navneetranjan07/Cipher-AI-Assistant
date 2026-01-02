package com.example.demo.command;

public class CommandResponse {

    private final String command;
    private final String speech;

    public CommandResponse(String command, String speech) {
        this.command = command;
        this.speech = speech;
    }

    public String getCommand() {
        return command;
    }

    public String getSpeech() {
        return speech;
    }
}
