package com.cota.biomes.getBiome;

import com.cota.biomes.files.playerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class managers {



    public static final long DOUBLE_PRESS_THRESHOLD = 500;
    public static Map<UUID, Long> lastRepairTime = new HashMap<>();


    public static boolean isCooldownActive(Player player, int sec) {
        long cooldownTime = sec; // 25 seconds cooldown, 20 ticks per second

        playerData pd = new playerData(player.getUniqueId().toString());


        if (player.hasPermission("biomes.bypass")) return false;


        return pd.getPlayerData().get("cooldowns") != null && (System.currentTimeMillis() - pd.getPlayerData().getLong("cooldowns"))/1000 < cooldownTime;
    }

    public static boolean isCooldownActiveSecond(Player player, int sec) {
        long cooldownTime = sec; // 25 seconds cooldown, 20 ticks per second

        playerData pd = new playerData(player.getUniqueId().toString());

        if (player.hasPermission("biomes.bypass")) return false;

        return pd.getPlayerData().get("cooldowns2") != null && (System.currentTimeMillis() - pd.getPlayerData().getLong("cooldowns2"))/1000 < cooldownTime;
    }

    public static void setCooldown(Player player) {
        playerData pd = new playerData(player.getUniqueId().toString());
        pd.getPlayerData().set("cooldowns", System.currentTimeMillis());
        pd.save();
    }

    public static void setCooldownSecond(Player player) {
        playerData pd = new playerData(player.getUniqueId().toString());
        pd.getPlayerData().set("cooldowns2", System.currentTimeMillis());
        pd.save();
    }

    public static void setStarted(Player p) {
        playerData pd = new playerData(p.getUniqueId().toString());
        pd.getPlayerData().set("started", true);
        pd.save();
    }

    public static void setStartedSecond(Player p) {
        playerData pd = new playerData(p.getUniqueId().toString());
        pd.getPlayerData().set("started2", true);
        pd.save();
    }


    public static double getMultiplier(Player p) {
        double multiplier =1.00;
        playerData pd = new playerData(p.getUniqueId().toString());

        if (p.getInventory().contains(Material.DRAGON_EGG)) {
            if (p.getWorld().getBiome(p.getLocation()).toString().toLowerCase().contains(pd.getPlayerData().getString("biome"))) {
                multiplier=1.75;
            }

        }else {
            if (!p.getWorld().getBiome(p.getLocation()).toString().toLowerCase().contains(pd.getPlayerData().getString("biome"))) {
                multiplier=0.5;
            }
        }
        return multiplier;
    }
}
