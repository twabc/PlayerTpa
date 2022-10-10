package playertpa.playertpa;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {

    private static PlayerTpa plugin;
    public util(PlayerTpa plugin) {
        this.plugin = plugin;
    }
    public static FileConfiguration messageFile = plugin.plugin.messageFile.getConfig();

    public static String formatText(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text.replace("{Prefix}", messageFile.getString("Prefix")));
        for (ChatColor style : ChatColor.values())
            try {
                int from = 0;
                while (text.indexOf("&#", from) >= 0) {
                    from = text.indexOf("&#", from) + 1;
                    text = text.replace(text.substring(from - 1, from + 7),
                            net.md_5.bungee.api.ChatColor.of(text.substring(from, from + 7)).toString());
                }
            } catch (Throwable t) {
            }
        return text;
    }

    public static void onlyPlayerCanSendCmd() {
        Bukkit.getLogger().info("Only player can use this command !");
    }

    public static void commandTutorial(Player player) {
        List<String> commandTutorial = messageFile.getStringList("Command-tutorial");
        for (String message : commandTutorial) {
            player.sendMessage(util.formatText(message));
        }
    }

    public static void adminCommandTutorial(Player player) {
        List<String> commandTutorial = messageFile.getStringList("Admin-command-tutorial");
        for (String message : commandTutorial) {
            player.sendMessage(util.formatText(message));
        }
    }

    public static String replaceHexColour(String message) {
        Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        while (matcher.find()) {
            message = message.replace(matcher.group(), "");
        }
        return message;
    }

    public static net.md_5.bungee.api.ChatColor getHexColour(String message) {
        Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            return net.md_5.bungee.api.ChatColor.of(matcher.group().replace("&", ""));
        }
        return net.md_5.bungee.api.ChatColor.WHITE;
    }

    public static BaseComponent[] tpaButton() {
        String acceptButton = messageFile.getString("Click-button.accept");
        String denyButton = messageFile.getString("Click-button.deny");

        BaseComponent[] button = new ComponentBuilder(util.replaceHexColour(acceptButton))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes"))
                .color(util.getHexColour(acceptButton))
                .append(util.replaceHexColour(denyButton))
                .color(util.getHexColour(denyButton))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpno"))
                .create();
        return button;
    }

    public static BaseComponent[] cancelTpaButton(Player targetPlayer) {
        String senderMessage = messageFile.getString("Sender.successful-send-request").replace("{Player}", targetPlayer.getName()).replace("{Prefix}", messageFile.getString("Prefix"));

        BaseComponent[] cancelButton = new ComponentBuilder(util.replaceHexColour(senderMessage))
                .color(util.getHexColour(messageFile.getString("Sender.successful-send-request").replace("{Player}", targetPlayer.getName())))
                .append(util.replaceHexColour(messageFile.getString("Click-button.cancel")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel"))
                .color(util.getHexColour(messageFile.getString("Click-button.cancel")))
                .create();
        return cancelButton;
    }

    public static void tpa(Player player, Player targetPlayer) {
        FileConfiguration settingFile = plugin.plugin.settingFile.getConfig();

        if (settingFile.getBoolean("Delay.enabled")) {
            final int[] second = {settingFile.getInt("Delay.second")};

            (new BukkitRunnable() {
                public void run() {

                if (settingFile.getBoolean("Delay.message")) {
                    player.sendMessage(util.formatText(messageFile.getString("Delay-message.message").replace("{second}", second[0] + "")));
                }

                if (settingFile.getBoolean("Delay.title")) {
                    player.sendTitle(util.formatText(messageFile.getString("Delay-message.title").replace("{second}", second[0] + "")), util.formatText(messageFile.getString("Delay-message.subtitle").replace("{second}", second[0] + "")));
                }

                if (settingFile.getBoolean("Delay.actionbar")) {
                    player.sendActionBar(util.formatText(messageFile.getString("Delay-message.actionbar").replace("{second}", second[0] + "")));
                }

                second[0] = second[0] - 1;

                if (second[0] < 0) {
                    player.teleport(targetPlayer);
                    this.cancel();
                }
                }
            }).runTaskTimer(plugin.plugin, 20, 20);
            return;
        }
        player.teleport(targetPlayer);
    }
}
