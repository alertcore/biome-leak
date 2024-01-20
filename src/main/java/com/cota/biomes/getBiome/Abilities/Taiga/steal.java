package com.cota.biomes.getBiome.Abilities.Taiga;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

import static com.cota.biomes.getBiome.managers.*;

public class steal implements Listener {


    public static HashMap<Player, Boolean> checking = new HashMap<>();

    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("taiga")) {

            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 10 * 60)) {

                        if (player.getTargetEntity(30) == null) return;
                        if (player.getTargetEntity(3) instanceof Player target) {
                            Steal(player, target);
                            setStarted(player);
                            checking.put(player, true);
                        }


                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private void Steal(Player player, Player target) {


        player.openInventory(target.getInventory());

        setCooldown(player);




    }


}
