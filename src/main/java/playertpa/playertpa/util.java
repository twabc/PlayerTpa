package playertpa.playertpa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {

    private static PlayerTpa plugin;
    public util(PlayerTpa plugin) {
        this.plugin = plugin;
    }

    public static String formatText(String text) {
        FileConfiguration messageFile = plugin.plugin.file.getConfig();
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
        FileConfiguration messageFile = plugin.plugin.file.getConfig();
        List<String> commandTutorial = messageFile.getStringList("Command-tutorial");
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
}
