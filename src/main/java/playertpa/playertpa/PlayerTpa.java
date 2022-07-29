package playertpa.playertpa;

import org.bukkit.plugin.java.JavaPlugin;
import playertpa.playertpa.command.file;
import playertpa.playertpa.command.tpa;

public final class PlayerTpa extends JavaPlugin {

    public PlayerTpa plugin;
    public playertpa.playertpa.command.file file;

    @Override
    public void onEnable() {
        plugin = this;

        this.file = new file(this);
        setupCommands();
    }

    @Override
    public void onDisable() {
    }

    public void setupCommands() {
        getCommand("tpcancel").setExecutor(new tpa(this));
        getCommand("tpa").setExecutor(new tpa(this));
        getCommand("tpaccept").setExecutor(new tpa(this));
        getCommand("tpdeny").setExecutor(new tpa(this));
        getCommand("tpyes").setExecutor(new tpa(this));
        getCommand("tpno").setExecutor(new tpa(this));
    }
}