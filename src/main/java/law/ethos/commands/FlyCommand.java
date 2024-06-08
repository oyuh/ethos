package law.ethos.commands;

import law.ethos.methods.Essentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.fly")) {
                boolean isFlying = player.getAllowFlight();
                Essentials.setFly(player, !isFlying);
                player.sendMessage("Fly mode " + (!isFlying ? "enabled" : "disabled"));
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
