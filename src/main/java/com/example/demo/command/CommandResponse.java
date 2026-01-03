package com.example.demo.command;

public class CommandResponse {

    private final String command;
    private final String message;

    public CommandResponse(String command, String message) {
        this.command = command;
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }
}
