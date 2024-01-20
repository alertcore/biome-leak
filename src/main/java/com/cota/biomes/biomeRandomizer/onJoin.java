package com.cota.biomes.biomeRandomizer;


import com.cota.biomes.files.playerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.cota.biomes.Biomes.plugin;


public class onJoin implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String r = randomizer();

        playerData pd = new playerData(p.getUniqueId().toString());
        if (pd.getPlayerData().get("selection") == null) {
            if (pd.getPlayerData().get("biome") == null) {
                pd.getPlayerData().set("selection", r);
                pd.save();
                p.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin().getConfig().getString("first-join-title")
                        .replaceAll("%biome%", r)), ChatColor.translateAlternateColorCodes('&', plugin().getConfig().getString("first-join-subtitle")), 5, 60, 5);
            }
        }else {
            if (pd.getPlayerData().get("biome") == null) {
                p.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin().getConfig().getString("first-join-title")
                        .replaceAll("%biome%", pd.getPlayerData().getString("selection"))), ChatColor.translateAlternateColorCodes('&', plugin().getConfig().getString("first-join-subtitle")), 5, 60, 5);
            }
        }

        if (pd.getPlayerData().getBoolean("shark") || pd.getPlayerData().getBoolean("warden")) {

            p.setInvisible(false);
            p.setRemainingAir(20);

            pd.getPlayerData().set("shark", false);
            pd.getPlayerData().set("warden", false);
            p.setMaxHealth(20);
            p.setHealth(20);
            pd.save();
        }

    }

    public String randomizer() {
        ArrayList<String> types = new ArrayList<>();

        types.add("plains");
        types.add("forest");
        types.add("savanna");
        types.add("cherry");
        types.add("jungle");
        types.add("slopes");
        types.add("ocean");
        types.add("taiga");
        types.add("spikes");
        types.add("dark");
        types.add("desert");
        types.add("swamp");
        types.add("badlands");
        types.add("mushroom");

        Random random = new Random();
        int randomValue = random.nextInt(types.size()); // Generating a random value between 0 and 100











        return types.get(randomValue);
    }



}
