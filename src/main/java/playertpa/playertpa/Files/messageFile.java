package playertpa.playertpa.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import playertpa.playertpa.PlayerTpa;

public class messageFile {

    private PlayerTpa plugin;
    private FileConfiguration messageFile = null;
    private File configFile = null;


    public messageFile(PlayerTpa plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "message.yml");

        this.messageFile = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream a = this.plugin.getResource("message.yml");
        if(a != null) {
            YamlConfiguration b = YamlConfiguration.loadConfiguration(new InputStreamReader(a));
            this.messageFile.setDefaults(b);
        }
    }

    public FileConfiguration getConfig() {
        if(this.messageFile == null)
            reloadConfig();
        return this.messageFile;
    }

    public void saveConfig() {
        if(this.messageFile == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "無法儲存資料夾" + this.getConfig(), e);
        }
    }

    public void saveDefaultConfig() {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "message.yml");

        if(!this.configFile.exists()) {
            this.plugin.saveResource("message.yml", false);
        }
    }
}