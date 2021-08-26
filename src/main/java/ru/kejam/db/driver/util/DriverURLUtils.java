package ru.kejam.db.driver.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DriverURLUtils {
    public static boolean parseURL(String url) {
        if (url == null || url.isEmpty()) return false;
        final String[] parts = url.split(":");
        if (parts.length < 4) return false;
        if (!parts[0].equalsIgnoreCase("jdbc")) {
            log.error("Wrong protocol! Pleas use 'jdbc'! Current value is {}", parts[0]);
            return false;
        }
        if (!parts[1].equalsIgnoreCase("kejamdbsql")) {
            log.error("Wrong database name! Please use 'kejamdbsql'");
            return false;
        }
        if (!parts[2].startsWith("//")) {
            log.error("Address must starts with '//");
        }
        return true;
    }

    public static String returnClearURL(String url) {
        final String[] parts = url.split(":");
        final String clearURL = "http:" + parts[2] + ":" + parts[3];
        if (clearURL.endsWith("/")) return clearURL;
        return clearURL + "/";
    }
}
