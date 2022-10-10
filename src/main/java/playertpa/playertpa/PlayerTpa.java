package playertpa.playertpa;

import org.bukkit.plugin.java.JavaPlugin;
import playertpa.playertpa.Files.messageFile;
import playertpa.playertpa.Files.settingFile;
import playertpa.playertpa.command.*;

public final class PlayerTpa extends JavaPlugin {

    public static PlayerTpa plugin;
    public messageFile messageFile;
    public settingFile settingFile;

    @Override
    public void onEnable() {
        plugin = this;

        this.messageFile = new messageFile(this);
        this.settingFile = new settingFile(this);

        setupCommands();
        setupTabCompleter();
    }

    @Override
    public void onDisable() {
    }

    public void setupCommands() {
        getCommand("tpahere").setExecutor(new tpahere(this));
        getCommand("tpa").setExecutor(new tpa(this));

        getCommand("tpaccept").setExecutor(new tpaccept(this));
        getCommand("tpyes").setExecutor(new tpaccept(this));

        getCommand("tpdeny").setExecutor(new tpdeny(this));
        getCommand("tpno").setExecutor(new tpdeny(this));

        getCommand("tpacancel").setExecutor(new tpacancel(this));
    }

    public void setupTabCompleter() {
        getCommand("tpa").setTabCompleter(new tpa(this));
    }
}
