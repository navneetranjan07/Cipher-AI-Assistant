package com.example.demo.command;

import com.example.demo.voice.TextToSpeech;

import java.time.LocalTime;

public class CommandExecutor {

    public static void execute(CommandResponse response) {

        TextToSpeech.speak(response.getSpeech());

        try {
            switch (response.getCommand()) {

                case "chrome" ->
                        Runtime.getRuntime().exec("cmd /c start chrome");

                case "notepad" ->
                        Runtime.getRuntime().exec("notepad");

                case "calculator" ->
                        Runtime.getRuntime().exec("calc");

                case "camera" ->
                        Runtime.getRuntime().exec("start microsoft.windows.camera:");

                case "time" -> {
                    String time = LocalTime.now().getHour() + " " +
                            LocalTime.now().getMinute();
                    TextToSpeech.speak("The time is " + time);
                }

                default ->
                        TextToSpeech.speak("Command not supported");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
