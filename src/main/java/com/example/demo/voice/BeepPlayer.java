package com.example.demo.voice;

import java.awt.*;

public class BeepPlayer {

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }

    // 🔔 Wake beep (single short)
    public static void wake() {
        Toolkit.getDefaultToolkit().beep();
    }

    // ✅ Command accepted (double beep)
    public static void success() {
        Toolkit.getDefaultToolkit().beep();
        sleep(120);
        Toolkit.getDefaultToolkit().beep();
    }

    // ❌ Error / unknown command (long beep)
    public static void error() {
        Toolkit.getDefaultToolkit().beep();
        sleep(400);
        Toolkit.getDefaultToolkit().beep();
    }

    // 😴 Going to sleep (soft)
    public static void sleepBeep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
