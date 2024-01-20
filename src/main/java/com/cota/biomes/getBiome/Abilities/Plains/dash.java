package com.cota.biomes.getBiome.Abilities.Plains;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import static com.cota.biomes.getBiome.managers.*;

public class dash implements Listener {



    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();

        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("plains")) {


            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 60)) {
                        if (player.isOnGround()) {
                            dash(player);
                            setStarted(player);
                        }
                    }else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cOn cooldown"));
                    }
                }

                lastShiftPressTime = currentTime;


            }
        }
    }

    private void dash(Player player) {



        Vector direction = player.getLocation().getDirection().setY(0.01).normalize().multiply(15*getMultiplier(player));

        player.setVelocity(direction);

        setCooldown(player);

    }


}
