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

public class tpdeny implements CommandExecutor {

    public PlayerTpa plugin;
    public tpdeny(PlayerTpa playerTpa) {
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

        if (label.equalsIgnoreCase("tpdeny") || label.equalsIgnoreCase("tpno")) {
            if (tpaTarget.containsTargetValue(player)) {
                Player targetPlayer = Bukkit.getPlayer(tpaTarget.getWhoTarget(player));
                targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-deny-request").replace("{Player}", player.getName())));
                player.sendMessage(util.formatText(messageFile.getString("Receiver.deny-request")));
                tpaTarget.removeTarget(targetPlayer);
                return true;
            } else
            if (tpahereTarget.containsTargetValue(player)) {
                Player targetPlayer = Bukkit.getPlayer(tpahereTarget.getWhoTarget(player));
                targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-deny-request").replace("{Player}", player.getName())));
                player.sendMessage(util.formatText(messageFile.getString("Receiver.deny-request")));
                tpahereTarget.removeTarget(targetPlayer);
                return true;
            } else {
                sender.sendMessage(util.formatText(messageFile.getString("Sender.did-not-receive-any-requests")));
                return true;
            }
        }
        return true;
    }
}
