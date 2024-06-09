package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.util.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMCCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.console_use")));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(plugin.getPermission("gmc"))) {
            player.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.gamemode.creative")));
        return true;
    }
}
