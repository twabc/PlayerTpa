package playertpa.playertpa.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.event.tpaevent;
import playertpa.playertpa.util;

public class tpa implements CommandExecutor {

    private PlayerTpa plugin;
    public tpa(PlayerTpa plugin) {
        this.plugin = plugin;
    }
    public static HashMap<UUID, UUID> targetMap = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration file = plugin.plugin.file.getConfig();
        String prefix = Color(file.getString("Prefix"));
        List<String> command_tutorial_list = file.getStringList("Command-tutorial");

        if (!(sender instanceof Player)) {
            sender.sendMessage(Color(file.getString("Only-player-can-use-this-command")).replace("{Prefix}", prefix));
            return true;
        }

        if (command.getName().equals("tpa")) {
            if (args[0].equals("help")) {
             for (String string : command_tutorial_list) {
                 sender.sendMessage(Color(string));
                 }
             return true;
            }

            if (args.length == 1) {
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    sender.sendMessage(Color(file.getString("Player-not-online").replace("{Prefix}", prefix)));
                    return true;
                }

                Player player_target = Bukkit.getPlayer(args[0]);
                final Player player_sender = (Player) sender;

                if (player_target.getUniqueId().equals(player_sender.getUniqueId())) {
                    sender.sendMessage(Color(file.getString("Teleport-to-myself").replace("{Prefix}", prefix)));
                    return true;
                }

                if (targetMap.containsKey(player_sender.getUniqueId())) {
                    sender.sendMessage(Color(file.getString("Already-has-pending-requests").replace("{Prefix}", prefix)));
                    return false;
                }

                TextComponent message = new TextComponent(Color(file.getString("Click-to-accept-request")));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes"));
                TextComponent message2 = new TextComponent(Color(file.getString("Click-to-deny-request")));
                message2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpno"));
                player_target.sendMessage(Color(file.getString("Receiver-receives-the-request").replace("{Player}", sender.getName()).replace("{Prefix}", prefix)));
                player_target.spigot().sendMessage(message);
                player_target.spigot().sendMessage(message2);
                targetMap.put(player_sender.getUniqueId(), player_target.getUniqueId());
                TextComponent cancelevent = new TextComponent(Color(file.getString("Click-to-retract-request").replace("{Prefix}", prefix)));
                cancelevent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpcancel"));

                sender.sendMessage(Color(file.getString("Send-request").replace("{Player}", player_target.getName()).replace("{Prefix}", prefix)));
                sender.spigot().sendMessage(cancelevent);
                (new BukkitRunnable() {
                    public void run() {
                        tpa.targetMap.remove(player_sender.getUniqueId());
                    }
                }).runTaskLaterAsynchronously((Plugin) this.plugin, 6000L);

            } else {
                for (String string : command_tutorial_list) {
                    sender.sendMessage(Color(string));
                }
                return true;
            }
            return true;
        }

        if (command.getName().equals("tpaccept") || command.getName().equals("tpyes")) {
            final Player senderP = (Player) sender;
            if (targetMap.containsValue(senderP.getUniqueId())) {
                sender.sendMessage(Color(file.getString("Accept-request")).replace("{Prefix}", prefix));
                for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                    if (((UUID) entry.getValue()).equals(senderP.getUniqueId())) {
                        Player tpRequester = Bukkit.getPlayer(entry.getKey());
                        tpaevent event = new tpaevent(tpRequester, tpRequester.getLocation());
                        Bukkit.getPluginManager().callEvent(event);
                        tpRequester.sendMessage(Color(file.getString("Receiver-accept-request").replace("{Prefix}", prefix).replace("{Player}", senderP.getName())));
                        tpRequester.teleport((Entity) senderP);
                        targetMap.remove(entry.getKey());
                        break;
                    }
                }
            } else {
                sender.sendMessage(Color(file.getString("Did-not-receive-any-requests").replace("{Prefix}", prefix)));
            }
            return true;
        }
        if (command.getName().equals("tpdeny") || command.getName().equals("tpno")) {
            final Player senderP = (Player) sender;
            if (targetMap.containsValue(senderP.getUniqueId())) {
                for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                    if (((UUID) entry.getValue()).equals(senderP.getUniqueId())) {
                        targetMap.remove(entry.getKey());
                        Player originalSender = Bukkit.getPlayer(entry.getKey());
                        originalSender.sendMessage(Color(file.getString("Receiver-deny-request").replace("{Prefix}", prefix).replace("{Player}", sender.getName())));
                        sender.sendMessage(Color(file.getString("Deny-request").replace("{Prefix}", prefix)));
                        break;
                    }
                }
            } else {
                sender.sendMessage(Color(file.getString("Did-not-receive-any-requests").replace("{Prefix}", prefix)));
            }
            return true;
        }
        if (command.getName().equals("tpcancel")) {
            final Player senderP = (Player) sender;
            for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                targetMap.remove(entry.getKey());
                senderP.sendMessage(Color(file.getString("Retract-request").replace("{Prefix}", prefix)));
            }
            return false;
        }
        return false;
    }

    public String Color(String string) {
        return util.formatText(string);
    }

}
