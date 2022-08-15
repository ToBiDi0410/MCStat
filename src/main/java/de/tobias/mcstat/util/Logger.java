package de.tobias.mcstat.util;

import org.bukkit.Bukkit;

public class Logger {

    public static String prefix = "§8[§bMCSTAT§8] ";

    public static void debug(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + "§8[§5DEBUG§8] §7" + msg);
    }
    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + "§8[§aINFO§8] §7" + msg);
    }

    public static void warn(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix + "§8[§6WARN§8] §e" + msg);
    }

    public static void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(prefix +"§8[§4ERROR§8] §c" + msg);
    }
}
