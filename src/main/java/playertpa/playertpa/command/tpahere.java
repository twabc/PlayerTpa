package playertpa.playertpa.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import playertpa.playertpa.Manager.Target;
import playertpa.playertpa.Manager.TpahereTarget;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

public class tpahere implements CommandExecutor {

    public PlayerTpa plugin;
    public tpahere(PlayerTpa playerTpa) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.plugin.messageFile.getConfig();

        if (!(sender instanceof Player)) {
            util.onlyPlayerCanSendCmd();
            return true;
        }

        Player player = (Player)sender;

        Target tpaTarget = new Target(player.getUniqueId());
        TpahereTarget tpahereTarget = new TpahereTarget(player.getUniqueId());

        if (label.equalsIgnoreCase("tpahere")) {
            if (args.length == 0) {
                util.commandTutorial(player);
                return true;
            }

            if (args[0].equals("help")) {
                util.commandTutorial(player);
                return true;
            }

            if (args.length == 1) {
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    player.sendMessage(util.formatText(messageFile.getString("Sender.not-online")));
                    return true;
                }

                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(util.formatText(messageFile.getString("Sender.teleport-to-myself")));
                    return true;
                }

                if (tpaTarget.hasTarget(player) || tpahereTarget.hasTarget(player)) {
                    player.sendMessage(util.formatText(messageFile.getString("Sender.already-has-pending-requests")));
                    return false;
                }

                targetPlayer.sendMessage(util.formatText(messageFile.getString("Receiver.tpahere-receives-request").replace("{Player}", player.getName())));
                targetPlayer.spigot().sendMessage(util.tpaButton());
                player.spigot().sendMessage(util.cancelTpaButton(targetPlayer));

                tpahereTarget.setTarget(Bukkit.getPlayer(args[0]));

                (new BukkitRunnable() {
                    public void run() {
                        tpahereTarget.removeTarget(player);
                        player.sendMessage(util.formatText(messageFile.getString("Sender.request-outdated").replace("{Player}", targetPlayer.getName())));
                    }
                }).runTaskLaterAsynchronously(plugin.plugin, 6000L);
            } else {
                util.commandTutorial(player);
                return true;
            }
        }

        return true;
    }
}
