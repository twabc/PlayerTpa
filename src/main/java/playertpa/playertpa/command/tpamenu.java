package playertpa.playertpa.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import playertpa.playertpa.PlayerTpa;
import playertpa.playertpa.util;

import java.util.ArrayList;
import java.util.List;

public class tpamenu implements CommandExecutor {

    public PlayerTpa plugin;
    public tpamenu() {
        this.plugin = plugin.plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messageFile = plugin.messageFile.getConfig();

        if (!(sender instanceof Player)) {
            util.onlyPlayerCanSendCmd();
            return true;
        }

        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 9*3, util.formatText(messageFile.getString("Inventory.title")));

        List<Player> playerList = new ArrayList<Player>();
        for (Player allPlayer : Bukkit.getOnlinePlayers()) {
            playerList.add(allPlayer);
        }

        for (int i = 0; i < playerList.size(); i++) {
            Player targetPlayer = playerList.get(i);
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            itemMeta.setOwner(targetPlayer.getName());


            if (i < 45) {
                inventory.setItem(i, item);
            }
        }
        return true;
    }
}
