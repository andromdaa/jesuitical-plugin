package io.github.andromdaa;
import io.github.andromdaa.listeners.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {

        // register listeners
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        // config options
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
