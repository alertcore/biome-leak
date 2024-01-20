package com.cota.biomes.getBiome.Abilities.Savanna;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.cota.biomes.getBiome.managers.*;

public class repairArmor implements Listener {





    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("savanna")) {

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                // Check if the time difference between the current and last shift press is within the threshold
                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {

                    if (!isCooldownActive(player, 60)) {
                        // Repair armor
                        repairArmor(player);
                        applyHealthBoost(player);

                        setStarted(player);
                        // Update the last repair time
                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }

                }

                lastShiftPressTime = currentTime;
            }
        }
    }

    private void applyHealthBoost(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, -1+((int) Math.round(getMultiplier(player)))));
    }

    private void removeHealthBoost(Player player) {
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
    }

    private void repairArmor(Player player) {
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                // Check if the item has durability
                if (itemStack.getDurability() > 0) {
                    // Use the repair() method to set the item's durability to its maximum
                    itemStack.setDurability((short) 0);
                }
            }
        }
        setCooldown(player);

    }


}
