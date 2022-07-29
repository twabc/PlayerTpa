package playertpa.playertpa;

import org.bukkit.ChatColor;

public class util {

    public static String formatText(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
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
}
