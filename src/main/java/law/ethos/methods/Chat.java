package law.ethos.methods;

import org.bukkit.entity.Player;

public class Chat {
    public static String formatChat(Player player, String message) {
        // Implement chat formatting
        return player.getDisplayName() + ": " + message;
    }
}
