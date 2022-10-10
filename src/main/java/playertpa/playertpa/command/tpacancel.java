package playertpa.playertpa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import playertpa.playertpa.Manager.Target;
import playertpa.playertpa.Manager.TpahereTarget;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

public class tpacancel implements CommandExecutor {

    public PlayerTpa plugin;
    public tpacancel(PlayerTpa playerTpa) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.plugin.messageFile.getConfig();

        if (!(sender instanceof Player)) {
            util.onlyPlayerCanSendCmd();
            return true;
        }

        Player player = (Player) sender;

        TpahereTarget tpahereTarget = new TpahereTarget(player.getUniqueId());
        Target tpaTarget = new Target(player.getUniqueId());

        if (label.equalsIgnoreCase("tpacancel")) {
            if (tpaTarget.hasTarget(player) || tpahereTarget.hasTarget(player)) {
                player.sendMessage(util.formatText(messageFile.getString("Sender.successful-retract-request")));

                Player targetPlayer = null;
                if (tpaTarget.hasTarget(player)) {
                    targetPlayer = Bukkit.getPlayer(tpaTarget.getTarget(player));
                } else
                if (tpahereTarget.hasTarget(player)) {
                    targetPlayer = Bukkit.getPlayer(tpahereTarget.getTarget(player));
                }

                targetPlayer.sendMessage(util.formatText(messageFile.getString("Receiver.sender-retract-request").replace("{Player}", player.getName())));

                tpahereTarget.removeTarget(player);
                tpaTarget.removeTarget(player);
            }
            return true;
        }

        return true;
    }
}
