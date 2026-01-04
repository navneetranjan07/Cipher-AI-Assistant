package com.example.demo;

import com.example.demo.voice.SpeechRecognizer;

public class CipherAiAssistantApplicationTests {

    public static void main(String[] args) {
        System.out.println("🤖 Prime AI Assistant started");
        new SpeechRecognizer().startListening();
    }
}
