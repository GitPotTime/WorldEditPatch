package fr.pottime.wepatch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class WEPatch extends JavaPlugin implements Listener {

    private HashMap<Player, Integer> hashMap;

    @Override
    public void onEnable() {
        if (!getConfig().getBoolean("active", true)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        hashMap = new HashMap<Player, Integer>();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("//calc") || event.getMessage().startsWith("//eval")) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            boolean kickPlayer = getConfig().getBoolean("kickPlayer", true);
            if (hashMap.get(player) == null) {
                hashMap.put(player, 0);
            }
            int count = hashMap.get(player);
            if (count == 4) {
                if (kickPlayer) {
                    kick(player);
                    return;
                }
            }
            hashMap.put(player, count + 1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("blockCommand")));
        }
    }

    private void kick(Player player) {
        String kickMessage = getConfig().getString("kickMessage");
        if (kickMessage == null || kickMessage.isEmpty()) {
            kickMessage = "You got kicked!";
        }
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
    }
}
