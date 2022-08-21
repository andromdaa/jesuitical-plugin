package io.github.andromdaa.listeners;

import io.github.andromdaa.Plugin;
import jdk.jfr.Timespan;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class EventListener implements Listener {
    private final Plugin plugin;
    private final NamespacedKey deathKey;

    public EventListener(Plugin plugin) {
        this.plugin = plugin;
        this.deathKey = new NamespacedKey(plugin, "lastdeath");
    }


    @EventHandler
    public void firstJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        PersistentDataContainer container = p.getPersistentDataContainer();

        if (!container.has(deathKey, PersistentDataType.LONG)) {
            container.set(deathKey, PersistentDataType.LONG, Instant.now().toEpochMilli());
        }

    }

    @EventHandler
    public void deathListener(PlayerDeathEvent event) {
        Player p = event.getEntity().getPlayer();
        PersistentDataContainer container = p.getPersistentDataContainer();

        long lastDeath = container.get(deathKey, PersistentDataType.LONG);
        long now = Instant.now().toEpochMilli();
        container.set(deathKey, PersistentDataType.LONG, Instant.now().toEpochMilli());
        now -= lastDeath;

        int trigger = plugin.getConfig().getInt("config.trigger");
        trigger *= 60;

        int total_secs = (int) TimeUnit.MILLISECONDS.toSeconds(now);

        if(total_secs < trigger) return;

        int secs = (int) ((now / (1000)) % 60);
        int mins = (int) ((now / (1000*60)) % 60);
        int hours = (int) ((now / (1000*60*60)) % 24);
        int days = (int) (now / (1000*60*60*24));

        String deathMessage = plugin.getConfig().getString("config.message");

        deathMessage = deathMessage.replaceAll("\\$days", String.valueOf(days));
        deathMessage = deathMessage.replaceAll("\\$hours", String.valueOf(hours));
        deathMessage = deathMessage.replaceAll("\\$mins", String.valueOf(mins));
        deathMessage = deathMessage.replaceAll("\\$secs", String.valueOf(secs));
        deathMessage = deathMessage.replaceAll("\\$playerName", p.getDisplayName());

        deathMessage = ChatColor.translateAlternateColorCodes('&', deathMessage);

        p.sendMessage(deathMessage);
    }


}
