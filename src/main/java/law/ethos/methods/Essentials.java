package law.ethos.methods;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Essentials {
    public static void setFly(Player player, boolean enable) {
        player.setAllowFlight(enable);
        player.setFlying(enable);
    }

    public static void setGameMode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public static void teleport(Player player, Player target) {
        player.teleport(target.getLocation());
    }
}
