package law.ethos.util;

import org.bukkit.ChatColor;

public class ChatUtil {
    public static String colorize(String message) {
        if (message == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
