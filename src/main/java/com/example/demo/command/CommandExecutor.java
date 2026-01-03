package com.example.demo.command;

import com.example.demo.system.AppLauncher;

public class CommandExecutor {

    public static void execute(CommandResponse response) {

        if (response == null) return;

        if ("unknown".equals(response.getCommand())) {
            System.out.println("❌ Command ignored (unknown)");
            return;
        }

        System.out.println("✅ Executing: " + response.getCommand());
        AppLauncher.open(response.getCommand());
    }
}
