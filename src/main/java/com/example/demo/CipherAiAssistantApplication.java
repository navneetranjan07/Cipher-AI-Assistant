package com.example.demo;

public class CipherAiAssistantApplication {

    public static void main(String[] args) {
        System.out.println("🤖 Prime AI Assistant started");

        new Thread(() -> {
            new com.example.demo.voice.SpeechRecognizer().startListening();
        }).start();
    }
}
