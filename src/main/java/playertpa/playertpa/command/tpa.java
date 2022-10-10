package playertpa.playertpa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import playertpa.playertpa.Manager.Target;
import playertpa.playertpa.Manager.TpahereTarget;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

import java.util.ArrayList;
import java.util.List;

public class tpa implements CommandExecutor, TabCompleter {

    private PlayerTpa plugin;
    public tpa(PlayerTpa plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.messageFile.getConfig();

        if (!(sender instanceof Player)) {
           util.onlyPlayerCanSendCmd();
           return true;
        }

        Player player = (Player)sender;

        Target tpaTarget = new Target(player.getUniqueId());
        TpahereTarget tpahereTarget = new TpahereTarget(player.getUniqueId());

        if (label.equalsIgnoreCase("tpa")) {
            if (args.length == 0) {
               util.commandTutorial(player);
               return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
               util.commandTutorial(player);
               return true;
            }

            if (args[0].equalsIgnoreCase("admin")) {
               if (player.hasPermission("PlayerTpa.admin")) {
                   if (args.length == 1) {
                      util.adminCommandTutorial(player);
                   }

                   if (args.length == 2) {
                      switch (args[1])
                      {
                          case "reload":
                              plugin.messageFile.reloadConfig();
                              plugin.settingFile.reloadConfig();
                              player.sendMessage(util.formatText(messageFile.getString("Admin-message.reload-file")));
                              break;
                          case "help":
                              util.adminCommandTutorial(player);
                              break;
                      }
                      return true;
                   }
               }
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
                    player.sendMessage(util.formatText(messageFile.getString("Sender.already-has-pending-requests").replace("{Player}", targetPlayer.getName())));
                    return false;
                }

                tpaTarget.setTarget(Bukkit.getPlayer(args[0]));

                targetPlayer.sendMessage(util.formatText(messageFile.getString("Receiver.tpa-receives-request").replace("{Player}", player.getName())));
                targetPlayer.spigot().sendMessage(util.tpaButton());
                player.spigot().sendMessage(util.cancelTpaButton(targetPlayer));

                (new BukkitRunnable() {
                    public void run() {
                        tpaTarget.removeTarget(player);
                        player.sendMessage(util.formatText(messageFile.getString("Sender.request-outdated").replace("{Player}", targetPlayer.getName())));
                    }
                }).runTaskLaterAsynchronously(plugin.plugin, 6000L);

            } else {
                util.commandTutorial(player);
                return true;
            }
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<String>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                arguments.add(player.getName());
            }
            arguments.add("help");
            if (sender.isOp()) {
                arguments.add("admin");
            }
            return arguments;
        }
        switch (args[0])
        {
            case "admin":
              arguments.add("help");
              arguments.add("reload");
              return arguments;
        }
        return null;
    }
}
