package playertpa.playertpa.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.ClickEvent;
import playertpa.playertpa.Manager.Target;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

public class tpa implements CommandExecutor {

    private PlayerTpa plugin;
    public tpa(PlayerTpa plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.plugin.file.getConfig();

        if (!(sender instanceof Player)) {
            util.onlyPlayerCanSendCmd();
            return true;
        }

        Player player = (Player)sender;
        Target target = new Target(player.getUniqueId());

        if (label.equalsIgnoreCase("tpa")) {
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

                if (target.hasTarget(player)) {
                    player.sendMessage(util.formatText(messageFile.getString("Sender.already-has-pending-requests")));
                    return false;
                }

                target.setTarget(Bukkit.getPlayer(args[0]));

                String acceptButton = messageFile.getString("Click-button.accept");
                String denyButton = messageFile.getString("Click-button.deny");
                String senderMessage = messageFile.getString("Sender.successful-send-request").replace("{Player}", targetPlayer.getName()).replace("{Prefix}", messageFile.getString("Prefix"));

                BaseComponent[] button = new ComponentBuilder(util.replaceHexColour(acceptButton))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes"))
                        .color(util.getHexColour(acceptButton))
                        .append(util.replaceHexColour(denyButton))
                        .color(util.getHexColour(denyButton))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpno"))
                        .create();

                BaseComponent[] cancelButton = new ComponentBuilder(util.replaceHexColour(senderMessage))
                        .color(util.getHexColour(messageFile.getString("Sender.successful-send-request").replace("{Player}", targetPlayer.getName())))
                        .append(util.replaceHexColour(messageFile.getString("Click-button.cancel")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpcancel"))
                        .color(util.getHexColour(messageFile.getString("Click-button.cancel")))
                        .create();

                targetPlayer.sendMessage(util.formatText(messageFile.getString("Receiver.receives-request").replace("{Player}", player.getName())));
                targetPlayer.spigot().sendMessage(button);
                player.spigot().sendMessage(cancelButton);

                (new BukkitRunnable() {
                    public void run() {
                        target.removeTarget(player);
                        player.sendMessage(util.formatText(messageFile.getString("Sender.request-outdated").replace("{Player}", targetPlayer.getName())));
                    }
                }).runTaskLaterAsynchronously(plugin.plugin, 6000L);

            } else {
                util.commandTutorial(player);
                return true;
            }
            return true;
        }

        if (label.equalsIgnoreCase("tpaccept")|| label.equalsIgnoreCase("tpyes")) {
            if (target.containsTargetValue(player)) {
                Player targetPlayer = Bukkit.getPlayer(target.getWhoTarget(player));
                targetPlayer.teleport(player.getLocation());
                target.removeTarget(targetPlayer);

                player.sendMessage(util.formatText(messageFile.getString("Receiver.accept-request")));
                targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-accept-request").replace("{Player}", player.getName())));
                return true;
            } else {
                player.sendMessage(util.formatText(messageFile.getString("Sender.did-not-receive-any-requests")));
                return true;
            }
        }

        if (label.equalsIgnoreCase("tpdeny") || label.equalsIgnoreCase("tpno")) {
            if (target.containsTargetValue(player)) {
                Player targetPlayer = Bukkit.getPlayer(target.getWhoTarget(player));
                targetPlayer.sendMessage(util.formatText(messageFile.getString("Sender.receiver-deny-request").replace("{Player}", player.getName())));
                player.sendMessage(util.formatText(messageFile.getString("Receiver.deny-request")));
                target.removeTarget(targetPlayer);
                return true;
            } else {
                sender.sendMessage(util.formatText(messageFile.getString("Sender.did-not-receive-any-requests")));
                return true;
            }
        }

        if (command.getName().equals("tpcancel")) {
            if (target.hasTarget(player)) {
                target.removeTarget(player);
                player.sendMessage(util.formatText(messageFile.getString("Sender.successful-retract-request")));
            }
            return true;
        }
        return true;
    }
}
