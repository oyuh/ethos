package law.ethos.commands;

import law.ethos.methods.Essentials;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.gmc")) {
                Essentials.setGameMode(player, GameMode.CREATIVE);
                player.sendMessage("Game mode set to creative.");
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
