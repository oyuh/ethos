package law.ethos.methods;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.Arrays;

public class Colors {
    public static List<String> getColors() {
        return Arrays.asList(
                "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD",
                "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE"
        );
    }

    public static ChatColor getColor(String colorName) {
        return ChatColor.valueOf(colorName.toUpperCase());
    }
}
