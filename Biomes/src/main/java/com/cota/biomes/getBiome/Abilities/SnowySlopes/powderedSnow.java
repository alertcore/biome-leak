package com.cota.biomes.getBiome.Abilities.SnowySlopes;

import com.cota.biomes.files.playerData;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

import static com.cota.biomes.getBiome.managers.*;

public class powderedSnow implements Listener {


    public static HashMap<Player, Boolean> checking = new HashMap<>();

    private long lastShiftPressTime =0;
    @EventHandler
    public void Dash(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        String biome = player.getWorld().getBiome(player.getLocation()).name();
        playerData pd = new playerData(player.getUniqueId().toString());
        String Biome = pd.getPlayerData().getString("biome");

        if (Biome == null) return;

        if (Biome.contains("slopes")) {


            // Check if the player is sneaking
            if (event.isSneaking()) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShiftPressTime <= DOUBLE_PRESS_THRESHOLD) {
                    // Check if the time difference between the current and last shift press is within the threshold
                    if (!isCooldownActive(player, 30)) {


                        if (player.getTargetEntity(30) == null) return;
                        if (player.getTargetEntity(30) instanceof Player target) {
                            Freeze(player, target);
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

    private void Freeze(Player player, Player target) {


        target.setFreezeTicks(45*20*((int) Math.round(getMultiplier(player))) );


        setCooldown(player);


    }


}
