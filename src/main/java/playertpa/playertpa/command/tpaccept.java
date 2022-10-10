package playertpa.playertpa.command;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import playertpa.playertpa.Manager.Target;
import playertpa.playertpa.Manager.TpahereTarget;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

public class tpaccept implements CommandExecutor {

    public PlayerTpa plugin;
    public tpaccept(PlayerTpa playerTpa) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.plugin.messageFile.getConfig();

        if (!(sender instanceof Player)) {
            util.onlyPlayerCanSendCmd();
            return true;
        }

        Player player = (Player)sender;

        TpahereTarget tpahereTarget = new TpahereTarget(player.getUniqueId());
        Target tpaTarget = new Target(player.getUniqueId());

        if (label.equalsIgnoreCase("tpaccept") || label.equalsIgnoreCase("tpyes")) {
            if (tpaTarget.containsTargetValue(player)) {
                Player targetPlayer = Bukkit.getPlayer(tpaTarget.getWhoTarget(player));
                tpaTarget.removeTarget(targetPlayer);

                util.tpa(targetPlayer, player);
                player.sendMessage(util.formatText(messageFile.getString("Receiver.accept-request")));
                targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-accept-request").replace("{Player}", player.getName())));
                return true;
            } else
            if (tpahereTarget.containsTargetValue(player)) {
               Player targetPlayer = Bukkit.getPlayer(tpahereTarget.getWhoTarget(player));
               tpahereTarget.removeTarget(targetPlayer);

               util.tpa(player, targetPlayer);
               player.sendMessage(util.formatText(messageFile.getString("Receiver.accept-request")));
               targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-accept-request").replace("{Player}", player.getName())));
               return true;
            } else {
               player.sendMessage(util.formatText(messageFile.getString("Sender.did-not-receive-any-requests")));
               return true;
            }
        }
        return true;
    }
}
