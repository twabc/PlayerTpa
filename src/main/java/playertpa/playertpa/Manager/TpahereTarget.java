package playertpa.playertpa.Manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpahereTarget {

    public final UUID uuid;
    public static Map<UUID, UUID> targetMap = new HashMap<>();

    public TpahereTarget(UUID uuid) {
        this.uuid = uuid;
    }

    public static UUID getTarget(Player player) {
        return targetMap.get(player.getUniqueId());
    }

    public static UUID getWhoTarget(Player player) {
        for (Player allPlayer : Bukkit.getOnlinePlayers()) {
            if (hasTarget(allPlayer)) {
                if (Bukkit.getPlayer(getTarget(allPlayer)).equals(player)) {
                    return allPlayer.getUniqueId();
                }
            }
        }
        return null;
    }

    public void setTarget(Player targetPlayer) {
        targetMap.put(uuid, targetPlayer.getUniqueId());
    }

    public void removeTarget(Player player) {
        if (hasTarget(player)) {
            targetMap.remove(player.getUniqueId());
        }
    }

    public static boolean hasTarget(Player player) {
        return targetMap.containsKey(player.getUniqueId());
    }

    public static boolean containsTargetValue(Player player) {
        return targetMap.containsValue(player.getUniqueId());
    }
}
